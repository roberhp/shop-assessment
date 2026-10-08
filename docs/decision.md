# Registro de Decisiones de Arquitectura

Este documento registra las decisiones de diseño tomadas durante el desarrollo
del proyecto, así como las preguntas que permanecen abiertas por falta de
definición explícita en la evaluación.

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

### Impacto

El modelo `Customer` mantiene una referencia ligera a los pedidos y no
mantiene una copia de la información completa de cada pedido.

---

## Decisión 002 — Interpretación de OrderStatus

**Estado:** Provisional

### Pregunta

¿Cómo debe representarse internamente el campo externo `orderStatus`?

### Decisión

A nivel de dominio se propone representar este dato mediante el concepto
`estimateDeliveryDate`, de acuerdo con la descripción funcional de la
evaluación.

### Razón

La evaluación describe el dato del pedido como:

`Estatus de pedido (Fecha estimada de entrega)`

Por lo tanto, el modelo de dominio utilizará el concepto de fecha estimada
de entrega en lugar de acoplarse directamente al nombre utilizado por el
servicio externo.

### Validación pendiente

Antes de definir el tipo de dato definitivo, se debe verificar el valor real
proporcionado por el endpoint externo `/pedidos`.

### Alternativas consideradas

- Mantener `orderStatus` con el mismo nombre y significado del servicio
  externo.
- Transformarlo al concepto de dominio `estimateDeliveryDate`.

### Impacto

El adaptador responsable de consumir `/pedidos` será responsable de transformar
el modelo externo al modelo utilizado por la aplicación.

### Confirmación requerida

Sí.

---

## Decisión 003 — Estructura de Dirección de Envío

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

### Confirmación requerida

Sí.

---

## Pregunta Abierta 001 — Estructura de Respuesta de Búsqueda

**Estado:** Abierta

### Pregunta

¿Cuál debe ser la estructura exacta de respuesta de las APIs de búsqueda?

### Propuesta actual

Definir endpoints específicos para la búsqueda de pedidos y productos,
manteniendo pendiente la definición del contrato exacto de respuesta hasta
contar con una definición adicional.

### Por qué importa

La estructura de respuesta afecta:

- El contrato de la API.
- Los DTOs de respuesta.
- La implementación de los casos de uso.
- Las pruebas de los controladores.

### Confirmación requerida

Sí.

---

## Pregunta Abierta 002 — Operaciones CRUD de Order y Delivery

**Estado:** Abierta

### Pregunta

¿Qué operaciones exactas deben exponerse para los datos de `Order` y
`Delivery`?

### Contexto

La evaluación menciona inicialmente tres APIs CRUD para información de
cliente, entrega y pedido, pero posteriormente especifica explícitamente las
operaciones de creación, consulta y actualización para Customer.

No se define con el mismo nivel de detalle el contrato de operaciones para
Order y Delivery.

### Propuesta actual

Implementar únicamente las operaciones que puedan justificarse a partir de
los requerimientos detallados, evitando crear endpoints de actualización o
eliminación de pedidos sin una necesidad explícita.

### Confirmación requerida

Sí, si el evaluador espera operaciones CRUD completas para Order y Delivery.