package com.example.prices.infrastructure.adapter.in.web;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.prices.application.port.in.GetApplicablePriceUseCase;
import com.example.prices.domain.model.Price;
import com.example.prices.domain.model.PriceRequest;
import com.example.prices.infrastructure.adapter.in.web.api.PricesApi;
import com.example.prices.infrastructure.adapter.in.web.dto.PriceResponse;

@RestController
public class PriceController implements PricesApi {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;

    public PriceController(
            GetApplicablePriceUseCase getApplicablePriceUseCase) {
        this.getApplicablePriceUseCase = getApplicablePriceUseCase;
    }

    @Override
    public ResponseEntity<PriceResponse> getApplicablePrice(
            LocalDateTime applicationDate, Long productId, Long brandId) {

        PriceRequest request =
                PriceRequest.builder().productId(productId).brandId(
                        brandId).applicationDate(applicationDate).build();

        Price price = getApplicablePriceUseCase.getApplicablePrice(request);

        PriceResponse response = new PriceResponse(price.getProductId(),
                price.getBrandId(), price.getPriceList(), price.getStartDate(),
                price.getEndDate(),
                Double.valueOf(price.getPrice().doubleValue()),
                price.getCurr());

        return ResponseEntity.ok(response);
    }
}
