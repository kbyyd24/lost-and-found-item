package cn.gaoyuexiang.LostAndFound.item.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class MissPropertyException extends LostAndFoundException{
  public MissPropertyException() {
    super(BAD_REQUEST);
  }

  public MissPropertyException(String message) {
    super(message);
  }
}
