# General Architecture

## 1. Objective

The application is implemented as a Modular Monolith using pragmatic
Clean Architecture principles.

The objective is to keep business rules separated from infrastructure
and presentation concerns, maintain clear module boundaries and make the
application easy to test and evolve.

The solution is maintained as a single deployable application because
of the scope of the project. Splitting the application into multiple
microservices is not considered necessary.

The architecture also avoids the MVC pattern, as required by the
technical assessment.

---

## 2. Architecture Style

The solution combines two main concepts:

### Modular Monolith

The application is maintained as a single project and deployment, but
is internally divided into modules based on the main capabilities of
the system:

* Customer
* Order
* Delivery
* Item
* Search

Each module has its own responsibilities and internal organization.

This provides separation of concerns without introducing the operational
complexity of multiple independent services.

### Pragmatic Clean Architecture

The application separates:

* Domain models and business rules.
* Application use cases.
* REST presentation.
* Infrastructure and persistence details.

The goal is not to introduce abstractions only for architectural
purposes. Each layer and abstraction should provide a concrete
responsibility.

---

## 3. Modules

The application is organized into the following modules:

```text
com.liverpool.appsales.exam
├── customer
│   ├── domain
│   ├── application
│   ├── infrastructure
│   └── presentation
├── order
│   ├── domain
│   ├── application
│   ├── infrastructure
│   └── presentation
├── delivery
│   ├── domain
│   ├── application
│   ├── infrastructure
│   └── presentation
├── item
│   ├── domain
│   ├── application
│   └── infrastructure
├── search
│   ├── domain
│   ├── application
│   └── presentation
└── presentation
    └── GlobalExceptionHandler
```

### Customer

Responsible for customer information and customer-related operations.

The customer is identified by `userId`.

The customer also maintains a list of associated order references through
the `orders` field. The stored values represent the `orderRef` of the
associated orders.

### Order

Responsible for representing and managing order information.

An order contains:

* `orderRef`
* `userId`
* `canal`
* `orderStatus`
* `storeName`
* `estimateDeliveryDate`
* Associated items

### Delivery

Responsible for managing delivery information associated with an order.

The delivery currently contains:

* `deliveryId`
* `orderRef`
* `shippingAddress`

The shipping address is currently represented as a String.

### Item

Responsible for representing product information associated with an
order.

The item information includes:

* `itemId`
* `skuId`
* `quantity`
* `displayName`
* `deliveryStatus`
* `id`

The item module is used by the search functionality to retrieve and
filter product information.

### Search

Responsible for the search functionality required by the assessment.

It coordinates order and item information to support:

* Order filtering by `orderRef`.
* Order filtering by `orderStatus`.
* Order filtering by `storeName`.
* Item search by `displayName`.
* Flexible text matching.

---

## 4. Layers

Each module is organized according to the responsibilities required by
the application.

### Domain

Contains domain models and business concepts.

The domain layer does not depend directly on MongoDB, REST controllers
or persistence-specific implementations.

### Application

Contains application use cases and abstractions required by those use
cases.

Examples include:

* Create customer.
* Retrieve customer.
* Update customer.
* Create order.
* Retrieve order.
* Update order.
* Create delivery.
* Retrieve delivery.
* Update delivery.
* Search orders and items.

Repository abstractions are defined here when they are required by the
use cases.

### Infrastructure

Contains technical implementations required by the application.

The current infrastructure includes:

* MongoDB persistence.
* Spring Data MongoDB repositories.
* Persistence adapters that map MongoDB documents to domain models.

Infrastructure details are kept outside the domain and use-case logic.

### Presentation

Contains the REST controllers and HTTP-related DTOs.

Controllers are responsible for:

* Receiving HTTP requests.
* Validating request data.
* Calling application use cases.
* Mapping domain results to response DTOs.

Business logic is not implemented inside controllers.

---

## 5. Dependency Direction

