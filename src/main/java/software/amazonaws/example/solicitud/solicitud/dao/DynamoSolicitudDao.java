package software.amazonaws.example.solicitud.solicitud.dao;

import com.amazonaws.xray.interceptors.TracingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazonaws.example.solicitud.solicitud.entity.Solicitud;
import software.amazonaws.example.solicitud.solicitud.entity.Solicitudes;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DynamoSolicitudDao implements SolicitudDao {
  private static final Logger logger = LoggerFactory.getLogger(DynamoSolicitudDao.class);
  private static final String SOLICITUD_TABLE_NAME = System.getenv("SOLICITUD_TABLE_NAME");
  private final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
    .region(Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable())))
    .overrideConfiguration(ClientOverrideConfiguration.builder()
      .addExecutionInterceptor(new TracingInterceptor())
      .build())
    .httpClient(UrlConnectionHttpClient.builder().build())
    .build();

  @Override
  public Optional<Solicitud> getSolicitud(String id) {
    GetItemResponse getItemResponse = dynamoDbClient.getItem(GetItemRequest.builder()
      .key(Map.of("PK", AttributeValue.builder().s(id).build()))
      .tableName(SOLICITUD_TABLE_NAME)
      .build());

    if (getItemResponse.hasItem()) {
      return Optional.of(SolicitudMapper.solicitudFromDynamoDB(getItemResponse.item()));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void putSolicitud(Solicitud solicitud) {
    dynamoDbClient.putItem(PutItemRequest.builder()
      .tableName(SOLICITUD_TABLE_NAME)
      .item(SolicitudMapper.solicitudToDynamoDb(solicitud))
      .build());
  }

  @Override
  public void deleteSolicitud(String id) {
    dynamoDbClient.deleteItem(DeleteItemRequest.builder()
      .tableName(SOLICITUD_TABLE_NAME)
      .key(Map.of("PK", AttributeValue.builder().s(id).build()))
      .build());
  }

  @Override
  public Solicitudes getAllSolicitud() {
    ScanResponse scanResponse = dynamoDbClient.scan(ScanRequest.builder()
      .tableName(SOLICITUD_TABLE_NAME)
      .limit(20)
      .build());
    logger.info("Scan returned: {} item(s)", scanResponse.count());

    List<Solicitud> solicitudList = new ArrayList<>();

    for (Map<String, AttributeValue> item : scanResponse.items()) {
      solicitudList.add(SolicitudMapper.solicitudFromDynamoDB(item));
    }

    return new Solicitudes(solicitudList);
  }

  public void describeTable() {
    DescribeTableResponse response = dynamoDbClient.describeTable(DescribeTableRequest.builder()
      .tableName(SOLICITUD_TABLE_NAME)
      .build());
  }

}
