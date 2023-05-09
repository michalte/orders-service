package com.example.ordersystem.service;

import com.example.ordersystem.dto.OrderResponseDto;
import com.example.ordersystem.repository.OrderRepository;
import com.example.ordersystem.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public Flux<OrderResponseDto> getOrdersByUserId(int userId) {
        return Flux.fromStream(() -> orderRepository.findByUserId(userId).stream()) //blocking operation
                .map(EntityDtoUtil::toOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
