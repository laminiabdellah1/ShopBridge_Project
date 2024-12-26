package com.lamini.ecommerce.customer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Object> finById(String customerId);
}
