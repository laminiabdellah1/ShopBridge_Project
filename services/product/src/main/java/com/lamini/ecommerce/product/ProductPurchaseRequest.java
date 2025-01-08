package com.lamini.ecommerce.product;

import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(

        @NotNull(message = "productId is a mandatory")
        Integer productId,
        @NotNull(message = "quanity is a mandatory")
        double Quantity
) {
}
