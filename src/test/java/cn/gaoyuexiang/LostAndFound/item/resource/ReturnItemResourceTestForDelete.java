package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ReturnItemResourceTestForDelete {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private AuthService authService;

  @MockBean
  private ReturnItemService returnItemService;

  @MockBean
  private LostItemServiceImpl lostItemServiceImpl;

  private String resourceOwner;
  private String requestUser;
  private long superResourceId;
  private String token;
  private HttpEntity<Object> requestEntity;
  private String path;

  @Before
  public void setUp() throws Exception {
    resourceOwner = "resourceOwner";
    requestUser = "requestUser";
    superResourceId = 123L;
    token = "user-token";
    path = String.format("/item/lost/%d/returns/%s", superResourceId, resourceOwner);
    String action = "accept";
    buildEntity(requestUser, action);
  }

  @Test
  public void should_response_200_when_user_is_super_resource_owner_and_action_is_accept() throws Exception {
    ReturnItem returnItem = new ReturnItem();
    given(
        authService.checkUserRole(eq(superResourceId), eq(resourceOwner),
            eq(requestUser), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(authService.checkAction(UserRole.SUPER_RESOURCE_OWNER, ActionType.ACCEPT))
        .willReturn(true);
    given(lostItemServiceImpl.isClosed(eq(superResourceId))).willReturn(false);
    given(returnItemService.delete(eq(resourceOwner), eq(superResourceId), eq(ActionType.ACCEPT)))
        .willReturn(returnItem);
    ResponseEntity<ReturnItem> entity = getReturnItemResponseEntity();
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(returnItem));
  }

  @Test
  public void should_response_200_when_user_is_super_resource_owner_and_action_is_reject() throws Exception {
    buildEntity(requestUser, "reject");
    ReturnItem returnItem = new ReturnItem();
    given(
        authService.checkUserRole(eq(superResourceId), eq(resourceOwner),
            eq(requestUser), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(authService.checkAction(UserRole.SUPER_RESOURCE_OWNER, ActionType.REJECT))
        .willReturn(true);
    given(lostItemServiceImpl.isClosed(eq(superResourceId))).willReturn(false);
    given(returnItemService.delete(eq(resourceOwner), eq(superResourceId), eq(ActionType.REJECT)))
        .willReturn(returnItem);
    ResponseEntity<ReturnItem> entity = getReturnItemResponseEntity();
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(returnItem));
  }

  @Test
  public void should_response_200_when_user_is_resource_owner_and_action_is_cancel() throws Exception {
    buildEntity(resourceOwner, "cancel");
    ReturnItem returnItem = new ReturnItem();
    given(
        authService.checkUserRole(eq(superResourceId), eq(resourceOwner),
            eq(resourceOwner), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(authService.checkAction(UserRole.RESOURCE_OWNER, ActionType.CANCEL))
        .willReturn(true);
    given(lostItemServiceImpl.isClosed(eq(superResourceId))).willReturn(false);
    given(returnItemService.delete(eq(resourceOwner), eq(superResourceId), eq(ActionType.CANCEL)))
        .willReturn(returnItem);
    ResponseEntity<ReturnItem> entity = getReturnItemResponseEntity();
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(returnItem));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    given(
        authService.checkUserRole(eq(superResourceId), eq(resourceOwner),
            eq(requestUser), eq(token), eq(lostItemServiceImpl)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity = getMessageResponseEntity();
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_not_owner() throws Exception {
    given(
        authService.checkUserRole(eq(superResourceId), eq(resourceOwner),
            eq(requestUser), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.NOT_OWNER);
    given(authService.checkAction(UserRole.NOT_OWNER, ActionType.ACCEPT))
        .willReturn(false);
    ResponseEntity<Message> entity = getMessageResponseEntity();
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_super_resource_owner_and_action_is_not_accept_or_reject() throws Exception {
    buildEntity(requestUser, "cancel");
    given(
        authService.checkUserRole(eq(superResourceId), eq(resourceOwner),
            eq(requestUser), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(authService.checkAction(UserRole.SUPER_RESOURCE_OWNER, ActionType.CANCEL))
        .willReturn(false);
    ResponseEntity<Message> entity = getMessageResponseEntity();
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_resource_owner_and_action_not_cancel() throws Exception {
    buildEntity(resourceOwner, "accept");
    given(
        authService.checkUserRole(eq(superResourceId), eq(resourceOwner),
            eq(resourceOwner), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(authService.checkAction(UserRole.RESOURCE_OWNER, ActionType.ACCEPT))
        .willReturn(false);
    ResponseEntity<Message> entity = getMessageResponseEntity();
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_403_when_user_has_auth_but_lostItem_state_is_closed() throws Exception {
    given(
        authService.checkUserRole(eq(superResourceId), eq(resourceOwner),
            eq(requestUser), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(authService.checkAction(UserRole.SUPER_RESOURCE_OWNER, ActionType.ACCEPT))
        .willReturn(true);
    given(lostItemServiceImpl.isClosed(eq(superResourceId)))
        .willReturn(true);
    ResponseEntity<Message> entity = getMessageResponseEntity();
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  private void buildEntity(String requestUser, String action) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("username", requestUser);
    httpHeaders.add(token, token);
    httpHeaders.add("action-type", action);
    requestEntity = new HttpEntity<>(httpHeaders);
  }

  private ResponseEntity<ReturnItem> getReturnItemResponseEntity() {
    return restTemplate.exchange(path, DELETE, requestEntity, ReturnItem.class);
  }

  private ResponseEntity<Message> getMessageResponseEntity() {
    return restTemplate.exchange(path, DELETE, requestEntity, Message.class);
  }
}