package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
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

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class LostItemResourceTestForCreateLostItem {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private UserService userService;

  @MockBean
  private LostItemService lostItemService;

  private String username;
  private String token;
  private HttpEntity<LostItemCreator> requestEntity;

  @Before
  public void setUp() throws Exception {
    username = "username";
    token = "token";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add("user-token", token);
    LostItemCreator lostItemCreator = new LostItemCreator();
    requestEntity = new HttpEntity<>(lostItemCreator, httpHeaders);
  }

  @Test
  public void should_response_200_when_create_success() throws Exception {
    LostItem lostItem = new LostItem();
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.ONLINE);
    given(lostItemService.create(any(LostItemCreator.class), eq(username))).willReturn(lostItem);
    ResponseEntity<LostItem> entity =
        restTemplate.exchange("/item/lost", POST, requestEntity, LostItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(lostItem));
  }

  @Test
  public void should_response_400_when_body_miss_some_properties() throws Exception {
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.ONLINE);
    given(lostItemService.create(any(LostItemCreator.class), eq(username)))
        .willThrow(new MissPropertyException());
    ResponseEntity<Message> entity =
        restTemplate.exchange("/item/lost", POST, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(BAD_REQUEST));
  }

  @Test
  public void should_response_401_when_user_is_not_online() throws Exception {
    given(userService.checkState(eq(username), eq(token))).willReturn(UserState.OFFLINE);
    ResponseEntity<Message> entity =
        restTemplate.exchange("/item/lost", POST, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(UNAUTHORIZED));
  }
}