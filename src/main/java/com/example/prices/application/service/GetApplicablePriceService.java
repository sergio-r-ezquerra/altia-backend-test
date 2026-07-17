package com.example.prices.application.service;

import com.example.prices.application.port.in.GetApplicablePriceUseCase;
import com.example.prices.application.port.out.PriceRepositoryPort;
import com.example.prices.domain.exception.PriceNotFoundException;
import com.example.prices.domain.model.Price;
import com.example.prices.domain.model.PriceRequest;
import com.example.prices.domain.service.PricePrioritySelector;

import java.util.List;

public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;
    private final PricePrioritySelector pricePrioritySelector;

    public GetApplicablePriceService(PriceRepositoryPort priceRepositoryPort,
            PricePrioritySelector pricePrioritySelector) {
        this.priceRepositoryPort = priceRepositoryPort;
        this.pricePrioritySelector = pricePrioritySelector;
    }

    @Override
    public Price getApplicablePrice(PriceRequest request) {
        if (request.getProductId() == null || request.getBrandId() == null) {
            throw new IllegalArgumentException(
                    "Product ID and Brand ID must not be null");
        }
        if (request.getApplicationDate() == null) {
            throw new IllegalArgumentException(
                    "Application date must not be null");
        }

        List<Price> candidates =
                priceRepositoryPort.findCandidatePrices(request.getProductId(),
                        request.getBrandId(), request.getApplicationDate());

        return pricePrioritySelector.selectHighestPriority(
                candidates).orElseThrow(
                        () -> new PriceNotFoundException(String.format(
                                "Price not found for product %d, brand %d at date %s",
                                request.getProductId(), request.getBrandId(),
                                request.getApplicationDate())));
    }
}
