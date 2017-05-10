package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.UserState;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.service.UserService;
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

  @Value("${lost-and-found.user.host}")
  private String host;
  @Value("${lost-and-found.user.port}")
  private String port;
  @Value("${lost-and-found.user.checkStateURI}")
  private String checkStateURI;

  public UserServiceImpl() {
    this.restTemplate = new RestTemplate();
    this.stateMap = new HashMap<>(4);
    this.stateMap.put(HttpStatus.OK, UserState.ONLINE);
    this.stateMap.put(HttpStatus.UNAUTHORIZED, UserState.UNAUTHORIZED);
    this.stateMap.put(HttpStatus.NOT_FOUND, UserState.OFFLINE);
  }

  @Override
  public UserState checkState(String username, String userToken) {
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
}
