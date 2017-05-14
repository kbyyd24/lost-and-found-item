package cn.gaoyuexiang.LostAndFound.item.service;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FoundItemService {

  FoundItem create(FoundItemCreator creator, String createUser);

  List<FoundItemPageItem> loadPage(int page, int size, ItemSort sort);

  FoundItem loadOne(long itemId);

  FoundItem close(long itemId);

  FoundItem update(FoundItemCreator updater, long itemId, String updateUser);

  boolean isBelong(long itemId, String updateUser);

  boolean isClosed(long itemId);

}
