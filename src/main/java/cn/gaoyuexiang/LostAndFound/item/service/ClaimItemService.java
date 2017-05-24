package cn.gaoyuexiang.LostAndFound.item.service;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClaimItemService {

  boolean hasUnreadItem(long foundItemId);

  List<ClaimItemPageItem> loadPage(long foundItemId, int page, int size, ItemSort sort);

  ClaimItem loadOne(long foundItemId, String claimUser);

  ClaimItem create(long foundItemId, String createUser, ClaimItemCreator creator);

  ClaimItem delete(long foundItemId, String claimUser, ActionType action);
}
