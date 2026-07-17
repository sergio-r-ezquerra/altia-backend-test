package com.example.prices.application.port.out;

import java.time.LocalDateTime;
import java.util.List;

import com.example.prices.domain.model.Price;

/**
 * Outcoming port defining the contract for database operations related to prices.
 */
public interface PriceRepositoryPort {

    /**
     * Retrieves all candidate prices for a given date, product, and brand. The
     * business rule for selecting the highest priority price must be applied by
     * the caller (domain layer).
     *
     * @param productId
     *            Unique identifier of the product.
     * @param brandId
     *            Unique identifier of the brand.
     * @param applicationDate
     *            Date and time to evaluate.
     * @return A list of candidate Price entities (may be empty).
     */
    List<Price> findCandidatePrices(Long productId, Long brandId,
            LocalDateTime applicationDate);
}
