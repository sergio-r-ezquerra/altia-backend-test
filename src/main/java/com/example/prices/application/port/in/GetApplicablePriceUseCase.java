package com.example.prices.application.port.in;

import com.example.prices.domain.model.Price;
import com.example.prices.domain.model.PriceRequest;

public interface GetApplicablePriceUseCase {
    Price getApplicablePrice(PriceRequest request);
}
