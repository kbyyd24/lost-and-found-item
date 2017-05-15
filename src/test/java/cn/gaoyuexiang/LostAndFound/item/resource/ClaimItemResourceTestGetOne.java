package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemBelongChecker;
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
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ClaimItemResourceTestGetOne {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private AuthService authService;

  @MockBean
  private FoundItemBelongChecker belongChecker;

  @MockBean
  private ClaimItemService claimItemService;

  private String requestUser;
  private String userToken;
  private String resourceOwner;
  private long foundItemId;
  private HttpEntity<Object> requsetEntity;
  private String path;

  @Before
  public void setUp() throws Exception {
    requestUser = "username";
    userToken = "user-token";
    resourceOwner = "resourceOwner";
    foundItemId = 123L;
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(requestUser, requestUser);
    httpHeaders.add(userToken, userToken);
    requsetEntity = new HttpEntity<>(httpHeaders);
    path = String.format("/item/found/%d/claim/%s", foundItemId, resourceOwner);
  }

  @Test
  public void should_response_200_when_user_is_super_resource_owner_and_item_found() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    ClaimItem expectItem = new ClaimItem();
    given(claimItemService.loadOne(eq(foundItemId), eq(resourceOwner)))
        .willReturn(expectItem);
    ResponseEntity<ClaimItem> entity =
        restTemplate.exchange(path, GET, requsetEntity, ClaimItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void should_response_200_when_user_is_resource_owner_and_item_found() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    ClaimItem expectItem = new ClaimItem();
    given(claimItemService.loadOne(eq(foundItemId), eq(resourceOwner)))
        .willReturn(expectItem);
    ResponseEntity<ClaimItem> entity =
        restTemplate.exchange(path, GET, requsetEntity, ClaimItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requsetEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_not_owner() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.NOT_OWNER);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requsetEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_404_when_foundItem_not_found() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requsetEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void should_response_404_when_claimItem_not_found() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(claimItemService.loadOne(eq(foundItemId), eq(resourceOwner)))
        .willReturn(null);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requsetEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}