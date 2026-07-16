package com.example.prices.application.port.in;

import com.example.prices.domain.model.Price;
import com.example.prices.domain.model.PriceRequest;

public interface GetApplicablePriceUseCase {

    /**
     * Evaluates and retrieves the applicable price based on the priority rules
     * for a given date, product, and brand.
     *
     * @param request
     *            Price request containing the date, product, and brand.
     * 
     * @return The applicable Price entity.
     */
    Price getApplicablePrice(PriceRequest request);
}
