package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import org.apache.http.auth.AUTH;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceImplTestForCheckAction {

  @Autowired
  private AuthService authService;

  @Test
  public void should_return_true_when_user_is_super_resource_owner_and_action_is_accept_or_reject() throws Exception {
    assertTrue(authService.checkAction(UserRole.SUPER_RESOURCE_OWNER, ActionType.ACCEPT));
    assertTrue(authService.checkAction(UserRole.SUPER_RESOURCE_OWNER, ActionType.REJECT));
  }

  @Test
  public void should_return_true_when_user_is_resource_owner_and_action_is_cancel() throws Exception {
    assertTrue(authService.checkAction(UserRole.RESOURCE_OWNER, ActionType.CANCEL));
  }

  @Test
  public void should_return_false_when_user_is_super_resource_owner_and_action_is_cancel() throws Exception {
    assertFalse(authService.checkAction(UserRole.SUPER_RESOURCE_OWNER, ActionType.CANCEL));
  }

  @Test
  public void should_return_false_when_user_is_resource_owner_and_action_is_accept_or_reject() throws Exception {
    assertFalse(authService.checkAction(UserRole.RESOURCE_OWNER, ActionType.ACCEPT));
    assertFalse(authService.checkAction(UserRole.RESOURCE_OWNER, ActionType.REJECT));
  }

  @Test
  public void should_return_false_when_user_is_not_owner() throws Exception {
   assertFalse(authService.checkAction(UserRole.NOT_OWNER, ActionType.ACCEPT));
   assertFalse(authService.checkAction(UserRole.NOT_OWNER, ActionType.REJECT));
   assertFalse(authService.checkAction(UserRole.NOT_OWNER, ActionType.CANCEL));
  }
}