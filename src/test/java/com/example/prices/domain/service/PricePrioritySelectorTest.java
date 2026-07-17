package com.example.prices.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.prices.domain.model.Price;

public class PricePrioritySelectorTest {

    private final PricePrioritySelector selector = new PricePrioritySelector();

    @Test
    void shouldReturnEmptyWhenCandidatesAreNullOrEmpty() {
        assertTrue(selector.selectHighestPriority(null).isEmpty());
        assertTrue(selector.selectHighestPriority(
                Collections.emptyList()).isEmpty());
    }

    @Test
    void shouldReturnSingleCandidateCorrectly() {
        // @formatter:off
        Price price = Price.builder()
                .productId(Long.valueOf(35455))
                .brandId(Long.valueOf(1))
                .priority(Integer.valueOf(0))
                .price(BigDecimal.valueOf(35.50))
                .build();
        // @formatter:on

        Optional<Price> result = selector.selectHighestPriority(
                Collections.singletonList(price));
        assertTrue(result.isPresent());
        assertEquals(price, result.get());
    }

    @Test
    void shouldReturnCandidateWithHighestPriority() {
        // @formatter:off
        Price lowPriority = Price.builder()
                .productId(Long.valueOf(35455))
                .brandId(Long.valueOf(1))
                .priority(Integer.valueOf(0))
                .price(BigDecimal.valueOf(35.50))
                .build();
        // @formatter:on

        // @formatter:off
        Price highPriority = Price.builder()
                .productId(Long.valueOf(35455))
                .brandId(Long.valueOf(1))
                .priority(Integer.valueOf(1))
                .price(BigDecimal.valueOf(25.45))
                .build();
        // @formatter:on

        Optional<Price> result = selector.selectHighestPriority(
                Arrays.asList(lowPriority, highPriority));
        assertTrue(result.isPresent());
        assertEquals(highPriority, result.get());
    }
}
