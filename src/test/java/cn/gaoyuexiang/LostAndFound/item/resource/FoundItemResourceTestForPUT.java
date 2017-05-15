package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemBelongChecker;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemServiceImpl;
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
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.PUT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FoundItemResourceTestForPUT {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private AuthService authService;

  @MockBean
  private FoundItemService foundItemServiceImpl;

  @MockBean
  private FoundItemBelongChecker belongChecker;

  private String username;
  private String userToken;
  private long itemId;
  private String path;
  private HttpEntity<FoundItemCreator> requestEntity;
  private FoundItemCreator updater;

  @Before
  public void setUp() throws Exception {
    username = "username";
    userToken = "user-token";
    itemId = 132L;
    path = String.format("/item/found/%d", itemId);
    updater = new FoundItemCreator();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add(userToken, userToken);
    requestEntity = new HttpEntity<>(updater, httpHeaders);
  }

  @Test
  public void should_response_200_when_update_success() throws Exception {
    FoundItem foundItem = new FoundItem();
    given(authService.checkUserRole(
        eq(itemId), eq(username), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(foundItemServiceImpl.update(eq(updater), eq(itemId), eq(username)))
        .willReturn(foundItem);
    ResponseEntity<FoundItem> entity =
        restTemplate.exchange(path, PUT, requestEntity, FoundItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(foundItem));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    given(authService.checkUserRole(
        eq(itemId), eq(username), eq(userToken), eq(belongChecker)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_not_item_owner() throws Exception {
    given(authService.checkUserRole(
        eq(itemId), eq(username), eq(userToken), eq(belongChecker)))
        .willReturn(UserRole.NOT_OWNER);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_404_when_foundItem_not_found() throws Exception {
    given(authService.checkUserRole(
        eq(itemId), eq(username), eq(userToken), eq(belongChecker)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}