package cn.gaoyuexiang.LostAndFound.item.service;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;

import java.util.List;

public interface LostItemService {

  LostItem create(LostItemCreator lostItemCreator, String createUser);

  List<LostItemPageItem> loadPage(int page, int listSize, ItemSort sort);

  LostItem loadOne(long itemId);

  LostItem close(long itemId);

}
