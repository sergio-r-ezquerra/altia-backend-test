# Altia backend test

This is a production-ready RESTful microservice built with **Spring Boot 3.x** that determines the final applicable price (`PVP`) for a product within a specific brand and application date.

The application utilizes an in-memory **H2 database** pre-populated with realistic e-commerce scheduling data and natively resolves potential price-schedule overlaps by enforcing predefined numeric rule priorities.

---

## 🏛️ Architectural Approach: Hexagonal Architecture

To meet clean code guidelines and corporate decoupling principles, this project is designed using **Hexagonal Architecture (Ports & Adapters)**.

* **Domain Layer:** Contains the pure business logic, exceptions, core data models (`Price`, `PriceRequest`), and domain services like `PricePrioritySelector` (which implements the "highest priority wins" rule). It remains strictly agnostic and carries **zero dependencies** on external frameworks or Spring frameworks.
* **Application Layer (Ports):** Establishes the boundaries of the system through inbound ports (Use Cases) and outbound ports (Repository Interfaces).
* **Infrastructure Layer (Adapters):** Connects the application to specific technologies. It features incoming web components (`@RestController`), automated global exception handling (`@ControllerAdvice`), and database-specific logic via Spring Data JPA interacting with H2.

---

## 🚀 Technical Requirements Met

* **Spring Boot 3.x** & **Java 21** development stack.
* **Hexagonal Architecture** structure for strict isolation of business logic.
* **Automated Data Seeding:** Database automatically seeds sample records upon application bootstrap.
* **Robust Exception Handling:** Gracefully maps edge cases (e.g., entity not found or invalid payloads) into structured JSON error responses with explicit HTTP status codes.
* **Comprehensive Test Coverage:** Unit, MockMvc slice, and End-to-End REST-Assured integration tests validating the 5 explicit date-priority scenarios detailed in the requirement specs.
* **OpenAPI 3 / Swagger Integration:** Interactive API documentation fully accessible natively through the UI.
* **Docker Ready:** Multistage ultra-lightweight container builds using Alpine JRE mirrors.

---

## 🛠️ Tech Stack & Key Dependencies

This project relies on the following core dependencies configured in `pom.xml`:

* **Spring Boot (Starter Parent):** Provides the foundational framework for building the microservice.
  * `spring-boot-starter-webmvc`: Standard dependency for exposing REST endpoints and managing web request/response layers.
  * `spring-boot-starter-data-jpa`: Handles JPA configurations, Hibernate ORM integration, and transactional database mapping.
  * `spring-boot-starter-validation`: Used for structural validation of incoming request query parameters and data contracts.
* **H2 Database:** In-memory relational database used for local execution, seeding sample scheduling records, and fast test verification.
  * `spring-boot-h2console`: Auto-configures and exposes the web interface console for debugging data.
* **Lombok:** Reduces verbose boilerplate code (e.g. getters, setters, constructors, builders) via annotation processors.
* **Springdoc OpenAPI (v3.0.3):** Integrates Swagger UI to render interactive API testing pages.
* **Testing Libraries:**
  * `spring-boot-starter-test`: Standard dependency bundling JUnit 5, Mockito, AssertJ, and JSONPath utilities.
  * `spring-boot-starter-webmvc-test`: Provides auto-configuration for Spring MVC slicing and MockMvc test utilities.
  * `rest-assured` (v5.5.1): Framework used for automated End-to-End REST API integration tests over HTTP without mocks.

---

## 🛠️ Local Setup & Execution

### Prerequisites
* Java 21 or higher
* Maven 3.8+

### Build and Run locally
To compile the package, execute tests, and start the local runtime server:

```bash
# Clean, compile, and run automated verification tests
./mvnw clean install

# Spin up the Spring Boot application server locally
./mvnw spring-boot:run
```
The server will boot up and listen on port **8080** by default.

---

## 🐳 Docker Deployment

The project contains a multi-stage `Dockerfile` which isolates compilation from execution, maintaining a lean footprint and removing build overhead from the production container.

### Using Standard Docker Commands
```bash
# 1. Build the lightweight production docker image
docker build -t backend-test-sergio .

# 2. Spin up the application mapping traffic to port 8080
docker run -d -p 8080:8080 --name app-prices-service backend-test-sergio

# 3. Stream real-time container application logs
docker logs -f app-prices-service
```

