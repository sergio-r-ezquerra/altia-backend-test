package com.example.prices.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class PriceRequest {
    private Long productId;
    private Long brandId;
    private LocalDateTime applicationDate;
}
