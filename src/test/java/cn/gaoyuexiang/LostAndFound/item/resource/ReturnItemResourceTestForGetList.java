package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class ReturnItemResourceTestForGetList {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private ReturnItemService returnItemService;

  @MockBean
  private AuthService authService;

  private String username;
  private String token;
  private long lostItemId;
  private HttpEntity<?> requestEntity;
  private String path;
  private int page;
  private int size;
  private ItemSort sort;
  private String query;

  @Before
  public void setUp() throws Exception {
    username = "username";
    token = "user-token";
    lostItemId = 123L;
    path = String.format("/item/lost/%d/returns", lostItemId);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add(token, token);
    requestEntity = new HttpEntity<>(httpHeaders);
    page = 1;
    size = 8;
    sort = ItemSort.CREATE_TIME;
    query = String.format("?page=%d&listSize=%d&sort=%s", page, size, sort.getColumnName());
  }

  @Test
  public void should_response_200_when_user_is_online_and_has_the_item() throws Exception {
    ReturnItemPageItem pageItem = new ReturnItemPageItem();
    List<ReturnItemPageItem> pageItems = Collections.singletonList(pageItem);
    given(authService.checkUserRole(eq(lostItemId), eq(username), eq(token)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(returnItemService.getReturnItemPageItems(eq(lostItemId), eq(page), eq(size), eq(sort)))
        .willReturn(pageItems);
    ResponseEntity<List> entity =
        restTemplate.exchange(path + query, GET, requestEntity, List.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void should_response_401_when_user_is_not_online() throws Exception {
    given(authService.checkUserRole(eq(lostItemId), eq(username), eq(token)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_online_but_item_owner() throws Exception {
    given(authService.checkUserRole(eq(lostItemId), eq(username), eq(token)))
        .willReturn(UserRole.NOT_OWNER);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_404_when_item_not_found() throws Exception {
    given(authService.checkUserRole(eq(lostItemId),eq(username),eq(token)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}