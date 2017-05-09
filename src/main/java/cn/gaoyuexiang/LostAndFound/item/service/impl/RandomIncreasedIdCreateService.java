package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RandomIncreasedIdCreateService implements IdCreateService {

  private long increaseStep;

  public RandomIncreasedIdCreateService(
      @Value("#{systemProperties['lost-and-found.id.increase.step'] ?: 4}") long increaseStep) {
    this.increaseStep = increaseStep;
  }

  @Override
  public long create(long base) {
    return base + Double.doubleToLongBits(Math.random() * increaseStep);
  }
}
