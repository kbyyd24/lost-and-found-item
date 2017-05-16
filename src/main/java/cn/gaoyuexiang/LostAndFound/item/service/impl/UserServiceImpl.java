package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.config.UserServiceProperties;
import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

@Service
public class UserServiceImpl implements UserService {

  private RestTemplate restTemplate;
  private Map<HttpStatus, UserState> stateMap;

  private String host;
  private String port;
  private String checkStateURI;

  @Autowired
  public UserServiceImpl(UserServiceProperties properties) {
    this.host = properties.getHost();
    this.port = properties.getPort();
    this.checkStateURI = properties.getCheckStateURI();
    this.restTemplate = new RestTemplate();
    initStateMap();
  }

  private void initStateMap() {
    this.stateMap = new HashMap<>(4);
    this.stateMap.put(HttpStatus.OK, UserState.ONLINE);
    this.stateMap.put(HttpStatus.UNAUTHORIZED, UserState.UNAUTHORIZED);
    this.stateMap.put(HttpStatus.NOT_FOUND, UserState.OFFLINE);
  }

  @Override
  public UserState checkState(String username, String userToken) {
    checkParams(username, userToken);
    String URI = checkStateURI.replace("username", username);
    String URL_FORMAT = "http://%s:%s%s";
    String path = String.format(URL_FORMAT, host, port, URI);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", userToken);
    HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
    ResponseEntity<Message> response =
        restTemplate.exchange(path, GET, requestEntity, Message.class);
    return stateMap.get(response.getStatusCode());
  }

  private void checkParams(String username, String userToken) {
    if (username == null || userToken == null) {
      throw new UnauthorizedException();
    }
  }
}
