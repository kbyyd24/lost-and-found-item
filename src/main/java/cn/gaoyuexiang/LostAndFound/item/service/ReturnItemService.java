package cn.gaoyuexiang.LostAndFound.item.service;

import org.springframework.stereotype.Service;

@Service
public interface ReturnItemService {

  boolean hasUnreadItem(long lostItemId);

}
