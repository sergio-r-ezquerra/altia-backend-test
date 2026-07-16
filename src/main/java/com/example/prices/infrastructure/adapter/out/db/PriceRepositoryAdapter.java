package com.example.prices.infrastructure.adapter.out.db;

import com.example.prices.application.port.out.PriceRepositoryPort;
import com.example.prices.domain.model.Price;
import com.example.prices.infrastructure.adapter.out.db.entity.PriceEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final PriceRepository priceRepository;

    public PriceRepositoryAdapter(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Optional<Price> findApplicablePrice(Long productId, Long brandId,
            LocalDateTime applicationDate) {
        return priceRepository.findApplicablePrices(productId, brandId,
                applicationDate).stream().findFirst().map(this::mapToDomain);
    }

    private Price mapToDomain(PriceEntity entity) {
        // @formatter:off
        return Price.builder()
                .brandId(entity.getBrandId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceList(entity.getPriceList())
                .productId(entity.getProductId())
                .priority(entity.getPriority())
                .price(entity.getPrice())
                .curr(entity.getCurr())
                .build();
        // @formatter:on
    }
}
