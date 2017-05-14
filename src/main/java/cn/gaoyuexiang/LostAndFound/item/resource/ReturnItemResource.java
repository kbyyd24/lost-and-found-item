package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.LostItemServiceImpl;
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
  private LostItemServiceImpl lostItemServiceImpl;
  private AuthService authService;

  @Autowired
  public ReturnItemResource(ReturnItemService returnItemService,
                            LostItemServiceImpl lostItemServiceImpl,
                            AuthService authService) {
    this.returnItemService = returnItemService;
    this.lostItemServiceImpl = lostItemServiceImpl;
    this.authService = authService;
  }

  @GET
  public List<ReturnItemPageItem> getReturns(@PathParam("itemId") long lostItemId,
                             @HeaderParam("username") String username,
                             @HeaderParam("user-token") String userToken,
                             @QueryParam("page") @DefaultValue("1") int page,
                             @QueryParam("listSize") @DefaultValue("8") int listSize,
                             @QueryParam("sort") @DefaultValue("create_time") String sort) {
    if (authService.checkUserRole(lostItemId, username, userToken, lostItemServiceImpl) == UserRole.NOT_OWNER) {
      throw new UnauthorizedException(UserRole.NOT_OWNER.name());
    }
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
    UserRole userRole = authService.checkUserRole(lostItemId, returnItemOwner,
        requestUser, userToken, lostItemServiceImpl);
    if (userRole == UserRole.NOT_OWNER) {
      throw new UnauthorizedException(userRole.name());
    }
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
    UserRole userRole = authService.checkUserRole(lostItemId, returnItemOwner,
        requestUser, userToken, lostItemServiceImpl);
    if (userRole != UserRole.RESOURCE_OWNER) {
      throw new UnauthorizedException();
    }
    if (lostItemServiceImpl.isClosed(lostItemId)) {
      throw new UpdateItemException("lost item closed");
    }
    return returnItemService.create(returnItemOwner, lostItemId, creator);
  }

  @Path("{returnItemOwner}")
  @DELETE
  public ReturnItem changeItemState(@PathParam("itemId") long superResourceId,
                                    @PathParam("returnItemOwner") String resourceOwner,
                                    @HeaderParam("username") String requestUser,
                                    @HeaderParam("user-token") String userToken,
                                    @HeaderParam("action-type") @DefaultValue("cancel")
                                          String actionName) {
    UserRole userRole = authService.checkUserRole(superResourceId, resourceOwner,
        requestUser, userToken, lostItemServiceImpl);
    ActionType action = ActionType.getByValue(actionName);
    if (!authService.checkAction(userRole, action)) {
      throw new UnauthorizedException(action.getValue());
    }
    if (lostItemServiceImpl.isClosed(superResourceId)) {
      throw new UpdateItemException("lost item closed");
    }
    return returnItemService.delete(resourceOwner, superResourceId, action);
  }

}
