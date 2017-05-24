package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.LostItemBelongChecker;
import cn.gaoyuexiang.LostAndFound.item.service.impl.LostItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.List;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.LOST_ITEM_NOT_EXIST;
import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.PAGE_OUT_OF_BOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("/item/lost")
@Produces(APPLICATION_JSON)
public class LostItemResource {

  private LostItemService lostItemServiceImpl;
  private LostItemBelongChecker belongChecker;
  private UserService userService;
  private AuthService authService;

  @Autowired
  public LostItemResource(LostItemService lostItemServiceImpl,
                          LostItemBelongChecker belongChecker,
                          UserService userService,
                          AuthService authService) {
    this.lostItemServiceImpl = lostItemServiceImpl;
    this.belongChecker = belongChecker;
    this.userService = userService;
    this.authService = authService;
  }

  @GET
  public List<LostItemPageItem> loadLostItemList(@QueryParam("page") @DefaultValue("1") int page,
                                   @QueryParam("listSize") @DefaultValue("8") int listSize,
                                   @QueryParam("sort") @DefaultValue("createTime") String sort) {
    ItemSort itemSort = ItemSort.getItemSortByColumnName(sort);
    return lostItemServiceImpl.loadPage(page, listSize, itemSort);
  }

  @POST
  @Consumes(APPLICATION_JSON)
  public LostItem createLostItem(@HeaderParam("username") String username,
                                 @HeaderParam("user-token") String token,
                                 LostItemCreator creator) {
    UserState userState = userService.checkState(username, token);
    if (userState != UserState.ONLINE) {
      throw new UnauthorizedException(userState.name());
    }
    return lostItemServiceImpl.create(creator, username);
  }

  @GET
  @Path("{itemId}")
  public LostItem loadOneLostItem(@PathParam("itemId") long id) {
    LostItem lostItem = lostItemServiceImpl.loadOne(id);
    if (lostItem == null) {
      throw new NotFoundException(LOST_ITEM_NOT_EXIST.getReason());
    }
    return lostItem;
  }

  @PUT
  @Path("{itemId}")
  @Consumes(APPLICATION_JSON)
  public LostItem updateLostItem(@PathParam("itemId") long id,
                                 @HeaderParam("username") String username,
                                 @HeaderParam("user-token") String userToken,
                                 LostItemCreator updater) {
    if (authService.checkUserRole(id, username, userToken, belongChecker) == UserRole.NOT_OWNER) {
      throw new UnauthorizedException(UserRole.NOT_OWNER.name());
    }
    return lostItemServiceImpl.update(updater, id, username);
  }

  @DELETE
  @Path("{itemId}")
  public LostItem closeItem(@PathParam("itemId") long id,
                            @HeaderParam("username") String username,
                            @HeaderParam("user-token") String userToken) {
    if (authService.checkUserRole(id, username, userToken, belongChecker) == UserRole.NOT_OWNER) {
      throw new UnauthorizedException(UserRole.NOT_OWNER.name());
    }
    return lostItemServiceImpl.close(id);
  }

}
