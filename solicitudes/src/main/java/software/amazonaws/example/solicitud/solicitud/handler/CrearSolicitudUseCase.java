package software.amazonaws.example.solicitud.solicitud.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazonaws.example.solicitud.solicitud.dao.SolicitudDao;
import software.amazonaws.example.solicitud.solicitud.entity.Solicitud;
import java.util.function.Function;
@Component
public class CrearSolicitudUseCase implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  private final SolicitudDao solicitudDao;
  private final ObjectMapper objectMapper;

  CrearSolicitudUseCase(SolicitudDao solicitudDao, ObjectMapper objectMapper) {
    this.solicitudDao = solicitudDao;
    this.objectMapper = objectMapper;
  }
  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
    if (!requestEvent.getHttpMethod().equals(HttpMethod.PUT.name())) {
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(HttpStatusCode.METHOD_NOT_ALLOWED)
        .withBody("Only PUT method is supported");
    }
    try {
      String id = requestEvent.getPathParameters().get("id");
      String jsonPayload = requestEvent.getBody();
      Solicitud solicitud = objectMapper.readValue(jsonPayload, Solicitud.class);
      if (!solicitud.id().equals(id)) {
        return new APIGatewayProxyResponseEvent()
          .withStatusCode(HttpStatusCode.BAD_REQUEST)
          .withBody("Solicitud ID in the body does not match path parameter");
      }
      solicitudDao.putSolicitud(solicitud);
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(HttpStatusCode.CREATED)
        .withBody("Solicitud with id = " + id + " created");
    } catch (Exception e) {
      e.printStackTrace();
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
        .withBody("Internal Server Error :: " + e.getMessage());
    }
  }
}