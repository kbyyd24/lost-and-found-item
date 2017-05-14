package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.exception.CloseItemException;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.LostItemServiceImpl;
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
import static org.springframework.http.HttpMethod.DELETE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class LostItemResourceTestForDelete {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private AuthService authService;

  @MockBean
  private LostItemServiceImpl lostItemServiceImpl;

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
    given(authService.checkUserRole(eq(id), eq(username), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(lostItemServiceImpl.close(eq(id))).willReturn(lostItem);
    ResponseEntity<LostItem> entity =
        restTemplate.exchange(path, DELETE, requestEntity, LostItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(lostItem));
  }

  @Test
  public void should_response_401_when_user_is_not_online() throws Exception {
    given(authService.checkUserRole(eq(id), eq(username), eq(token), eq(lostItemServiceImpl)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_not_item_owner() throws Exception {
    given(authService.checkUserRole(eq(id), eq(username), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.NOT_OWNER);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_403_when_throw_CloseItemException() throws Exception {
    given(authService.checkUserRole(eq(id), eq(username), eq(token), eq(lostItemServiceImpl)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(lostItemServiceImpl.close(eq(id))).willThrow(new CloseItemException(""));
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void should_response_404_when_item_not_found() throws Exception {
    given(authService.checkUserRole(eq(id), eq(username), eq(token), eq(lostItemServiceImpl)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, DELETE, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}