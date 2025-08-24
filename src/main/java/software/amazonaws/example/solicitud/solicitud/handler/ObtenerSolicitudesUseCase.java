// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.solicitud.solicitud.handler;

import java.util.function.Function;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazonaws.example.solicitud.solicitud.dao.SolicitudDao;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class ObtenerSolicitudesUseCase implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  private final SolicitudDao solicitudDao;
  private final ObjectMapper objectMapper;

  public ObtenerSolicitudesUseCase(SolicitudDao solicitudDao, ObjectMapper objectMapper) {
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
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(HttpStatusCode.OK)
        .withBody(objectMapper.writeValueAsString(solicitudDao.getAllSolicitud()));
    } catch (JsonProcessingException je) {
      je.printStackTrace();
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
        .withBody("Internal Server Error");
    }
  }
}
