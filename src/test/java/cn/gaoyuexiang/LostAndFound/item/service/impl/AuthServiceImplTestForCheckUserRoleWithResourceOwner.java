package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceImplTestForCheckUserRoleWithResourceOwner {

  @Autowired
  private AuthService authService;

  @MockBean
  private LostItemService lostItemService;

  @MockBean
  private UserService userService;

  @Test
  public void should_return_resource_owner_when_request_user_is_resource_owner() throws Exception {
    String username = "username";
    String token = "token";
    long superResourceId = 123L;
    when(userService.checkState(username, token)).thenReturn(UserState.ONLINE);
    when(lostItemService.isBelong(superResourceId, username)).thenReturn(false);
    UserRole userRole = authService.checkUserRole(superResourceId, username, username, token);
    assertThat(userRole, is(UserRole.RESOURCE_OWNER));
    verify(userService).checkState(username, token);
    verify(lostItemService).isBelong(superResourceId, username);
  }

  @Test
  public void should_return_super_resource_owner_when_request_user_is_super_resource_owner() throws Exception {
    String requestUser = "requestUser";
    String resourceOwner = "resourceOwner";
    long superResourceId = 123L;
    String token = "token";
    when(userService.checkState(requestUser, token)).thenReturn(UserState.ONLINE);
    when(lostItemService.isBelong(superResourceId, requestUser)).thenReturn(true);
    UserRole userRole = authService.checkUserRole(superResourceId, resourceOwner, requestUser, token);
    assertThat(userRole, is(UserRole.SUPER_RESOURCE_OWNER));
    verify(userService).checkState(requestUser, token);
    verify(lostItemService).isBelong(superResourceId, requestUser);
  }

  @Test
  public void should_return_not_owner_when_request_user_not_super_resource_owner_and_resource_owner() throws Exception {
    String requestUser = "requestUser";
    String resourceOwner = "resourceOwner";
    long superResourceId = 123L;
    String token = "token";
    when(userService.checkState(requestUser, token)).thenReturn(UserState.ONLINE);
    when(lostItemService.isBelong(superResourceId, requestUser)).thenReturn(false);
    UserRole userRole = authService.checkUserRole(superResourceId, resourceOwner, requestUser, token);
    assertThat(userRole, is(UserRole.NOT_OWNER));
    verify(userService).checkState(requestUser, token);
    verify(lostItemService).isBelong(superResourceId, requestUser);
  }

  @Test(expected = UnauthorizedException.class)
  public void should_throw_UnauthorizedException_when_user_not_online() throws Exception {
    String requestUser = "requestUser";
    String resourceOwner = "resourceOwner";
    long superResourceId = 123L;
    String token = "token";
    when(userService.checkState(requestUser, token)).thenReturn(UserState.OFFLINE);
    authService.checkUserRole(superResourceId, resourceOwner, requestUser, token);
    verify(userService).checkState(requestUser, token);
  }
}