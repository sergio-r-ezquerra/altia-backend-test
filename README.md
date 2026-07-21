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
* **Comprehensive Test Coverage:** Features End-to-End (E2E) integration test suites validating the 5 explicit date-priority scenarios detailed in the requirement specs.
* **OpenAPI 3 / Swagger Integration:** Interactive API documentation fully accessible natively through the UI.
* **Docker Ready:** Multistage ultra-lightweight container builds using Alpine JRE mirrors.

---

## 🛠️ Tech Stack & Key Dependencies

This project relies on the following core dependencies configured in the `pom.xml`:

* **Spring Boot (Starter Parent):** Provides the foundational framework for building the microservice.
  * `spring-boot-starter-webmvc`: Standard dependency for exposing REST endpoints and managing web request/response layers.
  * `spring-boot-starter-data-jpa`: Handles JPA configurations, Hibernate ORM integration, and transactional database mapping.
  * `spring-boot-starter-validation`: Used for structural validation of incoming request query parameters and data contracts.
* **H2 Database:** In-memory relational database used for local execution, seeding sample scheduling records, and fast test verification.
  * `spring-boot-h2console`: Auto-configures and exposes the web interface console for debugging data.
* **Lombok:** Reduces verbose boilerplate code (e.g. getters, setters, constructors, builders) via annotation processors.
* **Springdoc OpenAPI (v3.0.3):** Integrates Swagger UI to render interactive api testing pages.
* **Testing Libraries:**
  * `spring-boot-starter-test`: Standard dependency bundling JUnit 5, Mockito, AssertJ, and JSONPath utilities.
  * `spring-boot-starter-webmvc-test`: Provides auto-configuration for Spring MVC slicing and MockMvc test utilities.

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
GET /api/v1/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

---

## 🧪 Automated Test Scenarios

The suite includes integration tests verifying the exact 5 target use cases defined in the specification protocol:

| Test Case | Date / Time | Product ID | Brand ID | Expected Price List | Target Price | Reason / Behavior |
|:---:|:---:|:---:|:---:|:---:|:---:|:---|
| **Test 1** | June 14, 10:00 | 35455 | 1 (ZARA) | **1** | **35.50 EUR** | Default fallback base price. |
| **Test 2** | June 14, 16:00 | 35455 | 1 (ZARA) | **2** | **25.45 EUR** | Overruled by higher priority logic (Priority 1 > 0). |
| **Test 3** | June 14, 21:00 | 35455 | 1 (ZARA) | **1** | **35.50 EUR** | Specific timeframe expired; reverts back to base tariff. |
| **Test 4** | June 15, 10:00 | 35455 | 1 (ZARA) | **3** | **30.50 EUR** | New overlapping timeframe active with Priority 1. |
| **Test 5** | June 16, 21:00 | 35455 | 1 (ZARA) | **4** | **38.95 EUR** | Final promotion period active matching highest index priority. |

To trigger the test execution safely via the command line interface, run:
```bash
./mvnw test
```

---

## 🔬 Unit Tests — Domain & Business Layer

Unit tests are divided into two main test suites:

1. **Domain Layer (`PricePrioritySelectorTest`)**: Validates the priority selection business logic in isolation using pure JUnit tests without any mocks or frameworks.
2. **Business / Application Layer (`GetApplicablePriceServiceTest`)**: Verifies the orchestrator logic, input validation, and proper integration with ports. It is isolated from the database and web layer using **Mockito** to mock the repository port.

To run the unit test suites:
```bash
# Run all tests
./mvnw test

# Run a specific unit test class
./mvnw test -Dtest=PricePrioritySelectorTest
./mvnw test -Dtest=GetApplicablePriceServiceTest
```

### Test coverage breakdown

#### ✅ Happy Path

| Test | Description |
|:---|:---|
| `testGetApplicablePriceSuccess` | Returns the correct `Price` when the repository finds a match, and verifies the repository is called exactly once. |
| `testGetApplicablePriceReturnsAllFields` | Asserts that all domain fields (`brandId`, `productId`, `priceList`, `priority`, `price`, `curr`, `startDate`, `endDate`) are propagated from the repository result without any silent data loss. |

#### ❌ Price Not Found

| Test | Description |
|:---|:---|
| `testGetApplicablePriceNotFound` | Verifies that `PriceNotFoundException` is thrown when the repository returns an empty `Optional`. |
| `testGetApplicablePriceNotFoundExceptionMessage` | Asserts the exact exception message format: `"Price not found for product {id}, brand {id} at date {date}"`. |

#### 🚫 Input Validation — Null Fields (message assertion)

| Test | Description |
|:---|:---|
| `testGetApplicablePriceWithNullProductId` | Throws `IllegalArgumentException` with message `"Product ID and Brand ID must not be null"` when `productId` is `null`. |
| `testGetApplicablePriceWithNullBrandId` | Same exception and message when `brandId` is `null`. |
| `testGetApplicablePriceWithNullApplicationDate` | Throws `IllegalArgumentException` with message `"Application date must not be null"` when `applicationDate` is `null`. |

#### 🚫 Input Validation — Repository not invoked on null inputs

| Test | Description |
|:---|:---|
| `testGetApplicablePriceNullProductIdDoesNotInvokeRepository` | Guarantees the repository is **never called** when `productId` is `null` (fail-fast guard). |
| `testGetApplicablePriceNullBrandIdDoesNotInvokeRepository` | Same guarantee for `brandId` null. |
| `testGetApplicablePriceNullDateDoesNotInvokeRepository` | Same guarantee for `applicationDate` null. |

#### 🔀 Request Variations (toBuilder copy)

| Test | Description |
|:---|:---|
| `testGetApplicablePriceWithDifferentProduct` | A request with a different `productId` is correctly forwarded to the repository; `PriceNotFoundException` is thrown when no match is found. |
| `testGetApplicablePriceWithDifferentBrand` | Same flow with a different `brandId`. |
| `testGetApplicablePriceWithDifferentDate` | Same flow with a different `applicationDate`. |
