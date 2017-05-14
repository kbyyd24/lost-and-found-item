package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FoundItemResourceTestForPOST {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private FoundItemServiceImpl foundItemService;

  @MockBean
  private UserService userService;

  private String username;
  private String userToken;
  private FoundItemCreator creator;
  private HttpEntity<FoundItemCreator> requestEntity;
  private String path;

  @Before
  public void setUp() throws Exception {
    username = "username";
    userToken = "user-token";
    creator = new FoundItemCreator();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add(userToken, userToken);
    requestEntity = new HttpEntity<>(creator, httpHeaders);
    path = "/item/found";
  }

  @Test
  public void should_response_200_when_create_success() throws Exception {
    FoundItem expectItem = new FoundItem();
    given(userService.checkState(eq(username), eq(userToken)))
        .willReturn(UserState.ONLINE);
    given(foundItemService.create(eq(creator), eq(username)))
        .willReturn(expectItem);
    ResponseEntity<FoundItem> entity =
        restTemplate.exchange(path, POST, requestEntity, FoundItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(expectItem));
  }

  @Test
  public void should_response_400_when_creator_miss_property() throws Exception {
    given(userService.checkState(eq(username),eq(userToken)))
        .willReturn(UserState.ONLINE);
    given(foundItemService.create(eq(creator),eq(username)))
        .willThrow(new MissPropertyException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, POST, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    given(userService.checkState(eq(username), eq(userToken)))
        .willReturn(UserState.OFFLINE);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, POST, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }
}