# API Design

## Overview

The application exposes REST APIs for customer, order and delivery
management, as well as a search endpoint for orders and items.

The API is implemented using Spring Boot and documented through
OpenAPI/Swagger.

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

The API documentation exposed through Swagger is written in Spanish.

---

## Customers

### POST /customers

Creates a new customer.

#### Request

```json
{
  "userId": "75c97531-abf5-4524-8107-90aa48d08efc",
  "firstName": "Juan",
  "paternalLastName": "Pérez",
  "maternalLastName": "García",
  "email": "juan.perez@example.com"
}
```

#### Response

Returns the created customer.

---

### GET /customers/{userId}

Retrieves a customer using its `userId`.

Example:

```text
GET /customers/75c97531-abf5-4524-8107-90aa48d08efc
```

---

### PUT /customers/{userId}

Updates the personal information of an existing customer.

The customer's associated order references are preserved during the update.

---

## Orders

### POST /orders

Creates a new order with its associated items.

#### Request

Example:

```json
{
  "orderRef": "3010091676",
  "userId": "75c97531-abf5-4524-8107-90aa48d08efc",
  "canal": "online",
  "orderStatus": "2025-12-06",
  "storeName": "L  SANTA FE",
  "estimateDeliveryDate": "2026-10-15",
  "items": [
    {
      "itemId": "3010091676-1132351437",
      "skuId": "1132351437",
      "quantity": 3
    }
  ]
}
```

#### Response

Returns the created order.

---

### GET /orders/{orderRef}

Retrieves an order using its `orderRef`.

Example:

```text
GET /orders/3010091676
```

---

### PUT /orders/{orderRef}

Updates an existing order.

---

## Deliveries

### POST /deliveries

Creates delivery information associated with an order.

#### Request

Example:

```json
{
  "orderRef": "3010091676",
  "shippingAddress": "Av. Insurgentes Sur 1234, CDMX"
}
```

#### Response

Returns the created delivery information.

---

### GET /deliveries/{deliveryId}

Retrieves delivery information using its `deliveryId`.

Example:

```text
GET /deliveries/550e8400-e29b-41d4-a716-446655440000
```

---

### PUT /deliveries/{deliveryId}

Updates delivery information.

---

## Search

### GET /search

Searches orders and items using optional search criteria.

The search supports the following parameters:

| Parameter | Description |
|---|---|
| `orderRef` | Filters orders by order reference |
| `orderStatus` | Filters orders by order status value |
| `storeName` | Filters orders by store name |
| `displayName` | Searches items by display name |

### Search behavior

The search gives priority to the order-related filters.

#### Case 1: No filters

When no search parameters are provided, the API returns all orders and
their associated items.

#### Case 2: Order filters only

When one or more of `orderRef`, `orderStatus` or `storeName` are
provided, the API first filters the orders.

The response contains the matching orders and their associated items.

#### Case 3: Order filters and displayName

When order filters and `displayName` are provided, the API first filters
the orders and then filters the items associated with those orders using
`displayName`.

#### Case 4: displayName only

When only `displayName` is provided, the API searches for matching items
and then retrieves the orders associated with those items.

The behavior of this case is a design decision and is documented in
`docs/decisions.md`.

### Search examples

#### Search by order reference

```text
GET /search?orderRef=3010091676
```

#### Search by order status

The provided reference data contains date-like values for `orderStatus`.

Example:

```text
GET /search?orderStatus=2025-12-06
```

#### Search by store name

```text
GET /search?storeName=Liverpool
```

#### Search by item display name

```text
GET /search?displayName=Pantalon
```

### Flexible item search

The item search normalizes text before matching.

The normalization considers:

* Uppercase and lowercase differences.
* Accents.
* Punctuation and commas.
* Extra whitespace.

The implementation also supports small spelling differences through
fuzzy text matching.

---

## Error Responses

The API uses the following HTTP status codes:

| Status | Meaning |
|---|---|
| `200` | Request completed successfully |
| `201` | Resource created successfully |
| `400` | Invalid request or validation error |
| `404` | Requested resource was not found |
| `409` | Resource already exists |

The exact error response is handled centrally by the application's
global exception handler.