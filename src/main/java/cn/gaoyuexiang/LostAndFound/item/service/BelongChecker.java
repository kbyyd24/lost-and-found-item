package cn.gaoyuexiang.LostAndFound.item.service;

import org.springframework.stereotype.Service;

@Service
public interface BelongChecker {

  boolean isBelong(long itemId, String username);

}
