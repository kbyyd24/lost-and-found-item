package cn.gaoyuexiang.LostAndFound.item.resource;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

class ResponseBuilder {
  Response build(Status status, Object entity) {
    return Response
        .status(status)
        .entity(entity)
        .build();
  }
}
