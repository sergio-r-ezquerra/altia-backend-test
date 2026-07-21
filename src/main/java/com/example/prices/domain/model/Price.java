package com.example.prices.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Price {
    private final Long brandId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer priceList;
    private final Long productId;
    private final Integer priority;
    private final BigDecimal price;
    private final String curr;
}
