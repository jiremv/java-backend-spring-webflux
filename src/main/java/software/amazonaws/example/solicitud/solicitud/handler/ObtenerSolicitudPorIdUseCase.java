package software.amazonaws.example.solicitud.solicitud.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazonaws.example.solicitud.solicitud.dao.SolicitudDao;
import software.amazonaws.example.solicitud.solicitud.entity.Solicitud;

import java.util.Optional;
import java.util.function.Function;

@Component
public class ObtenerSolicitudPorIdUseCase implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  private final SolicitudDao solicitudDao;
  private final ObjectMapper objectMapper;

  ObtenerSolicitudPorIdUseCase(SolicitudDao solicitudDao, ObjectMapper objectMapper) {
    this.solicitudDao = solicitudDao;
    this.objectMapper = objectMapper;
  }

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
    if (!requestEvent.getHttpMethod().equals(HttpMethod.GET.name())) {
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(HttpStatusCode.METHOD_NOT_ALLOWED)
        .withBody("Only GET method is supported");
    }
    try {
      String id = requestEvent.getPathParameters().get("id");
      Optional<Solicitud> solicitud = solicitudDao.getSolicitud(id);
      if (solicitud.isEmpty()) {
        return new APIGatewayProxyResponseEvent()
          .withStatusCode(HttpStatusCode.NOT_FOUND)
          .withBody("Solicitud with id = " + id + " not found");
      }
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(HttpStatusCode.OK)
        .withBody(objectMapper.writeValueAsString(solicitud.get()));
    } catch (Exception je) {
      je.printStackTrace();
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
        .withBody("Internal Server Error :: " + je.getMessage());
    }
  }
}