Dependencies are directed toward the application and domain rules.

Conceptually:

```text
Presentation
      │
      ▼
Application
      │
      ▼
Domain
```

Infrastructure provides concrete implementations of the abstractions
required by the application.

Conceptually:

```text
┌────────────────┐
│  Application   │
│  abstractions  │
└───────▲────────┘
        │
        │ implements
        │
┌───────┴────────┐
│ Infrastructure │
└────────────────┘
```

The domain does not depend directly on MongoDB or other infrastructure
details.

This structure follows the Dependency Inversion Principle without
introducing a formal Ports and Adapters architecture where it is not
necessary for the scope of the project.

---

## 6. Persistence

MongoDB is used as the persistence technology required by the
assessment.

Customer, Order, Delivery and Item information is persisted using
MongoDB collections.

The application separates domain models from MongoDB persistence
documents.

Conceptually:

```text
Application Use Case
       │
       ▼
Repository Abstraction
       │
       ▼
Repository Adapter
       │
       ▼
Spring Data MongoDB
       │
       ▼
     MongoDB
```

The repository adapter is responsible for mapping between the domain
model and the MongoDB document.

This prevents persistence-specific details from leaking into the domain.

---

## 7. Reference Data: /pedidos and /items

The technical assessment provides `/pedidos` and `/items` as reference
services/data used to obtain order and product information.

The current application does not introduce a dedicated external-service
adapter architecture around these URLs.

Instead, the provided data is used as reference data for defining and
seeding the application's internal domain and persistence models.

This keeps the implementation focused on the required REST APIs and
avoids introducing unnecessary coupling or infrastructure complexity.

The relationship between orders and items is represented internally
through the `itemId` values contained in an order.

---

## 8. Model Separation

The application keeps different models for different responsibilities:

* Domain models.
* MongoDB persistence documents.
* REST request DTOs.
* REST response DTOs.

Persistence documents are not exposed directly through the REST API.

Similarly, REST request and response DTOs are not used as domain models.

This separation prevents changes in persistence or HTTP representation
from directly affecting the domain model.

---

## 9. Search Architecture

The search functionality coordinates information from the Order and Item
modules.

The search process gives priority to structured order filters:

* `orderRef`
* `orderStatus`
* `storeName`

When these filters are present, orders are filtered first.

If `displayName` is also provided, the items associated with the
resulting orders are then filtered using the item display name.

When `displayName` is provided without order filters, the search can
start from the matching items and then retrieve the associated orders.

Text normalization includes:

* Lowercase conversion.
* Accent removal.
* Punctuation normalization.
* Whitespace normalization.

Fuzzy matching is used to tolerate small spelling differences.

The current implementation prioritizes correctness and clarity.
Database-level optimization of structured filters is considered a future
improvement.

---

## 10. Design Decisions

Architecture and domain decisions are documented in:

`docs/decisions.md`

Examples include:

* Customer stores order references using `orderRef`.
* `shippingAddress` is currently represented as a String.
* `estimateDeliveryDate` is represented as `LocalDate`.
* `/pedidos` and `/items` are treated as reference data rather than
  introducing external-service adapters.
* Search gives priority to structured order filters before filtering
  items by `displayName`.

Open questions that require confirmation from the evaluator are
documented separately in:

`docs/open-questions.md`

---

## 11. Design Principles

The implementation follows the following principles:

* Single Responsibility Principle.
* Dependency Inversion Principle.
* Separation of concerns.
* Dependency on abstractions where useful.
* Low coupling between modules.
* High cohesion within modules.
* Business logic outside controllers.
* Domain models independent from infrastructure.
* Explicit module responsibilities.
* Avoid unnecessary abstractions.
* Avoid shared components without a real cross-cutting responsibility.
* Prefer simple solutions appropriate to the scope of the project.

The architecture is not intended to be a purely academic structure.
Every layer and abstraction should provide a concrete responsibility to
the application.