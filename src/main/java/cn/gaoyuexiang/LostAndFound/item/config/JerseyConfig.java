package cn.gaoyuexiang.LostAndFound.item.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
  public JerseyConfig() {
    packages("cn.gaoyuexiang.LostAndFound.item.resource");
  }
}
