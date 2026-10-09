# Diseño de API

## Visión general

La aplicación expone APIs REST para la gestión de:

- Clientes.
- Pedidos.
- Entregas.
- Búsqueda de pedidos y productos.

La API está implementada utilizando Spring Boot y documentada mediante
OpenAPI/Swagger.

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

La documentación visible mediante Swagger está escrita en español.

---

## Clientes

### POST /customers

Crea un nuevo cliente.

#### Request

ññjson
{
  "userId": "75c97531-abf5-4524-8107-90aa48d08efc",
  "firstName": "Juan",
  "paternalLastName": "Pérez",
  "maternalLastName": "García",
  "email": "juan.perez@example.com"
}
ññ

La creación inicial del cliente no requiere pedidos asociados.

#### Response

Retorna el cliente creado.

---

### GET /customers/{userId}

Consulta un cliente utilizando su `userId`.

Ejemplo:

ññtext
GET /customers/75c97531-abf5-4524-8107-90aa48d08efc
ññ

#### Response

Retorna la información del cliente, incluyendo las referencias
`orderRef` de los pedidos asociados.

---

### PUT /customers/{userId}

Actualiza la información de un cliente existente.

La operación permite actualizar:

- Nombre.
- Apellido paterno.
- Apellido materno.
- Correo electrónico.
- Referencias de pedidos asociadas mediante `orderRef`.

Antes de asociar un pedido se valida que el `orderRef` corresponda al mismo
`userId` del cliente.

Ejemplo:

ññjson
{
  "firstName": "Juan",
  "paternalLastName": "Pérez",
  "maternalLastName": "García",
  "email": "juan.perez@example.com",
  "orders": [
    "3010091676",
    "30100916760987"
  ]
}
ññ

#### Response

Retorna el cliente actualizado.

---

### DELETE /customers/{userId}

Elimina un cliente existente utilizando su `userId`.

Ejemplo:

ññtext
DELETE /customers/75c97531-abf5-4524-8107-90aa48d08efc
ññ

---

## Pedidos

### POST /orders

Crea un nuevo pedido con sus productos asociados.

#### Request

Ejemplo:

ññjson
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
ññ

#### Response

Retorna el pedido creado.

---

### GET /orders/{orderRef}

Consulta un pedido utilizando su `orderRef`.

Ejemplo:

ññtext
GET /orders/3010091676
ññ

---

### PUT /orders/{orderRef}

Actualiza un pedido existente.

Ejemplo:

ññtext
PUT /orders/3010091676
ññ

#### Response

Retorna el pedido actualizado.

---

### DELETE /orders/{orderRef}

Elimina un pedido existente utilizando su `orderRef`.

Ejemplo:

ññtext
DELETE /orders/3010091676
ññ

---

## Entregas

### POST /deliveries

Crea información de entrega asociada a un pedido.

#### Request

Ejemplo:

ññjson
{
  "orderRef": "3010091676",
  "shippingAddress": "Av. Insurgentes Sur 1234, CDMX"
}
ññ

#### Response

Retorna la información de entrega creada.

---

### GET /deliveries/{deliveryId}

Consulta información de entrega utilizando su `deliveryId`.

Ejemplo:

ññtext
GET /deliveries/550e8400-e29b-41d4-a716-446655440000
ññ

---

### PUT /deliveries/{deliveryId}

Actualiza información de entrega existente.

Ejemplo:

ññtext
PUT /deliveries/550e8400-e29b-41d4-a716-446655440000
ññ

#### Response

Retorna la información de entrega actualizada.

---

### DELETE /deliveries/{deliveryId}

Elimina información de entrega existente.

Ejemplo:

ññtext
DELETE /deliveries/550e8400-e29b-41d4-a716-446655440000
ññ

---

## Búsqueda

### GET /search

Realiza búsquedas sobre pedidos y productos utilizando criterios
opcionales.

Los parámetros disponibles son:

