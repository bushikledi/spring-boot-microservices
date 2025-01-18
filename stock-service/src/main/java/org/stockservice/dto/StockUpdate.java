package org.stockservice.dto;

import lombok.Data;

@Data
public class StockUpdate {
    private Long id;
    private Long productId;
    private int quantity;
}
