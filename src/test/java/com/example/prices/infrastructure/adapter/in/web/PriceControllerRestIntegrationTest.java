package com.example.prices.infrastructure.adapter.in.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceControllerRestIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/prices";
    }

    /**
     * Test 1
     * 
     * Input: applicationDate: 2020-06-14 10:00 productId: 35455 brandId: 1
     *
     * Expected result: priceList: 1 price: 35.50 EUR
     */
    @Test
    @DisplayName("Should return price 35.50 EUR when requesting product 35455 on 14/06/2020 at 10:00")
    void test1_RequestAt10AMOn14th_ShouldReturnPrice35_50() {
        // @formatter:off
        given()
            .queryParam("productId", Integer.valueOf(35455))
            .queryParam("brandId", Integer.valueOf(1))
            .queryParam("applicationDate", "2020-06-14T10:00:00")
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("productId", equalTo(Integer.valueOf(35455)))
            .body("brandId", equalTo(Integer.valueOf(1)))
            .body("priceList", equalTo(Integer.valueOf(1)))
            .body("price", equalTo(Float.valueOf(35.50f)))
            .body("currency", equalTo("EUR"));
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
    void test2_RequestAt4PMOn14th_ShouldReturnPrice25_45() {
        // @formatter:off
        given()
            .queryParam("productId", Integer.valueOf(35455))
            .queryParam("brandId", Integer.valueOf(1))
            .queryParam("applicationDate", "2020-06-14T16:00:00")
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("productId", equalTo(Integer.valueOf(35455)))
            .body("brandId", equalTo(Integer.valueOf(1)))
            .body("priceList", equalTo(Integer.valueOf(2)))
            .body("price", equalTo(Float.valueOf(25.45f)))
            .body("currency", equalTo("EUR"));
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
    void test3_RequestAt9PMOn14th_ShouldReturnPrice35_50() {
        // @formatter:off
        given()
            .queryParam("productId", Integer.valueOf(35455))
            .queryParam("brandId", Integer.valueOf(1))
            .queryParam("applicationDate", "2020-06-14T21:00:00")
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("productId", equalTo(Integer.valueOf(35455)))
            .body("brandId", equalTo(Integer.valueOf(1)))
            .body("priceList", equalTo(Integer.valueOf(1)))
            .body("price", equalTo(Float.valueOf(35.50f)))
            .body("currency", equalTo("EUR"));
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
    void test4_RequestAt10AMOn15th_ShouldReturnPrice30_50() {
        // @formatter:off
        given()
            .queryParam("productId", Integer.valueOf(35455))
            .queryParam("brandId", Integer.valueOf(1))
            .queryParam("applicationDate", "2020-06-15T10:00:00")
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("productId", equalTo(Integer.valueOf(35455)))
            .body("brandId", equalTo(Integer.valueOf(1)))
            .body("priceList", equalTo(Integer.valueOf(3)))
            .body("price", equalTo(Float.valueOf(30.50f)))
            .body("currency", equalTo("EUR"));
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
    void test5_RequestAt7PMOn16th_ShouldReturnPrice38_95() {
        // @formatter:off
        given()
            .queryParam("productId", Integer.valueOf(35455))
            .queryParam("brandId", Integer.valueOf(1))
            .queryParam("applicationDate", "2020-06-16T19:00:00")
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("productId", equalTo(Integer.valueOf(35455)))
            .body("brandId", equalTo(Integer.valueOf(1)))
            .body("priceList", equalTo(Integer.valueOf(4)))
            .body("price", equalTo(Float.valueOf(38.95f)))
            .body("currency", equalTo("EUR"));
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
    void test6_RequestWhereNoPriceExists_ShouldReturn404() {
        // @formatter:off
        given()
            .queryParam("productId", Integer.valueOf(35455))
            .queryParam("brandId", Integer.valueOf(1))
            .queryParam("applicationDate", "2021-01-01T00:00:00")
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(404)
            .body("status", equalTo(Integer.valueOf(404)))
            .body("error", equalTo("Not Found"))
            .body("message", notNullValue());
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
    void test7_RequestWithMissingParameters_ShouldReturn400() {
        // @formatter:off
        given()
            .queryParam("productId", Integer.valueOf(35455))
            .queryParam("applicationDate", "2020-06-14T10:00:00")
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(400)
            .body("status", equalTo(Integer.valueOf(400)))
            .body("error", equalTo("Bad Request"));
        // @formatter:on
    }
}
