package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemBelongChecker;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ClaimItemResourceTestForGetPage {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private ClaimItemService claimItemService;

  @MockBean
  private FoundItemBelongChecker belongChecker;

  @MockBean
  private AuthService authService;

  private long itemId;
  private String path;
  private String requestUser;
  private String userToken;
  private HttpEntity<Object> requestEntity;
  private int page;
  private int size;
  private ItemSort sort;

  @Before
  public void setUp() throws Exception {
    itemId = 123L;
    page = 2;
    size = 4;
    sort = ItemSort.CREATE_TIME;
    String query = String.format("?page=%d&size=%d&sort=%s", page, size, sort.getColumnName());
    path = String.format("/item/found/%s/claim%s", itemId, query);
    requestUser = "username";
    userToken = "user-token";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(requestUser, requestUser);
    httpHeaders.add(userToken, userToken);
    requestEntity = new HttpEntity<>(httpHeaders);
  }

  @Test
  public void should_response_200_when_loadPage_success() throws Exception {
    given(authService
        .checkUserRole(eq(itemId), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(claimItemService
        .loadPage(eq(itemId), Matchers.eq(page), Matchers.eq(size), eq(sort)))
        .willReturn(Collections.singletonList(new ClaimItemPageItem()));
    ResponseEntity<List> entity =
        restTemplate.exchange(path, GET, requestEntity, List.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    given(authService
        .checkUserRole(eq(itemId), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_not_super_resource_owner() throws Exception {
    given(authService
        .checkUserRole(eq(itemId), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.NOT_OWNER);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_404_when_foundItem_not_found() throws Exception {
    given(authService
        .checkUserRole(eq(itemId), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}