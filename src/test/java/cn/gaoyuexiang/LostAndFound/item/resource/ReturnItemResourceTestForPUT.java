package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.LostItemBelongChecker;
import cn.gaoyuexiang.LostAndFound.item.service.impl.LostItemServiceImpl;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.PUT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ReturnItemResourceTestForPUT {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private ReturnItemService returnItemService;

  @MockBean
  private LostItemServiceImpl lostItemServiceImpl;

  @MockBean
  private AuthService authService;

  @MockBean
  private LostItemBelongChecker belongChecker;

  private ReturnItemCreator creator;
  private String username;
  private String token;
  private long lostItemId;
  private HttpEntity<ReturnItemCreator> requestEntity;
  private String path;

  @Before
  public void setUp() throws Exception {
    creator = new ReturnItemCreator("reason", "contact");
    username = "username";
    token = "user-token";
    lostItemId = 123L;
    path = String.format("/item/lost/%d/returns/%s", lostItemId, username);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add(token, token);
    requestEntity = new HttpEntity<>(creator, httpHeaders);
  }

  @Test
  public void should_response_200_when_service_return_an_item() throws Exception {
    ReturnItem returnItem = new ReturnItem();
    given(authService.checkUserRole(eq(lostItemId), eq(username),
        eq(username), eq(token), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(lostItemServiceImpl.isClosed(lostItemId)).willReturn(false);
    given(returnItemService.create(eq(username), eq(lostItemId), eq(creator)))
        .willReturn(returnItem);
    ResponseEntity<ReturnItem> entity =
        restTemplate.exchange(path, PUT, requestEntity, ReturnItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(returnItem));
  }

  @Test
  public void should_response_401_when_requestUser_is_lostItem_owner() throws Exception {
    given(authService.checkUserRole(eq(lostItemId), eq(username),
        eq(username), eq(token), eq(belongChecker)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    ResponseEntity<Message> entity = putRequest();
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    given(authService.checkUserRole(eq(lostItemId), eq(username),
        eq(username), eq(token), eq(belongChecker)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity = putRequest();
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_requestUser_is_not_resourceOwner() throws Exception {
    String resourceOwner = "resourceOwner";
    path = path.replace(username, resourceOwner);
    given(authService.checkUserRole(eq(lostItemId), eq(resourceOwner),
        eq(username), eq(token), eq(belongChecker)))
        .willReturn(UserRole.NOT_OWNER);
    ResponseEntity<Message> entity = putRequest();
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_403_when_lostItem_state_is_closed() throws Exception {
    given(authService.checkUserRole(eq(lostItemId), eq(username),
        eq(username), eq(token), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(lostItemServiceImpl.isClosed(eq(lostItemId))).willReturn(true);
    ResponseEntity<Message> entity = putRequest();
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void should_response_403_when_item_state_is_closed() throws Exception {
    given(authService.checkUserRole(eq(lostItemId), eq(username),
        eq(username), eq(token), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(returnItemService.create(eq(username), eq(lostItemId), eq(creator)))
        .willThrow(new UpdateItemException());
    ResponseEntity<Message> entity = putRequest();
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void should_response_404_when_lostItem_not_exist() throws Exception {
    given(authService.checkUserRole(eq(lostItemId), eq(username),
        eq(username), eq(token), eq(belongChecker)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity = putRequest();
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  private ResponseEntity<Message> putRequest() {
    return restTemplate.exchange(path, PUT, requestEntity, Message.class);
  }
}