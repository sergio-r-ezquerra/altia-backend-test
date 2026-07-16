package com.example.prices.infrastructure.config;

import com.example.prices.application.port.in.GetApplicablePriceUseCase;
import com.example.prices.application.port.out.PriceRepositoryPort;
import com.example.prices.application.service.GetApplicablePriceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public GetApplicablePriceUseCase getApplicablePriceUseCase(PriceRepositoryPort priceRepositoryPort) {
        return new GetApplicablePriceService(priceRepositoryPort);
    }
}
