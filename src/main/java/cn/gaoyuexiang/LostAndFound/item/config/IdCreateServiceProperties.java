package cn.gaoyuexiang.LostAndFound.item.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "lost-and-found.id")
public class IdCreateServiceProperties {

  private long initialBase;
  private long increaseStep;

  public IdCreateServiceProperties() {}

  public long getInitialBase() {
    return initialBase;
  }

  public void setInitialBase(long initialBase) {
    this.initialBase = initialBase;
  }

  public long getIncreaseStep() {
    return increaseStep;
  }

  public void setIncreaseStep(long increaseStep) {
    this.increaseStep = increaseStep;
  }
}
