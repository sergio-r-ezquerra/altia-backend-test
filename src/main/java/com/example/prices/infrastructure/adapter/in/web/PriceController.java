package com.example.prices.infrastructure.adapter.in.web;

import com.example.prices.application.port.in.GetApplicablePriceUseCase;
import com.example.prices.domain.model.Price;
import com.example.prices.domain.model.PriceRequest;
import com.example.prices.infrastructure.adapter.in.web.dto.PriceResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prices")
@Tag(name = "Prices", description = "Endpoints for applicable rates")
public class PriceController {

        private final GetApplicablePriceUseCase getApplicablePriceUseCase;

        public PriceController(
                        GetApplicablePriceUseCase getApplicablePriceUseCase) {
                this.getApplicablePriceUseCase = getApplicablePriceUseCase;
        }

        @GetMapping
        @Operation(summary = "Check applicable rate",
                        description = "Returns the data of the highest priority rate that applies to a product in a brand on a given date.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200",
                        description = "Fare found successfully",
                        content = @Content(mediaType = "application/json",
                                        schema = @Schema(
                                                        implementation = PriceResponseDTO.class))),
                        @ApiResponse(responseCode = "400",
                                        description = "Invalid input parameters or incorrect format",
                                        content = @Content(
                                                        mediaType = "application/json")),
                        @ApiResponse(responseCode = "404",
                                        description = "No applicable rate was found for the indicated criteria",
                                        content = @Content(
                                                        mediaType = "application/json")) })
        public ResponseEntity<PriceResponseDTO> getApplicablePrice(
                        @RequestParam("productId") @Parameter(
                                        description = "Product ID",
                                        example = "35455") Long productId,
                        @RequestParam("brandId") @Parameter(
                                        description = "Brand ID (example: 1 = ZARA)",
                                        example = "1") Long brandId,
                        @RequestParam("applicationDate") @Parameter(
                                        description = "Date the rate applies (ISO LocalDateTime)",
                                        example = "2020-06-14T10:00:00") @DateTimeFormat(
                                                        iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {

                PriceRequest request = PriceRequest.builder().productId(
                                productId).brandId(brandId).applicationDate(
                                                applicationDate).build();

                Price price = getApplicablePriceUseCase.getApplicablePrice(
                                request);

        // @formatter:off
        PriceResponseDTO response = PriceResponseDTO.builder()
                .productId(price.getProductId())
                .brandId(price.getBrandId())
                .priceList(price.getPriceList())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .price(price.getPrice())
                .curr(price.getCurr()).build();
        // @formatter:off

        return ResponseEntity.ok(response);
    }
}
