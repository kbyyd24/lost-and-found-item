package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemCreator;
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
public class ClaimItemResourceTestForPUT {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private ClaimItemService claimItemService;

  @MockBean
  private AuthService authService;

  @MockBean
  private FoundItemBelongChecker belongChecker;

  @MockBean
  private FoundItemCloseChecker closeChecker;

  private String resourceOwner;
  private String requestUser;
  private String userToken;
  private long foundItemId;
  private ClaimItemCreator creator;
  private HttpEntity<ClaimItemCreator> requestEntity;
  private String path;

  @Before
  public void setUp() throws Exception {
    resourceOwner = "resourceOwner";
    requestUser = "username";
    userToken = "user-token";
    foundItemId = 123L;
    path = String.format("/item/found/%d/claim/%s", foundItemId, resourceOwner);
    creator = new ClaimItemCreator();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(requestUser, requestUser);
    httpHeaders.add(userToken, userToken);
    requestEntity = new HttpEntity<>(creator, httpHeaders);
  }

  @Test
  public void should_response_200_when_put_success() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(closeChecker.isClosed(eq(foundItemId))).willReturn(false);
    ClaimItem claimItem = new ClaimItem();
    given(claimItemService.create(eq(foundItemId), eq(resourceOwner), eq(creator)))
        .willReturn(claimItem);
    ResponseEntity<ClaimItem> entity =
        restTemplate.exchange(path, PUT, requestEntity, ClaimItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(claimItem));
  }

  @Test
  public void should_response_400_when_creator_miss_property() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(closeChecker.isClosed(eq(foundItemId))).willReturn(false);
    given(claimItemService.create(eq(foundItemId), eq(resourceOwner), eq(creator)))
        .willThrow(new MissPropertyException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_not_resource_owner() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_403_when_foundItem_closed() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(closeChecker.isClosed(eq(foundItemId))).willReturn(true);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void should_response_403_when_claimItem_closed() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(closeChecker.isClosed(eq(foundItemId))).willReturn(false);
    given(claimItemService.create(eq(foundItemId), eq(resourceOwner), eq(creator)))
        .willThrow(new UpdateItemException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void should_response_404_when_foundItem_not_found() throws Exception {
    given(authService.checkUserRole(
        eq(foundItemId), eq(resourceOwner), eq(requestUser), eq(userToken), eq(belongChecker)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}