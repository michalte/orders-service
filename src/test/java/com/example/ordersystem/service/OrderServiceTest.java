package com.example.ordersystem.service;

import com.example.ordersystem.client.ProductClient;
import com.example.ordersystem.client.UserClient;
import com.example.ordersystem.dto.OrderRequestDto;
import com.example.ordersystem.dto.OrderResponseDto;
import com.example.ordersystem.dto.ProductDto;
import com.example.ordersystem.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private OrderService orderService;

    @Test
    void processOrders() {
        Flux<OrderResponseDto> orderResponseDtoFlux = Flux.zip(userClient.getAllUsers(), productClient.getAllProducts())
                .map(tuple2 -> createOrderRequest(tuple2.getT1(), tuple2.getT2()))
                .flatMap(orderRequestDto -> orderService.processOrder(Mono.just(orderRequestDto)))
                .doOnNext(System.out::println);

        StepVerifier.create(orderResponseDtoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    private OrderRequestDto createOrderRequest(UserDto userDto, ProductDto productDto){
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setProductId(productDto.getId());
        orderRequestDto.setUserId(userDto.getId());
        return orderRequestDto;
    }
}
