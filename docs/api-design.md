```md
# API Design

## Overview

The application exposes REST APIs for customer, order and delivery
management, as well as a search endpoint for orders and items.

The API is implemented using Spring Boot and documented through
OpenAPI/Swagger.

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

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
  "canal": "WEB",
  "orderStatus": "DELIVERED",
  "storeName": "Liverpool Galerías",
  "estimateDeliveryDate": "2026-10-15",
  "items": [
    {
      "itemId": "3010091676-1132351437",
      "skuId": "1132351437",
      "quantity": 1
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

Supported parameters:

| Parameter | Description |
|---|---|
| `orderRef` | Filters orders by order reference |
| `orderStatus` | Filters orders by status |
| `storeName` | Filters orders by store name |
| `displayName` | Searches items by display name |

#### Search by order reference

```text
GET /search?orderRef=3010091676
```

#### Search by order status

```text
GET /search?orderStatus=DELIVERED
```

#### Search by store name

```text
GET /search?storeName=Liverpool
```

#### Search by item display name

```text
GET /search?displayName=Pantalon
```

The item search applies text normalization to support differences
in capitalization, accents and punctuation, together with flexible
text matching.

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
```