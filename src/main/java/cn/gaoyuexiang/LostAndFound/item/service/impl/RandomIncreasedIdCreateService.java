package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.config.IdCreateServiceProperties;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RandomIncreasedIdCreateService implements IdCreateService {

  private long increaseStep;
  private long initialBase;

  @Autowired
  public RandomIncreasedIdCreateService(IdCreateServiceProperties properties) {
    this.initialBase = properties.getInitialBase();
    this.increaseStep = properties.getIncreaseStep();
  }

  @Override
  public long create(long base) {
    return base < initialBase ?
        initialBase :
        base + Double.doubleToLongBits(Math.random() * increaseStep);
  }
}
