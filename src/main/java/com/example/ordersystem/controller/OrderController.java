package com.example.ordersystem.controller;

import com.example.ordersystem.dto.OrderRequestDto;
import com.example.ordersystem.dto.OrderResponseDto;
import com.example.ordersystem.service.OrderQueryService;
import com.example.ordersystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderQueryService orderQueryService;

    @PostMapping
    public Mono<ResponseEntity<OrderResponseDto>> order(@RequestBody Mono<OrderRequestDto> orderRequestDtoMono) {
        return orderService.processOrder(orderRequestDtoMono)
                .map(ResponseEntity::ok)
                .onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
                .onErrorReturn(WebClientRequestException.class, ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

    @GetMapping("user/{id}")
    public Flux<OrderResponseDto> findAllUserOrders(@PathVariable int id) {
        return orderQueryService.getOrdersByUserId(id);
    }

}
