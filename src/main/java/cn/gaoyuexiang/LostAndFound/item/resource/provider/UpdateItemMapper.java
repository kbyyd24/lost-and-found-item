package cn.gaoyuexiang.LostAndFound.item.resource.provider;

import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Provider
@Component
public class UpdateItemMapper implements ExceptionMapper<UpdateItemException> {
  @Override
  public Response toResponse(UpdateItemException exception) {
    Message entity = new Message(exception.getMessage());
    return Response
        .status(FORBIDDEN)
        .entity(entity)
        .build();
  }
}