| Parámetro | Descripción |
|---|---|
| `orderRef` | Filtra pedidos por referencia de pedido |
| `orderStatus` | Filtra pedidos por valor de estatus |
| `storeName` | Filtra pedidos por nombre de tienda |
| `displayName` | Busca productos por nombre |

---

## Comportamiento de búsqueda

La búsqueda establece una prioridad entre los filtros relacionados con
pedidos y la búsqueda por `displayName`.

### Caso 1: Sin filtros

Cuando no se proporciona ningún parámetro de búsqueda, la API retorna los
pedidos y sus productos asociados.

### Caso 2: Solo filtros de pedido

Cuando se proporciona uno o más de los siguientes parámetros:

- `orderRef`
- `orderStatus`
- `storeName`

primero se filtran los pedidos.

La respuesta contiene los pedidos encontrados y sus productos asociados.

### Caso 3: Filtros de pedido y `displayName`

Cuando se combinan filtros de pedido con `displayName`, primero se filtran
los pedidos.

Posteriormente se filtran los productos asociados a dichos pedidos
utilizando `displayName`.

### Caso 4: Solo `displayName`

Cuando únicamente se proporciona `displayName`, primero se buscan los
productos que coinciden con el texto.

Posteriormente se identifican los pedidos asociados a dichos productos.

Este comportamiento es una decisión de diseño y se encuentra documentado en:

`docs/decisions.md`

---

## Ejemplos de búsqueda

### Búsqueda por referencia de pedido

ññtext
GET /search?orderRef=3010091676
ññ

### Búsqueda por estatus de pedido

Los datos de referencia proporcionados por la evaluación contienen valores
con formato de fecha para `orderStatus`.

Ejemplo:

ññtext
GET /search?orderStatus=2025-12-06
ññ

### Búsqueda por tienda

ññtext
GET /search?storeName=Liverpool
ññ

### Búsqueda por nombre de producto

ññtext
GET /search?displayName=Pantalon
ññ

---

## Búsqueda flexible de productos

La búsqueda de `displayName` normaliza el texto antes de realizar las
comparaciones.

La normalización contempla:

- Diferencias entre mayúsculas y minúsculas.
- Acentos.
- Puntuación.
- Comas.
- Espacios adicionales.

Además, la implementación permite tolerar pequeñas diferencias de
ortografía mediante fuzzy matching basado en Levenshtein Distance.

Por ejemplo, una consulta como:

ññtext
GET /search?displayName=Pantalon
ññ

puede coincidir con un producto cuyo nombre contenga:

ññtext
Pantalón Levi's
ññ

---

## Datos de referencia

La evaluación proporciona datos mediante las referencias:

- `/pedidos`
- `/items`

Estos datos se utilizan como referencia para definir y poblar los modelos
internos de la aplicación.

La API de esta aplicación no actúa como consumidor de esos servicios
externos.

Los recursos utilizados por la aplicación se mantienen en su propia
persistencia.

---

## Respuestas de error

La API utiliza los siguientes códigos HTTP:

| Código | Significado |
|---|---|
| `200` | Solicitud procesada correctamente |
| `201` | Recurso creado correctamente |
| `400` | Solicitud inválida o error de validación |
| `404` | Recurso solicitado no encontrado |
| `409` | El recurso ya existe |

Los errores se manejan de manera centralizada mediante el
`GlobalExceptionHandler`.

Esto permite mantener una estructura consistente para los errores
producidos por los distintos módulos.

---

## Validación

Los requests utilizan validaciones mediante Jakarta Validation.

Entre las validaciones utilizadas se encuentran:

- Campos obligatorios.
- Formato de correo electrónico.
- Restricciones específicas de los requests.

Cuando una validación falla, la API retorna:

`400 Bad Request`

junto con los detalles correspondientes de la validación.

---

## Documentación OpenAPI

La API se documenta mediante OpenAPI/Swagger.

La interfaz está disponible en:

`http://localhost:8080/swagger-ui/index.html`

La documentación permite consultar:

- Endpoints.
- Parámetros.
- Requests.
- Responses.
- Códigos HTTP.
- Modelos utilizados por la API.