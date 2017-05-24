package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemBelongChecker;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemCloseChecker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.NotFoundException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ClaimItemResourceTestForDelete {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private ClaimItemService claimItemService;

  @MockBean
  private FoundItemBelongChecker belongChecker;

  @MockBean
  private FoundItemCloseChecker closeChecker;

  @MockBean
  private AuthService authService;

  private String resourceOwner;
  private String requestUser;
  private String userToken;
  private long foundItemId;
  private String path;
  private HttpEntity<Object> requestEntity;

  @Before
  public void setUp() throws Exception {
    foundItemId = 123L;
    resourceOwner = "resourceOwner";
    requestUser = "username";
    userToken = "user-token";
    path = String.format("/item/found/%d/claim/%s", foundItemId, resourceOwner);
  }

  private void buildRequestEntity(ActionType action) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(requestUser, requestUser);
    httpHeaders.add(userToken, userToken);
    httpHeaders.add("action-type", action.getValue());
    requestEntity = new HttpEntity<>(httpHeaders);
  }

  @Test
  public void should_response_200_when_user_is_super_resource_owner_and_action_is_accept() throws Exception {
    ActionType action = ActionType.ACCEPT;
    buildRequestEntity(action);
    UserRole userRole = UserRole.SUPER_RESOURCE_OWNER;
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(userRole);
    given(authService.checkAction(userRole, action)).willReturn(true);
    given(closeChecker.isClosed(foundItemId)).willReturn(false);
    ClaimItem claimItem = new ClaimItem();
    given(claimItemService.delete(eq(foundItemId), eq(resourceOwner), eq(action)))
        .willReturn(claimItem);
    ResponseEntity<ClaimItem> entity =
        restTemplate.exchange(path, DELETE, requestEntity, ClaimItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(claimItem));
  }

  @Test
  public void should_response_200_when_user_is_super_resource_owner_and_action_is_reject() throws Exception {
    ActionType action = ActionType.REJECT;
    buildRequestEntity(action);
    UserRole userRole = UserRole.SUPER_RESOURCE_OWNER;
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(userRole);
    given(authService.checkAction(userRole, action)).willReturn(true);
    given(closeChecker.isClosed(foundItemId)).willReturn(false);
    ClaimItem claimItem = new ClaimItem();
    given(claimItemService.delete(eq(foundItemId), eq(resourceOwner), eq(action)))
        .willReturn(claimItem);
    ResponseEntity<ClaimItem> entity =
        restTemplate.exchange(path, DELETE, requestEntity, ClaimItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(claimItem));
  }

  @Test
  public void should_response_200_when_user_is_resource_owner_and_action_is_cancel() throws Exception {
    ActionType action = ActionType.CANCEL;
    UserRole userRole = UserRole.RESOURCE_OWNER;
    buildRequestEntity(action);
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(userRole);
    given(authService.checkAction(userRole, action)).willReturn(true);
    given(closeChecker.isClosed(foundItemId)).willReturn(false);
    ClaimItem claimItem = new ClaimItem();
    given(claimItemService.delete(eq(foundItemId), eq(resourceOwner), eq(action)))
        .willReturn(claimItem);
    ResponseEntity<ClaimItem> entity =
        restTemplate.exchange(path, DELETE, requestEntity, ClaimItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(claimItem));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    buildRequestEntity(ActionType.ACCEPT);
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_not_owner() throws Exception {
    buildRequestEntity(ActionType.ACCEPT);
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.NOT_OWNER);
    given(authService.checkAction(UserRole.NOT_OWNER, ActionType.ACCEPT)).willReturn(false);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_super_resource_owner_and_action_is_cancel() throws Exception {
    ActionType action = ActionType.CANCEL;
    UserRole userRole = UserRole.SUPER_RESOURCE_OWNER;
    buildRequestEntity(action);
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(userRole);
    given(authService.checkAction(userRole, action)).willReturn(false);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_resource_owner_and_action_not_cancel() throws Exception {
    ActionType action = ActionType.ACCEPT;
    UserRole userRole = UserRole.RESOURCE_OWNER;
    buildRequestEntity(action);
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(userRole);
    given(authService.checkAction(userRole, action)).willReturn(false);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_403_when_foundItem_state_is_closed() throws Exception {
    ActionType action = ActionType.CANCEL;
    UserRole userRole = UserRole.SUPER_RESOURCE_OWNER;
    buildRequestEntity(action);
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(userRole);
    given(authService.checkAction(userRole, action)).willReturn(true);
    given(closeChecker.isClosed(foundItemId)).willReturn(true);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void should_response_403_when_claimItem_state_not_unread() throws Exception {
    ActionType action = ActionType.CANCEL;
    UserRole userRole = UserRole.RESOURCE_OWNER;
    buildRequestEntity(action);
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(userRole);
    given(authService.checkAction(userRole, action)).willReturn(true);
    given(closeChecker.isClosed(foundItemId)).willReturn(false);
    given(claimItemService.delete(eq(foundItemId), eq(resourceOwner), eq(action)))
        .willThrow(new UpdateItemException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void should_response_404_when_foundItem_not_found() throws Exception {
    buildRequestEntity(ActionType.ACCEPT);
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void should_response_404_when_claimItem_not_found() throws Exception {
    ActionType action = ActionType.CANCEL;
    UserRole userRole = UserRole.RESOURCE_OWNER;
    buildRequestEntity(action);
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(userRole);
    given(authService.checkAction(userRole, action)).willReturn(true);
    given(closeChecker.isClosed(foundItemId)).willReturn(false);
    given(claimItemService.delete(eq(foundItemId), eq(resourceOwner), eq(action)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}