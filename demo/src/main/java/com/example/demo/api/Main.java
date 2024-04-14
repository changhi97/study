package com.example.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
public class Main {

    @GetMapping("/call_api")
    public Flux<String> findAll() {
        return WebClient.create()
                .get()
                .uri("https://finnhub.io/api/v1/company-news?symbol=AAPL&from=2023-08-15&to=2023-08-20&token=cm6luchr01qg94pu6k20cm6luchr01qg94pu6k2g")
                .retrieve()
                .bodyToFlux(String.class);
    }

}
