package com.example.productsservice;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final AtomicLong idGenerator = new AtomicLong();
    private final Map<String, Product> productMap = new ConcurrentHashMap<>();

    private final UsersClient usersClient;

    public ProductsController(UsersClient usersClient) {
        this.usersClient = usersClient;
    }

    @GetMapping
    public List<Product> getAll() {
        List<User> users = usersClient.getAll();

        return users.stream()
                .map(u -> productMap.get(u.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @PostMapping
    public Product create(@RequestBody ProductCreateData productData) {
        List<User> users = usersClient.getAll();

        long productId = idGenerator.incrementAndGet();

        UserCreateData userData = new UserCreateData();
        userData.setName("User of product " + productId);
        User user = usersClient.create(userData);

        Product product = new Product();
        product.setId("product-" + productId);
        product.setTitle(productData.getTitle());
        product.setDescription(productData.getDescription());

        productMap.put(user.getId(), product);

        return product;
    }
}

class ProductCreateData {
    private String title;
    private String description;

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