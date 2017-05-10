package cn.gaoyuexiang.LostAndFound.item.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class LostAndFoundException extends WebApplicationException{

  public LostAndFoundException(String message) {
    super(message);
  }

  public LostAndFoundException(Response.Status status) {
    super(status);
  }
}
