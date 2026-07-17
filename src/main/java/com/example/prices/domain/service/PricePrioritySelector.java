package com.example.prices.domain.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.example.prices.domain.model.Price;

/**
 * Pure domain service responsible for determining the applicable rate based on
 * the priority business rule.
 */
public class PricePrioritySelector {

    /**
     * Select the applicable price (with the highest numerical priority) from a
     * list of candidates.
     *
     * @param candidates
     *            List of active candidate prices on a date.
     * @return An Optional field with the applicable price, or empty if there
     *         are no candidates.
     */
    @SuppressWarnings("null")
    public Optional<Price> selectHighestPriority(List<Price> candidates) {
        if (candidates == null) {
            return Optional.empty();
        }
        return candidates.stream().max(
                Comparator.comparing(Price::getPriority));
    }
}
