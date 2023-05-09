package com.example.ordersystem.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderRequestDto {

    private Integer userId;
    private String productId;

}
