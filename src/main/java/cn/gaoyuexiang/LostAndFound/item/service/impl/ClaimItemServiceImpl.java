package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import org.springframework.stereotype.Service;

@Service
public class ClaimItemServiceImpl implements ClaimItemService {
  @Override
  public boolean hasUnreadItem(long foundItemId) {
    return false;
  }
}
