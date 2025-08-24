// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.solicitud.solicitud.entity;

import java.math.BigDecimal;

public record Solicitud(String id, String name, BigDecimal price) {
}
