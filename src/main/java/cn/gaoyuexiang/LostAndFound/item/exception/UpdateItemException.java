package cn.gaoyuexiang.LostAndFound.item.exception;

import javax.ws.rs.core.Response;

public class UpdateItemException extends LostAndFoundException{
  public UpdateItemException(String message) {
    super(message);
  }

  public UpdateItemException() {
    super(Response.Status.FORBIDDEN);
  }
}
