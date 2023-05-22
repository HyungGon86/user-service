package com.example.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseOrderDto {

    private String productId;
    private int qty;
    private int unitPrice;
    private int totalPrice;
    private LocalDateTime createdAt;

    private String orderId;

    @Builder
    public ResponseOrderDto(String productId, int qty, int unitPrice, int totalPrice, LocalDateTime createdAt, String orderId) {
        this.productId = productId;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.orderId = orderId;
    }
}
