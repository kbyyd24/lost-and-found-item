package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.gaoyuexiang.LostAndFound.item.enums.ItemSort.*;
import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.LOST_ITEM_NOT_EXIST;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Component
@Path("/item/lost")
@Produces(APPLICATION_JSON)
public class LostItemResource {

  private LostItemService lostItemService;
  private UserService userService;
  private Map<String, ItemSort> sortPropMap;
  private ResponseBuilder responseBuilder;

  @Autowired
  public LostItemResource(LostItemService lostItemService,
                          UserService userService) {
    this.lostItemService = lostItemService;
    this.userService = userService;
    sortPropMap = new HashMap<>();
    sortPropMap.put("create_time", CREATE_TIME);
    sortPropMap.put("begin_time", BEGIN_TIME);
    sortPropMap.put("end_time", END_TIME);
    this.responseBuilder = new ResponseBuilder();
  }

  @GET
  public Response loadLostItemList(@QueryParam("page") @DefaultValue("1") int page,
                                   @QueryParam("listSize") @DefaultValue("8") int listSize,
                                   @QueryParam("sort") @DefaultValue("create_time") String sort) {
    List<LostItemPageItem> lostItemPageItems = lostItemService.loadPage(page, listSize, sortPropMap.get(sort));
    if (lostItemPageItems.size() > 0) {
      return responseBuilder.build(OK, lostItemPageItems);
    }
    Message entity = new Message(
        NotFoundReason.PAGE_OUT_OF_BOUND.getReason());
    return responseBuilder.build(NOT_FOUND, entity);
  }

  @POST
  @Consumes(APPLICATION_JSON)
  public Response createLostItem(@HeaderParam("username") String username,
                                 @HeaderParam("user-token") String token,
                                 LostItemCreator creator) {
    UserState userState = userService.checkState(username, token);
    if (userState != UserState.ONLINE) {
      return responseBuilder.build(UNAUTHORIZED, new Message(userState.name()));
    }
    LostItem lostItem = lostItemService.create(creator, username);
    return responseBuilder.build(OK, lostItem);
  }

  @GET
  @Path("{itemId}")
  public Response loadOneLostItem(@PathParam("itemId") long id) {
    LostItem lostItem = lostItemService.loadOne(id);
    return lostItem == null ?
        responseBuilder.build(NOT_FOUND,
            new Message(LOST_ITEM_NOT_EXIST.getReason())) :
        responseBuilder.build(OK, lostItem);
  }

}
