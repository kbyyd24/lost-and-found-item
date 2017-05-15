package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import cn.gaoyuexiang.LostAndFound.item.service.BelongChecker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceImplTestForCheckUserRoleWithoutResourceOwner {

  @Autowired
  private AuthService authService;

  @MockBean
  private UserService userService;

  @Mock
  private BelongChecker belongChecker;

  private long itemId;
  private String requestUser;
  private String token;

  @Before
  public void setUp() throws Exception {
    itemId = 123L;
    requestUser = "requestUser";
    token = "token";
  }

  @Test
  public void should_return_super_resource_owner_when_user_is_item_owner() throws Exception {
    when(userService.checkState(requestUser, token)).thenReturn(UserState.ONLINE);
    when(belongChecker.isBelong(itemId, requestUser)).thenReturn(true);
    UserRole userRole = authService.checkUserRole(itemId, requestUser, token, belongChecker);
    assertThat(userRole, is(UserRole.SUPER_RESOURCE_OWNER));
    verify(userService).checkState(requestUser, token);
    verify(belongChecker).isBelong(itemId, requestUser);
  }

  @Test
  public void should_return_not_owner_when_user_not_item_owner() throws Exception {
    when(userService.checkState(requestUser, token)).thenReturn(UserState.ONLINE);
    when(belongChecker.isBelong(itemId, requestUser)).thenReturn(false);
    UserRole userRole = authService.checkUserRole(itemId, requestUser, token, belongChecker);
    assertThat(userRole, is(UserRole.NOT_OWNER));
    verify(userService).checkState(requestUser, token);
    verify(belongChecker).isBelong(itemId, requestUser);
  }

  @Test(expected = UnauthorizedException.class)
  public void should_throw_UnauthorizedException_when_user_not_online() throws Exception {
    when(userService.checkState(requestUser, token)).thenReturn(UserState.OFFLINE);
    authService.checkUserRole(itemId, requestUser, token, belongChecker);
    verify(userService).checkState(requestUser, token);
    verify(belongChecker, times(0)).isBelong(itemId, requestUser);
  }
}