package com.example.ordersystem.service;

import com.example.ordersystem.client.ProductClient;
import com.example.ordersystem.client.UserClient;
import com.example.ordersystem.dto.OrderRequestDto;
import com.example.ordersystem.dto.OrderResponseDto;
import com.example.ordersystem.dto.RequestContext;
import com.example.ordersystem.repository.OrderRepository;
import com.example.ordersystem.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;

    private final UserClient userClient;

    private final OrderRepository orderRepository;

    public Mono<OrderResponseDto> processOrder(Mono<OrderRequestDto> orderRequestDtoMono) {
        return orderRequestDtoMono
                .map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                .map(EntityDtoUtil::getOrder)
                .map(orderRepository::save) //blocking operation
                .map(EntityDtoUtil::toOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }



    private Mono<RequestContext> productRequestResponse(RequestContext requestContext) {
        return productClient.getProductById(requestContext.getOrderRequestDto().getProductId())
                .doOnNext(requestContext::setProductDto)
                .retryWhen(Retry.fixedDelay(5, Duration.of(1, ChronoUnit.SECONDS)))
                .thenReturn(requestContext);
    }

    private Mono<RequestContext> userRequestResponse(RequestContext requestContext) {
        return userClient.authorizeTransaction(requestContext.getTransactionRequestDto())
                .doOnNext(requestContext::setTransactionResponseDto)
                .retryWhen(Retry.fixedDelay(5, Duration.of(1, ChronoUnit.SECONDS)))
                .thenReturn(requestContext);
    }

}
