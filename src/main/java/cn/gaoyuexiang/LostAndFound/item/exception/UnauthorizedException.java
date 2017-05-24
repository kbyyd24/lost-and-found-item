package cn.gaoyuexiang.LostAndFound.item.exception;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

public class UnauthorizedException extends LostAndFoundException{
  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException() {
    super(UNAUTHORIZED);
  }
}
