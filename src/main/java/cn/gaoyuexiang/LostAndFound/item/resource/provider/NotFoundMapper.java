package cn.gaoyuexiang.LostAndFound.item.resource.provider;

import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
@Component
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {
  @Override
  public Response toResponse(NotFoundException exception) {
    return Response
        .status(NOT_FOUND)
        .entity(new Message(exception.getMessage()))
        .build();
  }
}
