package cn.gaoyuexiang.LostAndFound.item.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "lost-and-found.user")
public class UserServiceProperties {

  private String host;
  private String port;
  private String checkStateURI;

  public UserServiceProperties() {}

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getCheckStateURI() {
    return checkStateURI;
  }

  public void setCheckStateURI(String checkStateURI) {
    this.checkStateURI = checkStateURI;
  }
}
