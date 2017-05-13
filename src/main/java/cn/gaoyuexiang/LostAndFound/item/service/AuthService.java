package cn.gaoyuexiang.LostAndFound.item.service;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

  UserRole checkUserRole(long superResourceId, String resourceUser,
                         String requestUser, String userToken);

  UserRole checkUserRole(long itemId, String requestUser, String userToken);

  boolean checkAction(UserRole role, ActionType action);

}
