# Registro de Decisiones de Arquitectura

Este documento registra las decisiones de diseño tomadas durante el desarrollo
del proyecto, así como las decisiones provisionales que podrían cambiar si el
evaluador proporciona información adicional.

Las preguntas que requieren confirmación se mantienen en:

`docs/open-questions.md`

---

## Decisión 001 — Representación de pedidos en Customer

**Estado:** Decidido

### Pregunta

¿Cómo deben representarse los pedidos asociados a un cliente?

### Decisión

El cliente almacenará referencias a los pedidos mediante sus valores
`orderRef`.

### Razón

La evaluación indica que los usuarios están identificados mediante `userId`
y que el número de pedido está representado por `orderRef`.

Almacenar únicamente `orderRef` evita duplicar dentro del documento del cliente
la información completa del pedido.

### Alternativas consideradas

- Almacenar objetos completos de `Order`.
- Almacenar identificadores internos de pedidos.
- Almacenar únicamente los valores `orderRef`.

### Impacto

El modelo `Customer` mantiene una referencia ligera a los pedidos y no
mantiene una copia de la información completa de cada pedido.

---

## Decisión 002 — Representación de `estimateDeliveryDate`

**Estado:** Decidido

### Pregunta

¿Cómo debe representarse la fecha estimada de entrega dentro del modelo
interno?

### Decisión

La fecha estimada de entrega se representa mediante el campo
`estimateDeliveryDate` utilizando `LocalDate`.

### Razón

La evaluación incluye la fecha estimada de entrega como parte de los datos
del pedido.

No se requiere información de hora para esta fecha, por lo que `LocalDate`
representa adecuadamente el dato.

### Impacto

El modelo de dominio mantiene la fecha estimada de entrega como un concepto
independiente de `orderStatus`.

No se realiza una transformación automática entre `orderStatus` y
`estimateDeliveryDate`.

---

## Decisión 003 — Representación de `shippingAddress`

**Estado:** Provisional

### Pregunta

¿Debe `shippingAddress` permanecer como un `String` o convertirse en un
objeto estructurado?

### Decisión provisional

Mantener `shippingAddress` como `String`.

### Razón

La evaluación únicamente especifica "Dirección de envío" y no define una
estructura compuesta para este dato.

### Alternativas consideradas

- Mantener la dirección como `String`.
- Utilizar un objeto `Address` estructurado.

### Impacto

Si posteriormente se requiere una dirección estructurada, será necesario
modificar el modelo de dominio y posiblemente el contrato de la API.

La pregunta permanece registrada en `docs/open-questions.md`.

---

## Decisión 004 — Arquitectura Modular

**Estado:** Decidido

### Pregunta

¿Qué arquitectura utilizar para la aplicación?

### Decisión

Utilizar un **Monolito Modular con una implementación pragmática de Clean
Architecture**.

### Razón

El alcance del proyecto no justifica la complejidad operativa de múltiples
microservicios.

Al mismo tiempo, la evaluación indica que no se admite MVC y que se valoran
Clean Code, patrones de diseño y arquitectura avanzada.

La solución permite separar responsabilidades manteniendo un único
despliegue.

### Impacto

La aplicación se divide en módulos funcionales:

- Customer
- Order
- Delivery
- Item
- Search

Cada módulo mantiene separadas sus responsabilidades de dominio,
aplicación, infraestructura y presentación cuando corresponde.

---

## Decisión 005 — Uso de abstracciones para persistencia

**Estado:** Decidido

### Pregunta

¿Cómo evitar que los casos de uso dependan directamente de MongoDB?

### Decisión

Los casos de uso dependerán de abstracciones de repositorio definidas en la
capa de aplicación.

Las implementaciones concretas utilizarán Spring Data MongoDB dentro de
Infrastructure.

### Razón

Esto permite aplicar Dependency Inversion y mantener los casos de uso
independientes de los detalles de persistencia.

### Impacto

El flujo general de persistencia será:

```text
Use Case
   ↓
Repository abstraction
   ↓
Repository adapter
   ↓
Spring Data MongoDB
   ↓
MongoDB
```

---

## Decisión 006 — Separación entre dominio y persistencia

**Estado:** Decidido

### Pregunta

¿Deben utilizarse directamente los documentos de MongoDB como modelos de
dominio?

### Decisión

No.

Los modelos de dominio y los documentos de persistencia permanecerán
separados.

### Razón

Permite evitar que detalles específicos de MongoDB se propaguen hacia las
reglas de negocio.

### Impacto

Los adapters de persistencia son responsables de realizar las conversiones
entre:

- Domain model.
- MongoDB document.

---

## Decisión 007 — Separación de DTOs REST

**Estado:** Decidido

### Pregunta

¿Deben utilizarse directamente las entidades de dominio como contratos de
la API REST?

### Decisión

No.

Las APIs REST utilizan DTOs específicos para requests y responses.

### Razón

La representación HTTP no debe acoplarse directamente al modelo interno
de dominio.

### Impacto

Los controladores realizan las conversiones necesarias entre:

- Request DTO.
- Domain model.
- Response DTO.

---

## Decisión 008 — Normalización de búsqueda

**Estado:** Decidido

### Pregunta

¿Cómo implementar la búsqueda flexible solicitada por la evaluación?

### Decisión

La búsqueda normalizará el texto antes de realizar las comparaciones.

La normalización contempla:

- Conversión a minúsculas.
- Eliminación de acentos.
- Normalización de puntuación.
- Normalización de espacios.

### Razón

La evaluación solicita que la búsqueda sea flexible y que no considere
diferencias de comas, acentos, mayúsculas o errores mínimos de ortografía.

