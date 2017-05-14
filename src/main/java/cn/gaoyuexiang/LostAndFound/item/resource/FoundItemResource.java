package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

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
