```md
# Liverpool Order Management API

Backend application developed as part of the Liverpool Backend Technical Assessment.

The application provides REST APIs for customer, order and delivery
management, together with search functionality for orders and items.

## Technologies

- Java 17
- Spring Boot
- Spring Web
- Spring Data MongoDB
- MongoDB
- Maven
- OpenAPI / Swagger
- JUnit
- Mockito
- CheckStyle

## Architecture

The application follows a Modular Monolith architecture with a pragmatic
Clean Architecture approach.

The main modules are:

- Customer
- Order
- Delivery
- Item
- Search

Each main module is organized into layers for domain, application,
infrastructure and presentation according to its responsibilities.

The main dependency flow is:

Presentation → Application → Domain

Infrastructure provides the technical implementations required by the
application layer.

Additional architecture details are available in:

[Architecture](docs/architecture.md)

## API Documentation

The REST API is documented using OpenAPI/Swagger.

Once the application is running, Swagger UI is available at:

`http://localhost:8080/swagger-ui/index.html`

Detailed API information is available in:

[API Design](docs/api-design.md)

## API Endpoints

### Customers

- `POST /customers` - Create a customer
- `GET /customers/{userId}` - Get a customer
- `PUT /customers/{userId}` - Update a customer

### Orders

- `POST /orders` - Create an order
- `GET /orders/{orderRef}` - Get an order
- `PUT /orders/{orderRef}` - Update an order

### Deliveries

- `POST /deliveries` - Create delivery information
- `GET /deliveries/{deliveryId}` - Get delivery information
- `PUT /deliveries/{deliveryId}` - Update delivery information

### Search

- `GET /search` - Search orders and items using optional search criteria

## Requirements

- Java 17 or later
- Maven
- MongoDB

## Running the Application

Configure the MongoDB connection in the application configuration.

Then run:

```bash
./mvnw spring-boot:run
```

The application will be available at:

`http://localhost:8080`

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

## Running Tests

To execute the unit and controller tests:

```bash
./mvnw test
```

To execute the complete Maven verification lifecycle, including CheckStyle:

```bash
./mvnw verify
```

## Project Documentation

Additional project documentation is available in the `docs` directory:

- [API Design](docs/api-design.md)
- [Architecture](docs/architecture.md)
- [Testing](docs/testing.md)
- [Architecture & Decision Log](docs/decisions.md)
- [Open Questions](docs/open-questions.md)

## Development Notes

The project uses CheckStyle to enforce code style rules.

Before submitting changes, run:

```bash
./mvnw verify
```

All tests should pass and no CheckStyle violations should be reported.

## License

This project was developed as part of a technical assessment.
