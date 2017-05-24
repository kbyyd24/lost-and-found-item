package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {
  @Override
  public long getCurrentTime() {
    return System.currentTimeMillis();
  }
}
