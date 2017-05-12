package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
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
  private Map<String, ItemSort> sortMap;

  @Autowired
  public ReturnItemResource(ReturnItemService returnItemService,
                            LostItemService lostItemService,
                            UserService userService) {
    this.returnItemService = returnItemService;
    this.lostItemService = lostItemService;
    this.userService = userService;
    responseBuilder = new ResponseBuilder();
    sortMap = new HashMap<>(4);
    sortMap.put(ItemSort.END_TIME.getColumnName(), ItemSort.END_TIME);
    sortMap.put(ItemSort.BEGIN_TIME.getColumnName(), ItemSort.BEGIN_TIME);
    sortMap.put(ItemSort.CREATE_TIME.getColumnName(), ItemSort.CREATE_TIME);
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
    return responseBuilder
        .build(
            OK,
            returnItemService
                .getReturnItemPageItems(lostItemId, page, listSize, sortMap.get(sort)));
  }

}
