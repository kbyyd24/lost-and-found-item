package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimItemServiceImpl implements ClaimItemService {
  @Override
  public boolean hasUnreadItem(long foundItemId) {
    return false;
  }

  @Override
  public List<ClaimItemPageItem> loadPage(long foundItemId, int page, int size, int sort) {
    return null;
  }

  @Override
  public ClaimItem loadOne(long foundItemId, String claimUser) {
    return null;
  }

  @Override
  public ClaimItem create(long foundItemId, String createUser, ClaimItemCreator creator) {
    return null;
  }

  @Override
  public ClaimItem delete(long foundItemId, String claimUser, ActionType action) {
    return null;
  }
}
