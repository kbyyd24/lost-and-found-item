package cn.gaoyuexiang.LostAndFound.item.resource.provider;

import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import org.springframework.stereotype.Component;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
@Component
public class MissPropertyMapper implements ExceptionMapper<MissPropertyException> {
  @Override
  @Produces(APPLICATION_JSON)
  public Response toResponse(MissPropertyException exception) {
    return Response
        .status(BAD_REQUEST)
        .entity(new Message("miss some property"))
        .type(APPLICATION_JSON)
        .build();
  }
}
