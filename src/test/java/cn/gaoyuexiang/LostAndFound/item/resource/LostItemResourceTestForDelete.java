package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.CloseItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class LostItemResourceTestForDelete {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private UserService userService;

  @MockBean
  private LostItemService lostItemService;

  private String username;
  private String token;
  private long id;
  private HttpEntity<?> requestEntity;
  private String path;

  @Before
  public void setUp() throws Exception {
    username = "username";
    token = "user-token";
    id = 123L;
    path = "/item/lost/" + id;
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add(token, token);
    requestEntity = new HttpEntity<>(httpHeaders);
  }

  @Test
  public void should_response_200_when_close_success() throws Exception {
    LostItem lostItem = new LostItem();
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.ONLINE);
    given(lostItemService.close(eq(id))).willReturn(lostItem);
    ResponseEntity<LostItem> entity =
        restTemplate.exchange(path, DELETE, requestEntity, LostItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(lostItem));
  }

  @Test
  public void should_response_401_when_user_is_not_online() throws Exception {
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.OFFLINE);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_403_when_throw_CloseItemException() throws Exception {
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.ONLINE);
    given(lostItemService.close(eq(id))).willThrow(new CloseItemException(""));
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void should_response_404_when_item_not_found() throws Exception {
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.ONLINE);
    given(lostItemService.close(eq(id))).willReturn(null);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}