### Using Docker Compose (Recommended)
If you prefer singular orchestrations, spin up the complete isolated runtime with a single command:
```bash
docker compose up -d
```

---

## 📖 API Documentation & Testing Endpoints

Once the application is up and running via local Maven or Docker, you can inspect and manually query the endpoint via Swagger UI:

* **Interactive Swagger Documentation Interface:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **Raw OpenAPI Specification JSON Data:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

### Sample Endpoint Query Request
```http
GET /api/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

---

## 🧪 Automated Testing Suite

The application includes a thorough testing suite categorized into Unit, Web Slice (MockMvc), and REST API Integration (REST-Assured) tests.

To run the complete test suite:
```bash
./mvnw clean test
```

### 1. REST API Integration Tests without Mocks (`PriceControllerRestIntegrationTest`)

These tests run against a real running Spring Boot application context listening on a random HTTP port (`SpringBootTest.WebEnvironment.RANDOM_PORT`) connected to the in-memory H2 database, executing actual HTTP requests using **REST-Assured** without mocks.

#### Tested Requirements & Use Cases:

| Test Case | Method | Date / Time | Product ID | Brand ID | Expected Price List | Target Price | Expected HTTP Status / Outcome |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---|
| **Test 1** | `test1_RequestAt10AMOn14th_ShouldReturnPrice35_50` | June 14, 2020 10:00 | 35455 | 1 | **1** | **35.50 EUR** | `200 OK` (Base default price) |
| **Test 2** | `test2_RequestAt4PMOn14th_ShouldReturnPrice25_45` | June 14, 2020 16:00 | 35455 | 1 | **2** | **25.45 EUR** | `200 OK` (Overruled by higher priority 1) |
| **Test 3** | `test3_RequestAt9PMOn14th_ShouldReturnPrice35_50` | June 14, 2020 21:00 | 35455 | 1 | **1** | **35.50 EUR** | `200 OK` (Timeframe expired; reverts to base) |
| **Test 4** | `test4_RequestAt10AMOn15th_ShouldReturnPrice30_50` | June 15, 2020 10:00 | 35455 | 1 | **3** | **30.50 EUR** | `200 OK` (Active timeframe with Priority 1) |
| **Test 5** | `test5_RequestAt7PMOn16th_ShouldReturnPrice38_95` | June 16, 2020 19:00 | 35455 | 1 | **4** | **38.95 EUR** | `200 OK` (Final promotion active with Priority 1) |
| **Test 6** | `test6_RequestWhereNoPriceExists_ShouldReturn404` | Jan 01, 2021 00:00 | 35455 | 1 | - | - | `404 Not Found` (No active price match) |
| **Test 7** | `test7_RequestWithMissingParameters_ShouldReturn400` | June 14, 2020 10:00 | 35455 | - | - | - | `400 Bad Request` (Missing required `brandId`) |

### 2. Spring MVC Web Slice Tests (`PriceControllerTest`)

Uses `MockMvc` and Spring Boot test slicing (`@SpringBootTest` with `@AutoConfigureMockMvc`) to validate request mappings, query parameters, content types, and JSON responses.

### 3. Unit Tests — Domain & Business Layer

* **Domain Layer (`PricePrioritySelectorTest`)**: Validates the priority selection rule logic in total isolation without frameworks or mocks.
* **Application Layer (`GetApplicablePriceServiceTest`)**: Verifies service orchestration, input guards, exception throwing (`PriceNotFoundException`, `IllegalArgumentException`), and port invocations using **Mockito**.

### Executing Specific Test Suites

```bash
# Run REST-Assured integration tests
./mvnw test -Dtest=PriceControllerRestIntegrationTest

# Run MockMvc web slice tests
./mvnw test -Dtest=PriceControllerTest

# Run Service layer unit tests
./mvnw test -Dtest=GetApplicablePriceServiceTest

# Run Domain priority selector unit tests
./mvnw test -Dtest=PricePrioritySelectorTest
```

### 4. Running Postman Collection Tests

Postman collection containing the E2E test scenarios with automated assertion scripts is located at:

```
src/test/resources/postman/Altia backend test.postman_collection.json
```
**How to run**:

1. Start the application (./mvnw spring-boot:run).
2. Open Postman and click Import.
3. Select src/test/resources/postman/Altia backend test.postman_collection.json.
4. Run individual requests or execute the full suite using Postman Collection Runner against http://localhost:8080.