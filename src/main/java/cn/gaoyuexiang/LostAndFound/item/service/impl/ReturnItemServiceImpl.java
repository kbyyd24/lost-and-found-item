package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import org.springframework.stereotype.Service;

@Service
public class ReturnItemServiceImpl implements ReturnItemService {
  @Override
  public boolean hasUnreadItem(long lostItemId) {
    return false;
  }
}
