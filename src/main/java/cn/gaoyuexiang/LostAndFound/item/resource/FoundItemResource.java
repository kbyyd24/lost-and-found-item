package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

import java.util.List;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.PAGE_OUT_OF_BOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("/item/found")
@Produces(APPLICATION_JSON)
public class FoundItemResource {

  private FoundItemService foundItemService;
  private UserService userService;

  public FoundItemResource(FoundItemService foundItemService,
                           UserService userService) {
    this.foundItemService = foundItemService;
    this.userService = userService;
  }

  @GET
  public List<FoundItemPageItem> loadPage(@QueryParam("page") @DefaultValue("1") int page,
                                          @QueryParam("listSize") @DefaultValue("8") int size,
                                          @QueryParam("sort") @DefaultValue("create_time") String sortName) {
    ItemSort sort = ItemSort.getItemSortByColumnName(sortName);
    List<FoundItemPageItem> foundItemPageItems = foundItemService.loadPage(page, size, sort);
    if (foundItemPageItems.isEmpty()) {
      throw new NotFoundException(PAGE_OUT_OF_BOUND.getReason());
    }
    return foundItemPageItems;
  }

  @POST
  @Consumes(APPLICATION_JSON)
  public FoundItem createItem(@HeaderParam("username") String username,
                              @HeaderParam("user-token") String userToken,
                              FoundItemCreator creator) {
    UserState userState = userService.checkState(username, userToken);
    if (userState != UserState.ONLINE) {
      throw new UnauthorizedException(userState.name());
    }
    return foundItemService.create(creator, username);
  }
}
