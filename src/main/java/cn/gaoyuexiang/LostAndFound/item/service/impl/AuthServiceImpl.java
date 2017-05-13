package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.service.AuthService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final LostItemService lostItemService;

  @Autowired
  public AuthServiceImpl(UserService userService,
                         LostItemService lostItemService) {
    this.userService = userService;
    this.lostItemService = lostItemService;
  }

  @Override
  public UserRole checkUserRole(long superResourceId, String resourceUser,
                                String requestUser, String userToken) {
    checkUserState(requestUser, userToken);
    if (lostItemService.isBelong(superResourceId, requestUser)) {
      return UserRole.SUPER_RESOURCE_OWNER;
    }
    if (resourceUser.equals(requestUser)) {
      return UserRole.RESOURCE_OWNER;
    }
    return UserRole.NOT_OWNER;
  }

  private void checkUserState(String requestUser, String userToken) {
    UserState userState = userService.checkState(requestUser, userToken);
    if (userState != UserState.ONLINE) {
      throw new UnauthorizedException(userState.name());
    }
  }

  @Override
  public UserRole checkUserRole(long itemId, String requestUser, String userToken) {
    return null;
  }

  @Override
  public boolean checkAction(UserRole role, ActionType action) {
    return false;
  }
}
