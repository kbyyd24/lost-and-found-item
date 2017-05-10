package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RandomIncreasedIdCreateService implements IdCreateService {

  private long increaseStep;
  private long initialBase;

  public RandomIncreasedIdCreateService(
      @Value("${lost-and-found.id.increase-step}") long increaseStep,
      @Value("${lost-and-found.id.initial-base}") long initialBase) {
    this.increaseStep = increaseStep;
    this.initialBase = initialBase;
  }

  @Override
  public long create(long base) {
    return base < initialBase ?
        initialBase :
        base + Double.doubleToLongBits(Math.random() * increaseStep);
  }
}
