package com.example.ordersystem.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class UserTransactionDto {

    private Integer id;
    private Integer userId;
    private Integer amount;
    private LocalDateTime transactionDate;

}
