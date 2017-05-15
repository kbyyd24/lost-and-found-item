package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemServiceImpl;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.List;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.PAGE_OUT_OF_BOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("/item/found/{itemId}/returns")
@Produces(APPLICATION_JSON)
public class ClaimItemResource {

  private ClaimItemService claimItemService;
  private AuthService authService;
  private FoundItemServiceImpl belongChecker;

  public ClaimItemResource(ClaimItemService claimItemService,
                           AuthService authService,
                           FoundItemServiceImpl belongChecker) {
    this.claimItemService = claimItemService;
    this.authService = authService;
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

}
