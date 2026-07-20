package com.example.prices.infrastructure.adapter.out.db;

import com.example.prices.infrastructure.adapter.out.db.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

    /**
     * Retrieves all prices matching the given product and brand that are active
     * within the specified application date range.
     *
     * @param productId
     *            Unique identifier of the product.
     * @param brandId
     *            Unique identifier of the brand.
     * @param applicationDate
     *            Target validation date.
     * @return List of matching Price records to be processed by the service
     *         priority layer.
     */
    @Query("SELECT p FROM PriceEntity p WHERE p.productId = :productId "
            + "AND p.brandId = :brandId "
            + "AND :applicationDate BETWEEN p.startDate AND p.endDate")
    List<PriceEntity> findApplicablePrices(@Param("productId") Long productId,
            @Param("brandId") Long brandId,
            @Param("applicationDate") LocalDateTime applicationDate);
}
