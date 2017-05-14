package cn.gaoyuexiang.LostAndFound.item.service;

import org.springframework.stereotype.Service;

@Service
public interface ClaimItemService {

  boolean hasUnreadItem(long foundItemId);

}
