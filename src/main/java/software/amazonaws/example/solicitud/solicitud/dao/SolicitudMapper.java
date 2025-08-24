// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.solicitud.solicitud.dao;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazonaws.example.solicitud.solicitud.entity.Solicitud;

import java.math.BigDecimal;
import java.util.Map;

public class SolicitudMapper {
  private static final String PK = "PK";
  private static final String NAME = "name";
  private static final String PRICE = "price";

  public static Solicitud solicitudFromDynamoDB(Map<String, AttributeValue> items) {
    return new Solicitud(
      items.get(PK).s(),
      items.get(NAME).s(),
      new BigDecimal(items.get(PRICE).n())
    );
  }

  public static Map<String, AttributeValue> solicitudToDynamoDb(Solicitud solicitud) {
    return Map.of(
      PK, AttributeValue.builder().s(solicitud.id()).build(),
      NAME, AttributeValue.builder().s(solicitud.name()).build(),
      PRICE, AttributeValue.builder().n(solicitud.price().toString()).build()
    );
  }
}
