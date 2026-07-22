package com.example.prices.infrastructure.adapter.in.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerTest {

    private static final String API_PRICES = "/api/prices";

    private static final String BRAND_ID = "brandId";
    private static final String PRODUCT_ID = "productId";
    private static final String APPLICATION_DATE = "applicationDate";

    private static final String EUR = "EUR";

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test 1
     * 
     * Input: applicationDate: 2020-06-14 10:00 productId: 35455 brandId: 1
     *
     * Expected result: priceList: 1 price: 35.50 EUR
     */
    @Test
    @DisplayName("Should return price 35.50 EUR when requesting product 35455 on 14/06/2020 at 10:00")
    void test1_RequestAt10AMOn14th_ShouldReturnPrice35_50() throws Exception {
        // @formatter:off
        mockMvc.perform(get(API_PRICES)
                        .param(PRODUCT_ID, "35455")
                        .param(BRAND_ID, "1")
                        .param(APPLICATION_DATE, "2020-06-14T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(Integer.valueOf(35455))))
                .andExpect(jsonPath("$.brandId", is(Integer.valueOf(1))))
                .andExpect(jsonPath("$.priceList", is(Integer.valueOf(1))))
                .andExpect(jsonPath("$.price", is(Double.valueOf(35.50))))
                .andExpect(jsonPath("$.currency", is(EUR)));
        // @formatter:on
    }

    /**
     * Test 2
     * 
     * Input: applicationDate: 2020-06-14 16:00 productId: 35455 brandId: 1
     *
     * Expected result: priceList: 2 price: 25.45 EUR
     */
    @Test
    @DisplayName("Should return price 25.45 EUR when requesting product 35455 on 14/06/2020 at 16:00")
    void test2_RequestAt4PMOn14th_ShouldReturnPrice25_45() throws Exception {
        // @formatter:off
        mockMvc.perform(get(API_PRICES)
                        .param(PRODUCT_ID, "35455")
                        .param(BRAND_ID, "1")
                        .param(APPLICATION_DATE, "2020-06-14T16:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(Integer.valueOf(35455))))
                .andExpect(jsonPath("$.brandId", is(Integer.valueOf(1))))
                .andExpect(jsonPath("$.priceList", is(Integer.valueOf(2))))
                .andExpect(jsonPath("$.price", is(Double.valueOf(25.45))))
                .andExpect(jsonPath("$.currency", is(EUR)));
        // @formatter:on
    }

    /**
     * Test 3
     * 
     * Input: applicationDate: 2020-06-14 21:00 productId: 35455 brandId: 1
     *
     * Expected result: priceList: 1 price: 35.50 EUR
     */
    @Test
    @DisplayName("Should return price 35.50 EUR when requesting product 35455 on 14/06/2020 at 21:00")
    void test3_RequestAt9PMOn14th_ShouldReturnPrice35_50() throws Exception {
        // @formatter:off
        mockMvc.perform(get(API_PRICES)
                        .param(PRODUCT_ID, "35455")
                        .param(BRAND_ID, "1")
                        .param(APPLICATION_DATE, "2020-06-14T21:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(Integer.valueOf(35455))))
                .andExpect(jsonPath("$.brandId", is(Integer.valueOf(1))))
                .andExpect(jsonPath("$.priceList", is(Integer.valueOf(1))))
                .andExpect(jsonPath("$.price", is(Double.valueOf(35.50))))
                .andExpect(jsonPath("$.currency", is(EUR)));
        // @formatter:on
    }

    /**
     * Test 4
     * 
     * Input: applicationDate: 2020-06-15 10:00 productId: 35455 brandId: 1
     *
     * Expected result: priceList: 3 price: 30.50 EUR
     */
    @Test
    @DisplayName("Should return price 30.50 EUR when requesting product 35455 on 15/06/2020 at 10:00")
    void test4_RequestAt10AMOn15th_ShouldReturnPrice30_50() throws Exception {
        // @formatter:off
        mockMvc.perform(get(API_PRICES)
                        .param(PRODUCT_ID, "35455")
                        .param(BRAND_ID, "1")
                        .param(APPLICATION_DATE, "2020-06-15T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(Integer.valueOf(35455))))
                .andExpect(jsonPath("$.brandId", is(Integer.valueOf(1))))
                .andExpect(jsonPath("$.priceList", is(Integer.valueOf(3))))
                .andExpect(jsonPath("$.price", is(Double.valueOf(30.50))))
                .andExpect(jsonPath("$.currency", is(EUR)));
        // @formatter:on
    }

    /**
     * Test 5
     * 
     * Input: applicationDate: 2020-06-16 19:00 productId: 35455 brandId: 1
     *
     * Expected result: priceList: 4 price: 38.95 EUR
     */
    @Test
    @DisplayName("Should return price 38.95 EUR when requesting product 35455 on 16/06/2020 at 19:00")
    void test5_RequestAt7PMOn16th_ShouldReturnPrice38_95() throws Exception {
        // @formatter:off
        mockMvc.perform(get(API_PRICES)
                        .param(PRODUCT_ID, "35455")
                        .param(BRAND_ID, "1")
                        .param(APPLICATION_DATE, "2020-06-16T19:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(Integer.valueOf(35455))))
                .andExpect(jsonPath("$.brandId", is(Integer.valueOf(1))))
                .andExpect(jsonPath("$.priceList", is(Integer.valueOf(4))))
                .andExpect(jsonPath("$.price", is(Double.valueOf(38.95))))
                .andExpect(jsonPath("$.currency", is(EUR)));
        // @formatter:on
    }

    /**
     * Test 6
     * 
     * Input: applicationDate: 2021-01-01 00:00 productId: 35455 brandId: 1
     *
     * Expected result: 404 Not Found
     */
    @Test
    @DisplayName("Should return 404 when requesting product 35455 on 01/01/2021 at 00:00")
    void test6_RequestWhereNoPriceExists_ShouldReturn404() throws Exception {
        // @formatter:off
        mockMvc.perform(get(API_PRICES)
                        .param(PRODUCT_ID, "35455")
                        .param(BRAND_ID, "1")
                        .param(APPLICATION_DATE, "2021-01-01T00:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(Integer.valueOf(404))))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message").exists());
        // @formatter:on
    }

    /**
     * Test 7
     * 
     * Input: applicationDate: 2020-06-15 10:00 productId: 35455 brandId: 1
     *
     * Expected result: 400 Bad Request
     */
    @Test
    @DisplayName("Should return 400 when requesting product 35455 with missing brandId")
    void test7_RequestWithMissingParameters_ShouldReturn400() throws Exception {
        // @formatter:off
        mockMvc.perform(get(API_PRICES)
                        .param(PRODUCT_ID, "35455")
                        .param(APPLICATION_DATE, "2020-06-14T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(Integer.valueOf(400))))
                .andExpect(jsonPath("$.error", is("Bad Request")));
        // @formatter:on
    }
}
