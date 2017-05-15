package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import cn.gaoyuexiang.LostAndFound.item.service.BelongChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static cn.gaoyuexiang.LostAndFound.item.enums.ActionType.*;
import static cn.gaoyuexiang.LostAndFound.item.enums.UserRole.NOT_OWNER;
import static cn.gaoyuexiang.LostAndFound.item.enums.UserRole.RESOURCE_OWNER;
import static cn.gaoyuexiang.LostAndFound.item.enums.UserRole.SUPER_RESOURCE_OWNER;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserService userService;

  private Map<UserRole, Predicate<ActionType>> roleActionMap;

  @Autowired
  public AuthServiceImpl(UserService userService) {
    this.userService = userService;
    this.roleActionMap = buildRoleActionMap();
  }

  private Map<UserRole, Predicate<ActionType>> buildRoleActionMap() {
    HashMap<UserRole, Predicate<ActionType>> roleActonMap = new HashMap<>();
    roleActonMap.put(NOT_OWNER, action -> false);
    roleActonMap.put(SUPER_RESOURCE_OWNER, action -> action != CANCEL);
    roleActonMap.put(RESOURCE_OWNER, action -> action == CANCEL);
    return roleActonMap;
  }

  @Override
  public UserRole checkUserRole(long superResourceId, String resourceUser,
                                String requestUser, String userToken, BelongChecker belongChecker) {
    UserRole userRoleWithSuperResource = this.checkUserRole(superResourceId, requestUser,
        userToken, belongChecker);
    if (userRoleWithSuperResource == SUPER_RESOURCE_OWNER) {
      return userRoleWithSuperResource;
    } else {
      return resourceUser.equals(requestUser) ?
          RESOURCE_OWNER :
          NOT_OWNER;
    }
  }

  private void checkUserState(String requestUser, String userToken) {
    UserState userState = userService.checkState(requestUser, userToken);
    if (userState != UserState.ONLINE) {
      throw new UnauthorizedException(userState.name());
    }
  }

  @Override
  public UserRole checkUserRole(long itemId, String requestUser, String userToken,
                                BelongChecker belongChecker) {
    checkUserState(requestUser, userToken);
    return belongChecker.isBelong(itemId, requestUser) ?
        SUPER_RESOURCE_OWNER :
        NOT_OWNER;
  }

  @Override
  public boolean checkAction(UserRole role, ActionType action) {
    return roleActionMap.get(role).test(action);
  }
}
