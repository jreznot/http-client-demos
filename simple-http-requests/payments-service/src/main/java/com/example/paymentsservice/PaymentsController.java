package com.example.paymentsservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/payments")
public class PaymentsController {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8081")
            .build();

    @GetMapping
    public Flux<Payment> getAll() {
        Flux<Product> response = webClient.get()
                .uri("/products")
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Product.class));

        return response.map(p -> new Payment(p.getId(), "10$"));
    }
}

class Product {
    private String id;

    private String title;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class Payment {

    String product;
    String cost;

    public Payment(String product, String cost) {
        this.product = product;
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}