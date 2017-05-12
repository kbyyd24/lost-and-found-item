package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

import java.util.List;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.RETURN_ITEM_NOT_FOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("item/lost/{itemId}/returns")
@Produces(APPLICATION_JSON)
public class ReturnItemResource {

  private final ReturnItemService returnItemService;
  private final LostItemService lostItemService;
  private final UserService userService;

  @Autowired
  public ReturnItemResource(ReturnItemService returnItemService,
                            LostItemService lostItemService,
                            UserService userService) {
    this.returnItemService = returnItemService;
    this.lostItemService = lostItemService;
    this.userService = userService;
  }

  @GET
  public List<ReturnItemPageItem> getReturns(@PathParam("itemId") long lostItemId,
                             @HeaderParam("username") String username,
                             @HeaderParam("user-token") String userToken,
                             @QueryParam("page") @DefaultValue("1") int page,
                             @QueryParam("listSize") @DefaultValue("8") int listSize,
                             @QueryParam("sort") @DefaultValue("create_time") String sort) {
    checkGetAuth(lostItemId, username, userToken);
    ItemSort itemSort = ItemSort.getItemSortByColumnName(sort);
    return returnItemService
        .getReturnItemPageItems(lostItemId, page, listSize, itemSort);
  }

  @Path("{returnItemOwner}")
  @GET
  public ReturnItem getReturnItem(@PathParam("itemId") long lostItemId,
                                @PathParam("returnItemOwner") String returnItemOwner,
                                @HeaderParam("username") String requestUser,
                                @HeaderParam("user-token") String userToken) {
    checkGetAuth(lostItemId, returnItemOwner, requestUser, userToken);
    ReturnItem returnItem = returnItemService.getReturnItem(returnItemOwner, lostItemId);
    if (returnItem == null) {
      throw new NotFoundException(RETURN_ITEM_NOT_FOUND.getReason());
    }
    return returnItem;
  }

  @Path("{returnItemOwner}")
  @PUT
  @Consumes(APPLICATION_JSON)
  public ReturnItem putCreator(@PathParam("itemId") long lostItemId,
                               @PathParam("returnItemOwner") String returnItemOwner,
                               @HeaderParam("username") String requestUser,
                               @HeaderParam("user-token") String userToken,
                               ReturnItemCreator creator) {
    checkPutAuth(lostItemId, returnItemOwner, requestUser, userToken);
    return returnItemService.create(returnItemOwner, lostItemId, creator);
  }

  private void checkPutAuth(long lostItemId, String returnItemOwner,
                            String requestUser, String userToken) {
    if (!requestUser.equals(returnItemOwner)) {
      throw new UnauthorizedException("not owner");
    }
    UserState userState = userService.checkState(requestUser, userToken);
    if (userState != UserState.ONLINE) {
      throw new UnauthorizedException(userState.name());
    }
    if (lostItemService.isBelong(lostItemId, requestUser)) {
      throw new UnauthorizedException("can not return self item");
    }
  }

  private void checkGetAuth(long lostItemId, String username, String userToken) {
    this.checkGetAuth(lostItemId, null, username, userToken);
  }

  private void checkGetAuth(long lostItemId,
                            String returnItemOwner,
                            String requestUser,
                            String userToken) {
    UserState userState = userService.checkState(requestUser, userToken);
    if (userState != UserState.ONLINE) {
      throw new UnauthorizedException(userState.name());
    }
    if (!hasGetPermit(requestUser, returnItemOwner, lostItemId)) {
      throw new UnauthorizedException("not owner");
    }
  }

  private boolean hasGetPermit(String requestUser, String resourceOwner, long superResourceId) {
    return requestUser.equals(resourceOwner) || lostItemService.isBelong(superResourceId, requestUser);
  }

}
