package cn.gaoyuexiang.LostAndFound.item.service;

import cn.gaoyuexiang.LostAndFound.item.enums.UserState;

public interface UserService {

  UserState checkState(String username, String userToken);

}
