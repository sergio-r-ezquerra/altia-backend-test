package com.example.prices.infrastructure.adapter.out.db;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.prices.application.port.out.PriceRepositoryPort;
import com.example.prices.domain.model.Price;
import com.example.prices.infrastructure.adapter.out.db.entity.PriceEntity;

/**
 * Outgoing persistence adapter implementing {@link PriceRepositoryPort}.
 * Encapsulates JPA repository access and handles domain-to-entity mapping.
 */
@Component
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final PriceRepository priceRepository;

    public PriceRepositoryAdapter(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public List<Price> findCandidatePrices(Long productId, Long brandId,
            LocalDateTime applicationDate) {
        return priceRepository.findApplicablePrices(productId, brandId,
                applicationDate).stream().map(this::mapToDomain).toList();
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
