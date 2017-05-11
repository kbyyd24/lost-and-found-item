package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.PUT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class LostItemResourceTestForUpdateLostItem {
  
  @Autowired
  private TestRestTemplate restTemplate;
  
  @MockBean
  private LostItemService lostItemService;
  
  @MockBean
  private UserService userService;

  private String username;
  private String token;
  private LostItemCreator updater;
  private long id;
  private String path;
  private HttpEntity<LostItemCreator> requestEntity;

  @Before
  public void setUp() throws Exception {
    username = "username";
    token = "token";
    id = 123L;
    path = "/item/lost/" + id;
    updater = new LostItemCreator();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add("user-token", token);
    requestEntity = new HttpEntity<>(updater, httpHeaders);
  }

  @Test
  public void should_response_200_when_update_success() throws Exception {
    LostItem lostItem = new LostItem();
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.ONLINE);
    given(lostItemService.update(eq(updater), eq(id), eq(username))).willReturn(lostItem);
    ResponseEntity<LostItem> entity =
        restTemplate.exchange(path, PUT, requestEntity, LostItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(lostItem));
  }

  @Test
  public void should_response_401_when_user_is_not_online() throws Exception {
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.OFFLINE);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_not_owner_of_item() throws Exception {
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.ONLINE);
    given(lostItemService.update(eq(updater), eq(id), eq(username)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_404_when_item_not_found() throws Exception {
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.ONLINE);
    given(lostItemService.update(eq(updater), eq(id), eq(username))).willReturn(null);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}