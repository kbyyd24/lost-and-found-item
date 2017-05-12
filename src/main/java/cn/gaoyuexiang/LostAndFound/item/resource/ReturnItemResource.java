package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
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
  public ReturnItem getReturnItem(@PathParam("itemId") long lostItemId,
                                @PathParam("returnItemOwner") String returnItemOwner,
                                @HeaderParam("username") String requestUser,
                                @HeaderParam("user-token") String userToken) {
    UserState userState = userService.checkState(requestUser, userToken);
    checkAuth(lostItemId, returnItemOwner, requestUser, userState);
    ReturnItem returnItem = returnItemService.getReturnItem(returnItemOwner, lostItemId);
    if (returnItem == null) {
      throw new NotFoundException(RETURN_ITEM_NOT_FOUND.getReason());
    }
    return returnItem;
  }

  private void checkAuth(long lostItemId,
                         String returnItemOwner,
                         String requestUser,
                         UserState userState) {
    if (userState != UserState.ONLINE) {
      throw new UnauthorizedException(userState.name());
    }
    if (!hasPermit(requestUser, returnItemOwner, lostItemId)) {
      throw new UnauthorizedException("not owner");
    }
  }

  private boolean hasPermit(String requestUser, String resourceOwner, long superResourceId) {
    return requestUser.equals(resourceOwner) || lostItemService.isBelong(superResourceId, requestUser);
  }

}
