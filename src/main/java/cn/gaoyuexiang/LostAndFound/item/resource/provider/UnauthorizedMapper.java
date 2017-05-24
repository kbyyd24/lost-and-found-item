package cn.gaoyuexiang.LostAndFound.item.resource.provider;

import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import org.springframework.stereotype.Component;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Provider
@Component
public class UnauthorizedMapper implements ExceptionMapper<UnauthorizedException> {

  @Override
  @Produces(APPLICATION_JSON)
  public Response toResponse(UnauthorizedException exception) {
    String msg = exception.getMessage();
    if (msg == null || msg.isEmpty()) {
      msg = "unauthorized";
    }
    return Response
        .status(UNAUTHORIZED)
        .entity(new Message(msg))
        .build();
  }
}
