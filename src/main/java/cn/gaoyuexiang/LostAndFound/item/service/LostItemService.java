package cn.gaoyuexiang.LostAndFound.item.service;

import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;

import java.util.List;

public interface LostItemService {

  LostItem create(LostItemCreator lostItemCreator);

  List<LostItem> loadPage(int page, int listSize, String sort);

  LostItem loadOne(long itemId);

  LostItem close(long itemId);

}
