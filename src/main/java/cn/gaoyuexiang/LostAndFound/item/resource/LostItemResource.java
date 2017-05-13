package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.LOST_ITEM_NOT_EXIST;
import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.PAGE_OUT_OF_BOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

@Component
@Path("/item/lost")
@Produces(APPLICATION_JSON)
public class LostItemResource {

  private LostItemService lostItemService;
  private UserService userService;
  private AuthService authService;

  @Autowired
  public LostItemResource(LostItemService lostItemService,
                          UserService userService,
                          AuthService authService) {
    this.lostItemService = lostItemService;
    this.userService = userService;
    this.authService = authService;
  }

  @GET
  public List<LostItemPageItem> loadLostItemList(@QueryParam("page") @DefaultValue("1") int page,
                                   @QueryParam("listSize") @DefaultValue("8") int listSize,
                                   @QueryParam("sort") @DefaultValue("create_time") String sort) {
    ItemSort itemSort = ItemSort.getItemSortByColumnName(sort);
    List<LostItemPageItem> lostItemPageItems = lostItemService.loadPage(page, listSize, itemSort);
    if (lostItemPageItems.size() == 0) {
      throw new NotFoundException(PAGE_OUT_OF_BOUND.getReason());
    }
    return lostItemPageItems;
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
    return lostItemService.create(creator, username);
  }

  @GET
  @Path("{itemId}")
  public LostItem loadOneLostItem(@PathParam("itemId") long id) {
    LostItem lostItem = lostItemService.loadOne(id);
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
    UserRole userRole = authService.checkUserRole(id, username, userToken);
    if (userRole == UserRole.NOT_OWNER) {
      throw new UnauthorizedException(userRole.name());
    }
    return lostItemService.update(updater, id, username);
  }

  @DELETE
  @Path("{itemId}")
  public LostItem closeItem(@PathParam("itemId") long id,
                            @HeaderParam("username") String username,
                            @HeaderParam("user-token") String userToken) {
    UserState userState = userService.checkState(username, userToken);
    if (userState != UserState.ONLINE) {
      throw new UnauthorizedException(userState.name());
    }
    LostItem closedItem = lostItemService.close(id);
    if (closedItem == null) {
      throw new NotFoundException(LOST_ITEM_NOT_EXIST.getReason());
    }
    return closedItem;
  }

}
