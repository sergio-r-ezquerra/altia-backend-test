package com.example.prices.application.port.out;

import com.example.prices.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepositoryPort {

    /**
     * Evaluates and retrieves the applicable price based on the priority rules
     * for a given date, product, and brand.
     *
     * @param productId
     *            Unique identifier of the product.
     * 
     * @param brandId
     *            Unique identifier of the brand.
     * 
     * @param applicationDate
     *            Date and time to evaluate.
     * 
     * @return An Optional containing the applicable Price entity if found, or
     *         empty otherwise.
     */
    Optional<Price> findApplicablePrice(Long productId, Long brandId,
            LocalDateTime applicationDate);
}
