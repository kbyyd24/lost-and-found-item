package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.LostItemBelongChecker;
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
  private AuthService authService;

  @MockBean
  private LostItemBelongChecker belongChecker;

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
    given(authService.checkUserRole(eq(id), eq(username), eq(token), eq(belongChecker)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(lostItemService.update(eq(updater), eq(id), eq(username))).willReturn(lostItem);
    ResponseEntity<LostItem> entity =
        restTemplate.exchange(path, PUT, requestEntity, LostItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(lostItem));
  }

  @Test
  public void should_response_401_when_user_is_not_online() throws Exception {
    given(authService.checkUserRole(eq(id), eq(username), eq(token), eq(belongChecker)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_is_not_owner_of_item() throws Exception {
    given(authService.checkUserRole(eq(id), eq(username), eq(token), eq(belongChecker)))
        .willReturn(UserRole.NOT_OWNER);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_404_when_item_not_found() throws Exception {
    given(authService.checkUserRole(eq(id), eq(username), eq(token), eq(belongChecker)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}