### Impacto

La lógica de búsqueda utiliza un componente específico de normalización
antes de realizar las comparaciones.

---

## Decisión 009 — Búsqueda con tolerancia a errores pequeños

**Estado:** Provisional

### Pregunta

¿Cómo soportar errores mínimos de ortografía en `displayName`?

### Decisión

Utilizar Levenshtein Distance como mecanismo de fuzzy matching para tolerar
pequeñas diferencias de escritura.

### Razón

La evaluación solicita que la consulta sea flexible y tolere errores mínimos
de ortografía.

### Impacto

La implementación de fuzzy matching queda encapsulada en un componente
específico de la funcionalidad de búsqueda.

La elección de Levenshtein puede modificarse posteriormente si el evaluador
define otra expectativa.

---

## Decisión 010 — Prioridad de filtros en Search

**Estado:** Provisional

### Pregunta

¿Cómo combinar los filtros de pedido con `displayName`?

### Decisión

Cuando existen filtros de pedido (`orderRef`, `orderStatus` o `storeName`)
y también `displayName`, primero se filtran los pedidos y posteriormente
se filtran los items asociados a esos pedidos.

### Razón

Esta estrategia mantiene una relación clara entre los filtros de pedido y
los productos pertenecientes a dichos pedidos.

### Comportamiento definido

#### Sin filtros

Se retornan los pedidos y sus items asociados.

#### Filtros de pedido

Se retornan los pedidos encontrados y sus items asociados.

#### Filtros de pedido + `displayName`

Se filtran primero los pedidos y posteriormente los items asociados.

#### Solo `displayName`

Se buscan primero los items coincidentes y posteriormente los pedidos
asociados.

### Impacto

La implementación de Search coordina información de los módulos Order e
Item.

El comportamiento de búsqueda únicamente por `displayName` permanece como
una decisión de diseño que puede confirmarse con el evaluador.

---

## Decisión 011 — `/pedidos` y `/items` como datos de referencia

**Estado:** Decidido

### Pregunta

¿La aplicación debe implementar una arquitectura de integración con las
URLs `/pedidos` y `/items` proporcionadas por la evaluación?

### Decisión

No se implementará una arquitectura específica de consumidores externos
alrededor de dichas URLs.

Los datos proporcionados por estos servicios se consideran datos de
referencia para definir y poblar los modelos internos de la aplicación.

### Razón

El objetivo principal de la implementación es proporcionar las APIs
solicitadas y persistir la información requerida.

Introducir adapters HTTP específicos para estos servicios agregaría
complejidad que no es necesaria para el alcance actual.

### Impacto

La aplicación mantiene sus propios modelos y persistencia interna para:

- Customer.
- Order.
- Delivery.
- Item.

---

## Decisión 012 — Fecha estimada de entrega

**Estado:** Decidido

### Pregunta

¿Qué tipo de dato utilizar para `estimateDeliveryDate`?

### Decisión

Utilizar `LocalDate`.

### Razón

La fecha estimada de entrega no requiere información de hora dentro del
alcance actual.

### Impacto

El formato utilizado por la API será:

`YYYY-MM-DD`

Ejemplo:

`2026-10-15`

---

## Decisión 013 — Optimización de búsqueda estructurada

**Estado:** Pendiente de implementación

### Pregunta

¿Debe la búsqueda de `orderRef`, `orderStatus` y `storeName` realizarse
directamente mediante filtros de MongoDB?

### Decisión actual

Mantener la implementación actual basada en la lógica existente y no realizar
esta optimización durante la etapa actual del proyecto.

Como mejora futura, los filtros estructurados podrán ejecutarse directamente
en MongoDB, mientras que la búsqueda fuzzy de `displayName` permanecerá
en la lógica de aplicación.

### Razón

La prioridad actual es completar y validar correctamente la funcionalidad
requerida antes de introducir optimizaciones.

### Impacto

La optimización queda identificada como una mejora futura y no forma parte
del comportamiento actual.

---

## Decisión 014 — Asociación de pedidos mediante actualización de Customer

**Estado:** Decidido

### Pregunta

¿Cómo se deben asociar los pedidos al cliente mediante el campo `orders`?

### Decisión

Los clientes se crean inicialmente sin pedidos asociados.

La asociación de pedidos se realiza mediante la actualización del cliente
utilizando `PUT /customers/{userId}`.

El campo `orders` contiene los valores `orderRef` de los pedidos asociados.

Antes de persistir la asociación, cada `orderRef` recibido debe corresponder
a un pedido cuyo `userId` coincida con el `userId` del cliente.

### Razón

La evaluación requiere que el cliente mantenga los pedidos asociados y que
la relación utilice el `userId` del cliente y el `orderRef` del pedido.

La asociación mediante el endpoint de actualización permite mantener el
CRUD de Customer y demostrar explícitamente el flujo de creación,
asociación y consulta de pedidos.

### Comportamiento definido

- Cliente nuevo: `orders` se inicializa como una lista vacía.
- `orders` omitido o `null` durante la actualización: se conservan las
  asociaciones existentes.
- `orders` como lista vacía: se eliminan las asociaciones existentes.
- `orders` con valores: se validan los `orderRef` contra los pedidos del
  mismo `userId` antes de persistirlos.
- Si algún `orderRef` no pertenece al cliente, la actualización se rechaza
  con `400 Bad Request`.

### Impacto

El caso de uso de actualización de Customer depende de la abstracción
`OrderRepository` para validar la pertenencia de los pedidos.

Esto mantiene la regla de asociación dentro de la capa de aplicación y
evita que el controlador implemente lógica de negocio.