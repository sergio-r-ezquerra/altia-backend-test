package com.example.prices.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.prices.application.port.out.PriceRepositoryPort;
import com.example.prices.domain.exception.PriceNotFoundException;
import com.example.prices.domain.model.Price;
import com.example.prices.domain.model.PriceRequest;
import com.example.prices.domain.service.PricePrioritySelector;

@ExtendWith(MockitoExtension.class)
public class GetApplicablePriceServiceTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    private GetApplicablePriceService getApplicablePriceService;

    private PriceRequest testRequest;
    private Price testPrice;

    @BeforeEach
    void setUp() {
        // Inicializamos manualmente inyectando el PricePrioritySelector real
        getApplicablePriceService = new GetApplicablePriceService(priceRepositoryPort, new PricePrioritySelector());

        // @formatter:off
        testRequest = PriceRequest.builder()
                .productId(Long.valueOf(35455))
                .brandId(Long.valueOf(1))
                .applicationDate(LocalDateTime.of(2020, 6, 14, 10, 0))
                .build();
        // @formatter:on

        // @formatter:off
        testPrice = Price.builder()
                .brandId(Long.valueOf(1))
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .priceList(Integer.valueOf(1))
                .productId(Long.valueOf(35455))
                .priority(Integer.valueOf(0))
                .price(BigDecimal.valueOf(35.50))
                .curr("EUR")
                .build();
        // @formatter:on
    }

    // -------------------------------------------------------------------------
    // Happy path
    // -------------------------------------------------------------------------

    @Test
    void testGetApplicablePriceSuccess() {
        // Arrange
        when(priceRepositoryPort.findCandidatePrices(testRequest.getProductId(),
                testRequest.getBrandId(),
                testRequest.getApplicationDate())).thenReturn(
                        List.of(testPrice));

        // Act
        Price result =
                getApplicablePriceService.getApplicablePrice(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testPrice, result);
        verify(priceRepositoryPort, times(1)).findCandidatePrices(
                testRequest.getProductId(), testRequest.getBrandId(),
                testRequest.getApplicationDate());
    }

    @Test
    void testGetApplicablePriceReturnsAllFields() {
        // Arrange
        when(priceRepositoryPort.findCandidatePrices(testRequest.getProductId(),
                testRequest.getBrandId(),
                testRequest.getApplicationDate())).thenReturn(
                        List.of(testPrice));

        // Act
        Price result =
                getApplicablePriceService.getApplicablePrice(testRequest);

        // Assert — every domain field must propagate unchanged
        assertAll("All Price fields must be returned unchanged",
                () -> assertEquals(Long.valueOf(1), result.getBrandId()),
                () -> assertEquals(Long.valueOf(35455), result.getProductId()),
                () -> assertEquals(Integer.valueOf(1), result.getPriceList()),
                () -> assertEquals(Integer.valueOf(0), result.getPriority()),
                () -> assertEquals(BigDecimal.valueOf(35.50),
                        result.getPrice()),
                () -> assertEquals("EUR", result.getCurr()),
                () -> assertEquals(LocalDateTime.of(2020, 6, 14, 0, 0),
                        result.getStartDate()),
                () -> assertEquals(LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                        result.getEndDate()));
    }

    // -------------------------------------------------------------------------
    // Price not found
    // -------------------------------------------------------------------------

    @Test
    void testGetApplicablePriceNotFound() {
        // Arrange
        when(priceRepositoryPort.findCandidatePrices(testRequest.getProductId(),
                testRequest.getBrandId(),
                testRequest.getApplicationDate())).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(PriceNotFoundException.class, () -> {
            getApplicablePriceService.getApplicablePrice(testRequest);
        });

        verify(priceRepositoryPort, times(1)).findCandidatePrices(
                testRequest.getProductId(), testRequest.getBrandId(),
                testRequest.getApplicationDate());
    }

    @Test
    void testGetApplicablePriceNotFoundExceptionMessage() {
        // Arrange
        when(priceRepositoryPort.findCandidatePrices(testRequest.getProductId(),
                testRequest.getBrandId(),
                testRequest.getApplicationDate())).thenReturn(Collections.emptyList());

        // Act & Assert
        PriceNotFoundException ex =
                assertThrows(PriceNotFoundException.class, () -> {
                    getApplicablePriceService.getApplicablePrice(testRequest);
                });

        String expectedMessage = String.format(
                "Price not found for product %d, brand %d at date %s",
                testRequest.getProductId(), testRequest.getBrandId(),
                testRequest.getApplicationDate());
        assertEquals(expectedMessage, ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Input validation — null fields
    // -------------------------------------------------------------------------

    @Test
    void testGetApplicablePriceWithNullProductId() {
        // Arrange
        PriceRequest request = PriceRequest.builder().productId(null).brandId(
                Long.valueOf(1)).applicationDate(LocalDateTime.now()).build();

        // Act & Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    getApplicablePriceService.getApplicablePrice(request);
                });

        assertEquals("Product ID and Brand ID must not be null",
                exception.getMessage());
    }

    @Test
    void testGetApplicablePriceWithNullBrandId() {
        // Arrange
        // @formatter:off
        PriceRequest request = PriceRequest.builder()
                .productId(Long.valueOf(35455))
                .brandId(null)
                .applicationDate(LocalDateTime.now())
                .build();
        // @formatter:on

        // Act & Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    getApplicablePriceService.getApplicablePrice(request);
                });

        assertEquals("Product ID and Brand ID must not be null",
                exception.getMessage());
    }

    @Test
    void testGetApplicablePriceWithNullApplicationDate() {
        // Arrange
        // @formatter:off
        PriceRequest request = PriceRequest.builder()
                .productId(Long.valueOf(35455))
                .brandId(Long.valueOf(1))
                .applicationDate(null)
                .build();
        // @formatter:on

        // Act & Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    getApplicablePriceService.getApplicablePrice(request);
                });

        assertEquals("Application date must not be null",
                exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Input validation — Repository not invoked on null inputs
    // -------------------------------------------------------------------------

    @Test
    void testGetApplicablePriceNullProductIdDoesNotInvokeRepository() {
        // Arrange
        PriceRequest request = PriceRequest.builder().productId(null).brandId(
                Long.valueOf(1)).applicationDate(LocalDateTime.now()).build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getApplicablePriceService.getApplicablePrice(request);
        });

        verifyNoInteractions(priceRepositoryPort);
    }

    @Test
    void testGetApplicablePriceNullBrandIdDoesNotInvokeRepository() {
        // Arrange
        // @formatter:off
        PriceRequest request = PriceRequest.builder()
                .productId(Long.valueOf(35455))
                .brandId(null)
                .applicationDate(LocalDateTime.now())
                .build();
        // @formatter:on

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getApplicablePriceService.getApplicablePrice(request);
        });

        verifyNoInteractions(priceRepositoryPort);
    }

    @Test
    void testGetApplicablePriceNullDateDoesNotInvokeRepository() {
        // Arrange
        // @formatter:off
        PriceRequest request = PriceRequest.builder()
                .productId(Long.valueOf(35455))
                .brandId(Long.valueOf(1))
                .applicationDate(null)
                .build();
        // @formatter:on

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getApplicablePriceService.getApplicablePrice(request);
        });

        verifyNoInteractions(priceRepositoryPort);
    }

    // -------------------------------------------------------------------------
    // Variations on the request (toBuilder copy)
    // -------------------------------------------------------------------------

    @Test
    void testGetApplicablePriceWithDifferentProduct() {
        // Arrange
        // @formatter:off
        PriceRequest request = testRequest.toBuilder()
                .productId(Long.valueOf(12345))
                .build();
        // @formatter:on

        when(priceRepositoryPort.findCandidatePrices(Long.valueOf(12345),
                Long.valueOf(1), testRequest.getApplicationDate())).thenReturn(
                        Collections.emptyList());

        // Act & Assert
        assertThrows(PriceNotFoundException.class, () -> {
            getApplicablePriceService.getApplicablePrice(request);
        });

        verify(priceRepositoryPort, times(1)).findCandidatePrices(
                Long.valueOf(12345), Long.valueOf(1),
                testRequest.getApplicationDate());
    }

    @Test
    void testGetApplicablePriceWithDifferentBrand() {
        // Arrange
        // @formatter:off
        PriceRequest request = testRequest.toBuilder()
                .brandId(Long.valueOf(2))
                .build();
        // @formatter:on

        when(priceRepositoryPort.findCandidatePrices(testRequest.getProductId(),
                Long.valueOf(2), testRequest.getApplicationDate())).thenReturn(
                        Collections.emptyList());

        // Act & Assert
        assertThrows(PriceNotFoundException.class, () -> {
            getApplicablePriceService.getApplicablePrice(request);
        });

        verify(priceRepositoryPort, times(1)).findCandidatePrices(
                testRequest.getProductId(), Long.valueOf(2),
                testRequest.getApplicationDate());
    }

    @Test
    void testGetApplicablePriceWithDifferentDate() {
        // Arrange
        // @formatter:off
        PriceRequest request = testRequest.toBuilder()
                .applicationDate(LocalDateTime.of(2020, 6, 15, 10, 0))
                .build();
        // @formatter:on

        when(priceRepositoryPort.findCandidatePrices(testRequest.getProductId(),
                testRequest.getBrandId(),
                request.getApplicationDate())).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(PriceNotFoundException.class, () -> {
            getApplicablePriceService.getApplicablePrice(request);
        });

        verify(priceRepositoryPort, times(1)).findCandidatePrices(
                testRequest.getProductId(), testRequest.getBrandId(),
                request.getApplicationDate());
    }
}
