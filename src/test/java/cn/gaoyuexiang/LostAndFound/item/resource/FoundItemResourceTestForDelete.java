package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.CloseItemException;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemServiceImpl;
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
public class FoundItemResourceTestForDelete {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private AuthService authService;

  @MockBean
  private FoundItemServiceImpl foundItemServiceImpl;

  private String username;
  private String userToken;
  private long itemId;
  private HttpEntity<Object> requestEntity;
  private String path;

  @Before
  public void setUp() throws Exception {
    username = "username";
    userToken = "user-token";
    itemId = 123L;
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add(userToken, userToken);
    requestEntity = new HttpEntity<>(httpHeaders);
    path = String.format("/item/found/%d", itemId);
  }

  @Test
  public void should_response_200_when_close_success() throws Exception {
    FoundItem foundItem = new FoundItem();
    given(
        authService.checkUserRole(
            eq(itemId), eq(username), eq(userToken), eq(foundItemServiceImpl)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(foundItemServiceImpl.close(eq(itemId))).willReturn(foundItem);
    ResponseEntity<FoundItem> entity =
        restTemplate.exchange(path, DELETE, requestEntity, FoundItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(foundItem));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    given(
        authService.checkUserRole(
            eq(itemId), eq(username), eq(userToken), eq(foundItemServiceImpl)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_not_item_owner() throws Exception {
    given(
        authService.checkUserRole(
            eq(itemId), eq(username), eq(userToken), eq(foundItemServiceImpl)))
        .willReturn(UserRole.NOT_OWNER);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_403_when_foundItem_has_unread_claimItem() throws Exception {
    given(
        authService
            .checkUserRole(eq(itemId), eq(username), eq(userToken), eq(foundItemServiceImpl)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(foundItemServiceImpl.close(eq(itemId))).willThrow(new CloseItemException(""));
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void should_response_404_when_item_not_found() throws Exception {
    given(
        authService
            .checkUserRole(eq(itemId), eq(username), eq(userToken), eq(foundItemServiceImpl)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}