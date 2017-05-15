package cn.gaoyuexiang.LostAndFound.item.service;

import org.springframework.stereotype.Service;

@Service
public interface CloseChecker {

  boolean isClosed(long itemId);

}
