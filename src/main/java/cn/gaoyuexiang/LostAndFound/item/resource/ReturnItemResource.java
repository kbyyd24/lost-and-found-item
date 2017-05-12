package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.RETURN_ITEM_NOT_FOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Component
@Path("item/lost/{itemId}/returns")
@Produces(APPLICATION_JSON)
public class ReturnItemResource {

  private final ReturnItemService returnItemService;
  private final LostItemService lostItemService;
  private final UserService userService;

  private ResponseBuilder responseBuilder;

  @Autowired
  public ReturnItemResource(ReturnItemService returnItemService,
                            LostItemService lostItemService,
                            UserService userService) {
    this.returnItemService = returnItemService;
    this.lostItemService = lostItemService;
    this.userService = userService;
    responseBuilder = new ResponseBuilder();
  }

  @GET
  public Response getReturns(@PathParam("itemId") long lostItemId,
                             @HeaderParam("username") String username,
                             @HeaderParam("user-token") String userToken,
                             @QueryParam("page") @DefaultValue("1") int page,
                             @QueryParam("listSize") @DefaultValue("8") int listSize,
                             @QueryParam("sort") @DefaultValue("create_time") String sort) {
    UserState userState = userService.checkState(username, userToken);
    if (userState != UserState.ONLINE) {
      return responseBuilder.build(UNAUTHORIZED, new Message(userState.name()));
    }
    if (!lostItemService.isBelong(lostItemId, username)) {
      return responseBuilder.build(UNAUTHORIZED, new Message("not belong"));
    }
    ItemSort itemSort = ItemSort.getItemSortByColumnName(sort);
    return responseBuilder
        .build(
            OK,
            returnItemService
                .getReturnItemPageItems(lostItemId, page, listSize, itemSort));
  }

  @Path("{returnItemOwner}")
  @GET
  public Response getReturnItem(@PathParam("itemId") long lostItemId,
                                @PathParam("returnItemOwner") String returnItemOwner,
                                @HeaderParam("username") String requestUser,
                                @HeaderParam("user-token") String userToken) {
    UserState userState = userService.checkState(requestUser, userToken);
    if (userState != UserState.ONLINE) {
      return responseBuilder.build(UNAUTHORIZED, new Message(userState.name()));
    }
    if (requestUser.equals(returnItemOwner)) {
      ReturnItem returnItem = returnItemService.getReturnItem(returnItemOwner, lostItemId);
      if (returnItem == null) {
        return responseBuilder.build(NOT_FOUND, new Message(RETURN_ITEM_NOT_FOUND.getReason()));
      }
      return responseBuilder.build(OK, returnItem);
    }
    if (lostItemService.isBelong(lostItemId, requestUser)) {
      ReturnItem returnItem = returnItemService.getReturnItem(returnItemOwner, lostItemId);
      if (returnItem == null) {
        return responseBuilder.build(NOT_FOUND, new Message(RETURN_ITEM_NOT_FOUND.getReason()));
      }
      return responseBuilder.build(OK, returnItem);
    }
    return responseBuilder.build(UNAUTHORIZED, new Message("not owner"));
  }

}
