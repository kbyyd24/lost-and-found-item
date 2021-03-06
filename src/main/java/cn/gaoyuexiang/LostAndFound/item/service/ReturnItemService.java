package cn.gaoyuexiang.LostAndFound.item.service;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReturnItemService {

  boolean hasUnreadItem(long lostItemId);

  List<ReturnItemPageItem> getReturnItemPageItems(long itemId,
                                                  int page,
                                                  int listSize,
                                                  ItemSort sort);

  ReturnItem getReturnItem(String username, long lostItemId);

  ReturnItem create(String username, long lostItemId, ReturnItemCreator creator);

  ReturnItem delete(String username, long lostItemId, ActionType actionType);

}
