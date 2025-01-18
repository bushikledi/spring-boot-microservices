package org.orderservice.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String productId;
    private int quantity;
}