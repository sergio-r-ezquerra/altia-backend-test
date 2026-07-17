package com.example.prices.application.service;

import com.example.prices.application.port.in.GetApplicablePriceUseCase;
import com.example.prices.application.port.out.PriceRepositoryPort;
import com.example.prices.domain.exception.PriceNotFoundException;
import com.example.prices.domain.model.Price;
import com.example.prices.domain.model.PriceRequest;

public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    public GetApplicablePriceService(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    @Override
    public Price getApplicablePrice(PriceRequest request) {
        if (request.getProductId() == null || request.getBrandId() == null) {
            throw new IllegalArgumentException("Product ID and Brand ID must not be null");
        }
        if (request.getApplicationDate() == null) {
            throw new IllegalArgumentException("Application date must not be null");
        }
        // @formatter:off
        return priceRepositoryPort.findApplicablePrice(
                request.getProductId(),
                request.getBrandId(),
                request.getApplicationDate()
        ).orElseThrow(() -> new PriceNotFoundException(
                String.format("Price not found for product %d, brand %d at date %s",
                        request.getProductId(), request.getBrandId(), request.getApplicationDate())
        ));
        // @formatter:on
    }
}
