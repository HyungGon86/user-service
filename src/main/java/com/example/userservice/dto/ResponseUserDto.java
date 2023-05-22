package com.example.userservice.dto;

import com.example.userservice.domain.Orders;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseUserDto {

    private String email;
    private String name;

    private List<Orders> responseOrders = new ArrayList<>();

    @Builder
    public ResponseUserDto(String email, String name, List<Orders> responseOrders) {
        this.email = email;
        this.name = name;
        this.responseOrders = responseOrders;
    }
}
