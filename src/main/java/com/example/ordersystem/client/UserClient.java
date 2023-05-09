package com.example.ordersystem.client;

import com.example.ordersystem.dto.TransactionRequestDto;
import com.example.ordersystem.dto.TransactionResponseDto;
import com.example.ordersystem.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserClient {

    private final WebClient webClient;

    public UserClient(@Value("${user.service.url}") String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mono<TransactionResponseDto> authorizeTransaction(TransactionRequestDto transactionRequestDto) {
        return webClient.post()
                .uri("transaction")
                .bodyValue(transactionRequestDto)
                .retrieve()
                .bodyToMono(TransactionResponseDto.class);
    }

    public Flux<UserDto> getAllUsers() {
        return webClient.get()
                .uri("all")
                .retrieve()
                .bodyToFlux(UserDto.class);
    }
}
