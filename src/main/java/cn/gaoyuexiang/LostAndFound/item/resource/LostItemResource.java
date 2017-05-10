package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.gaoyuexiang.LostAndFound.item.enums.ItemSort.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("/item/lost")
public class LostItemResource {

  private LostItemService lostItemService;
  private Map<String, ItemSort> sortPropMap;

  @Autowired
  public LostItemResource(LostItemService lostItemService) {
    this.lostItemService = lostItemService;
    sortPropMap = new HashMap<>();
    sortPropMap.put("create_time", CREATE_TIME);
    sortPropMap.put("begin_time", BEGIN_TIME);
    sortPropMap.put("end_time", END_TIME);
  }

  @GET
  @Produces(APPLICATION_JSON)
  public Response loadListItemList(@QueryParam("page") @DefaultValue("1") int page,
                                   @QueryParam("listSize") @DefaultValue("8") int listSize,
                                   @QueryParam("sort") @DefaultValue("create_time") String sort) {
    List<LostItemPageItem> lostItemPageItems = lostItemService.loadPage(page, listSize, sortPropMap.get(sort));
    if (lostItemPageItems.size() > 0) {
      return Response.status(Status.OK)
          .entity(lostItemPageItems)
          .build();
    }
    return Response
        .status(Status.NOT_FOUND)
        .entity(
            new Message(
                NotFoundReason.PAGE_OUT_OF_BOUND.getReason()))
        .build();
  }

}
