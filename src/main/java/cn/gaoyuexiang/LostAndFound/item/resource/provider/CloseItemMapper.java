package cn.gaoyuexiang.LostAndFound.item.resource.provider;

import cn.gaoyuexiang.LostAndFound.item.exception.CloseItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Provider
@Component
public class CloseItemMapper implements ExceptionMapper<CloseItemException> {
  @Override
  public Response toResponse(CloseItemException exception) {
    Message message = new Message(exception.getMessage());
    return Response
        .status(FORBIDDEN)
        .entity(message)
        .build();
  }
}
