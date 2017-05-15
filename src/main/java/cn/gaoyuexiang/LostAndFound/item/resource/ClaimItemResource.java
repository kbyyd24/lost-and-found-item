package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemBelongChecker;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemCloseChecker;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemServiceImpl;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.List;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.PAGE_OUT_OF_BOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("/item/found/{itemId}/claim")
@Produces(APPLICATION_JSON)
public class ClaimItemResource {

  private ClaimItemService claimItemService;
  private AuthService authService;
  private FoundItemCloseChecker closeChecker;
  private FoundItemBelongChecker belongChecker;

  public ClaimItemResource(ClaimItemService claimItemService,
                           AuthService authService,
                           FoundItemCloseChecker closeChecker,
                           FoundItemBelongChecker belongChecker) {
    this.claimItemService = claimItemService;
    this.authService = authService;
    this.closeChecker = closeChecker;
    this.belongChecker = belongChecker;
  }

  @GET
  public List<ClaimItemPageItem> loadPage(@PathParam("itemId") long itemId,
                                          @HeaderParam("username") String requestUser,
                                          @HeaderParam("user-token") String userToken,
                                          @QueryParam("page") @DefaultValue("1") int page,
                                          @QueryParam("size") @DefaultValue("8") int size,
                                          @QueryParam("sort") @DefaultValue("create_time")
                                              String sortColumnName) {
    UserRole userRole = authService.checkUserRole(itemId, requestUser, userToken, belongChecker);
    if (userRole != UserRole.SUPER_RESOURCE_OWNER) {
      throw new UnauthorizedException(userRole.name());
    }
    ItemSort sort = ItemSort.getItemSortByColumnName(sortColumnName);
    return claimItemService.loadPage(itemId, page, size, sort);
  }

  @GET
  @Path("{resourceOwner}")
  public ClaimItem loadOne(@PathParam("itemId") long foundItemId,
                           @PathParam("resourceOwner") String resourceOwner,
                           @HeaderParam("username") String requestUser,
                           @HeaderParam("user-token") String userToken) {
    if (authService.checkUserRole(foundItemId, resourceOwner, requestUser, userToken, belongChecker)
        == UserRole.NOT_OWNER) {
      throw new UnauthorizedException(UserRole.NOT_OWNER.name());
    }
    ClaimItem claimItem = claimItemService.loadOne(foundItemId, resourceOwner);
    if (claimItem == null) {
      throw new NotFoundException(NotFoundReason.CLAIM_ITEM_NOT_FOUND.getReason());
    }
    return claimItem;
  }

  @PUT
  @Path("{resourceOwner}")
  @Consumes(APPLICATION_JSON)
  public ClaimItem put(@PathParam("itemId") long foundItemId,
                       @PathParam("resourceOwner") String resourceOwner,
                       @HeaderParam("username") String requestUser,
                       @HeaderParam("user-token") String userToken,
                       ClaimItemCreator creator) {
    UserRole userRole =
        authService.checkUserRole(foundItemId, resourceOwner, requestUser, userToken, belongChecker);
    if (userRole != UserRole.RESOURCE_OWNER) {
      throw new UnauthorizedException(userRole.name());
    }
    if (closeChecker.isClosed(foundItemId)) {
      throw new UpdateItemException("found item closed");
    }
    return claimItemService.create(foundItemId, resourceOwner, creator);
  }

  @DELETE
  @Path("{resourceOwner}")
  public ClaimItem delete(@PathParam("itemId") long foundItemId,
                          @PathParam("resourceOwner") String resourceOwner,
                          @HeaderParam("username") String requestUser,
                          @HeaderParam("user-token") String userToken,
                          @HeaderParam("action-type") @DefaultValue("cancel")
                              String actionName) {
    UserRole userRole =
        authService.checkUserRole(foundItemId, resourceOwner, requestUser, userToken, belongChecker);
    ActionType action = ActionType.getByValue(actionName);
    if (!authService.checkAction(userRole, action)) {
      throw new UnauthorizedException("role and action not match");
    }
    if (closeChecker.isClosed(foundItemId))
    {
      throw new UpdateItemException("found item closed");
    }
    ClaimItem delete = claimItemService.delete(foundItemId, resourceOwner, action);
    return delete;
  }
}
