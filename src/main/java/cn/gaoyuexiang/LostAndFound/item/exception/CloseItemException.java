package cn.gaoyuexiang.LostAndFound.item.exception;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class CloseItemException extends LostAndFoundException{
  public CloseItemException() {
    super(NOT_FOUND);
  }

  public CloseItemException(String message) {
    super(message);
  }
}
