package com.example.prices.infrastructure.config;

import com.example.prices.application.port.in.GetApplicablePriceUseCase;
import com.example.prices.application.port.out.PriceRepositoryPort;
import com.example.prices.application.service.GetApplicablePriceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.prices.domain.service.PricePrioritySelector;

@Configuration
public class BeanConfiguration {

    @Bean
    public PricePrioritySelector pricePrioritySelector() {
        return new PricePrioritySelector();
    }

    @Bean
    public GetApplicablePriceUseCase getApplicablePriceUseCase(
            PriceRepositoryPort priceRepositoryPort,
            PricePrioritySelector pricePrioritySelector) {
        return new GetApplicablePriceService(priceRepositoryPort, pricePrioritySelector);
    }
}
