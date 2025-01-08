package com.lamini.ecommerce.product;

import com.lamini.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Integer createProduct(ProductRequest request) {
        var product = mapper.toProduct(request);
         return repository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        //list of productIds
        var productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        // stored products
        var storedProducts = repository.findAllByIdInOrderById(productIds);
        //check if the stored products is more than the in the request products
        if(storedProducts.size() != productIds.size()) {
            throw new ProductPurchaseException("One or more products does not exist !");
        }

        var storedRequests = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for(int i = 0; i<storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storedRequests.get(i);
            if(product.getAvailableQuantity() < productRequest.Quantity()) {
                throw new ProductPurchaseException("Insufficient stock quantity for the product with Id " + product.getId());
            }
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.Quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);
            purchasedProducts.add(mapper.toProductPurchaseResponse(product,productRequest.Quantity()));

        }

        return purchasedProducts;
    }

    public ProductResponse finById(Integer productId) {
        return repository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with the ID: "+ productId));
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
