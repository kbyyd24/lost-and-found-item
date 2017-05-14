package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.LostItemServiceImpl;
import cn.gaoyuexiang.LostAndFound.item.service.interfaces.BelongChecker;
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
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ReturnItemResourceTestForGetReturnItem {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private ReturnItemService returnItemService;

  @MockBean
  private AuthService authService;

  @MockBean
  private LostItemServiceImpl belongChecker;

  private String lostItemOwner;
  private String returnItemOwner;
  private String token;
  private long lostItemId;
  private HttpEntity<Object> requestEntity;
  private String path;

  @Before
  public void setUp() throws Exception {
    lostItemOwner = "lostItemOwner";
    returnItemOwner = "returnItemOwner";
    token = "user-token";
    lostItemId = 123L;
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("username", lostItemOwner);
    httpHeaders.add(token, token);
    requestEntity = new HttpEntity<>(httpHeaders);
    path = String.format("/item/lost/%d/returns/%s", lostItemId, returnItemOwner);
  }

  @Test
  public void should_response_200_when_user_is_lostItem_owner() throws Exception {
    ReturnItem returnItem = new ReturnItem();
    given(
        authService.checkUserRole(eq(lostItemId), eq(returnItemOwner),
            eq(lostItemOwner), eq(token), eq(belongChecker)))
        .willReturn(UserRole.SUPER_RESOURCE_OWNER);
    given(returnItemService.getReturnItem(eq(returnItemOwner), eq(lostItemId)))
        .willReturn(returnItem);
    ResponseEntity<ReturnItem> entity =
        restTemplate.exchange(path, GET, requestEntity, ReturnItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void should_response_200_when_user_is_returnItem_owner() throws Exception {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("username", returnItemOwner);
    httpHeaders.add(token, token);
    requestEntity = new HttpEntity<>(httpHeaders);
    ReturnItem returnItem = new ReturnItem();
    given(
        authService.checkUserRole(eq(lostItemId), eq(returnItemOwner),
            eq(returnItemOwner), eq(token), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(returnItemService.getReturnItem(eq(returnItemOwner), eq(lostItemId)))
        .willReturn(returnItem);
    ResponseEntity<ReturnItem> entity =
        restTemplate.exchange(path, GET, requestEntity, ReturnItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void should_response_401_when_user_not_online() throws Exception {
    given(
        authService.checkUserRole(eq(lostItemId), eq(returnItemOwner),
            eq(lostItemOwner), eq(token), eq(belongChecker)))
        .willThrow(new UnauthorizedException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_401_when_user_not_lostItem_owner_and_returnItem_owner() throws Exception {
    given(
        authService.checkUserRole(eq(lostItemId), eq(returnItemOwner),
            eq(lostItemOwner), eq(token), eq(belongChecker)))
        .willReturn(UserRole.NOT_OWNER);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_response_404_when_lostItem_not_found() throws Exception {
    given(
        authService.checkUserRole(eq(lostItemId), eq(returnItemOwner),
            eq(lostItemOwner), eq(token), eq(belongChecker)))
        .willThrow(new NotFoundException());
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void should_response_404_when_returnItem_not_found() throws Exception {
    given(
        authService.checkUserRole(eq(lostItemId), eq(returnItemOwner),
            eq(lostItemOwner), eq(token), eq(belongChecker)))
        .willReturn(UserRole.RESOURCE_OWNER);
    given(returnItemService.getReturnItem(eq(returnItemOwner), eq(lostItemId)))
        .willReturn(null);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}