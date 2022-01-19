package com.example.productsservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "usersClient", url = "http://localhost:8080/users")
public interface UsersClient {
    @GetMapping
    List<User> getAll();

    @GetMapping("/{id}")
    User get(@PathVariable("id") String id);

    @PostMapping
    User create(@RequestBody UserCreateData userData);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") String id);
}