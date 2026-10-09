# Open Questions

Este documento contiene las preguntas identificadas durante el análisis y
desarrollo del proyecto que requieren confirmación sobre reglas de negocio
o comportamiento esperado.

Las decisiones que ya fueron tomadas durante la implementación se mantienen
en:

`docs/decisions.md`

Las preguntas de este documento representan ambigüedades que podrían requerir
confirmación del evaluador.

---

## OQ-001 — Delivery por pedido o por item

**Status:** Open

**Question:**

¿La dirección de entrega debe asociarse al pedido completo o puede existir
una dirección de entrega diferente para cada producto/item dentro de una
misma orden?

**Current proposal:**

Inicialmente, cada Delivery se asociará únicamente a un `orderRef` y
contendrá una sola dirección de envío (`shippingAddress`).

**Impact:**

Si se permite una dirección diferente por item, Delivery deberá relacionarse
también con `itemId`.

**Confirmation required:** Yes

---

## OQ-002 — Alcance de las operaciones CRUD

**Status:** Open

**Question:**

El enunciado indica que se requieren 3 APIs CRUD. ¿El comportamiento esperado
para cada recurso debe mantenerse como un CRUD completo, o existen
restricciones adicionales sobre las operaciones permitidas para Customer,
Order y Delivery?

**Current implementation:**

Se implementaron operaciones Create, Read, Update y Delete para los recursos
principales.

Para Customer, además de las operaciones CRUD, la actualización permite
asociar pedidos mediante `orderRef`.

**Impact:**

Si el evaluador define restricciones adicionales sobre alguna operación,
será necesario ajustar los endpoints correspondientes.

**Confirmation required:** Yes

---

## OQ-003 — Creación de Delivery

**Status:** Open

**Question:**

¿Todo pedido debe tener obligatoriamente un registro de Delivery, o la
información de entrega puede crearse posteriormente y existir temporalmente
un pedido sin una dirección asociada?

**Current proposal:**

Permitir que Delivery sea administrado de forma independiente y asociado a
un `orderRef`.

**Impact:**

Determina las reglas de validación y la relación entre Order y Delivery.

**Confirmation required:** Yes

---

## OQ-004 — Significado de `orderStatus`

**Status:** Open

**Question:**

En los datos proporcionados por `/pedidos`, ¿qué representa exactamente
el campo `orderStatus`?

Los valores de referencia observados tienen formato de fecha, por ejemplo:

`2025-12-06`

Sin embargo, el nombre del campo sugiere que podría representar el estado
del pedido.

**Current proposal:**

Mantener `orderStatus` como un campo independiente dentro de Order y
representarlo actualmente como `String`.

Mantener `estimateDeliveryDate` como un campo separado de tipo `LocalDate`.

No realizar una transformación automática entre ambos campos.

**Impact:**

Determina el significado definitivo de `orderStatus` y evita realizar una
transformación incorrecta entre `orderStatus` y `estimateDeliveryDate`.

**Confirmation required:** Yes

---

## OQ-005 — Regla de `itemId`

**Status:** Open

**Question:**

¿Debemos considerar `itemId` como un identificador funcional compuesto por
`orderRef-skuId`, o debe tratarse únicamente como un identificador recibido
desde la fuente de datos?

**Current proposal:**

Persistir `itemId` explícitamente dentro de `OrderItem`, junto con `skuId` y
`quantity`.

Actualmente se observa que:

`itemId = orderRef-skuId`

pero no se impondrá todavía una validación que reconstruya el valor
automáticamente.

**Impact:**

Determina si la aplicación debe validar la composición de `itemId` o
simplemente conservar el valor recibido.

**Confirmation required:** Yes

---

## OQ-006 — Identificador incremental de Item

**Status:** Open

**Question:**

¿El `id` incremental de `/items` tiene alguna función dentro de la lógica
del sistema o solamente identifica técnicamente cada registro?

**Current proposal:**

Considerarlo inicialmente como un identificador técnico independiente de
`itemId` y `skuId`.

**Impact:**

Determina si debe formar parte del modelo de negocio o únicamente del
modelo de persistencia o referencia.

**Confirmation required:** Yes

---

## OQ-007 — Una o múltiples direcciones de entrega

**Status:** Open

**Question:**

Si un pedido contiene múltiples items, ¿todos los items deben compartir la
misma dirección de entrega?

**Current proposal:**

Inicialmente asumir una única dirección de entrega por pedido.

Esta pregunta está relacionada con OQ-001.

**Impact:**

Si los items pueden tener diferentes destinos, será necesario asociar
Delivery también con `itemId`.

**Confirmation required:** Yes

---

## OQ-008 — Sincronización de `Customer.orders`

**Status:** Resolved by Design

**Question:**

Cuando se crea o modifica un pedido, ¿el campo `orders` del Customer debe
actualizarse automáticamente para reflejar los `orderRef` asociados al
usuario?

**Decision:**

La asociación de pedidos se realiza mediante `PUT /customers/{userId}`.

El cliente se crea inicialmente sin pedidos y el campo `orders` se actualiza
explícitamente durante la actualización del cliente.

Los `orderRef` recibidos son validados contra los pedidos cuyo `userId`
coincide con el cliente.

La decisión completa se encuentra documentada en:

`docs/decisions.md` — Decisión 014.

**Impact:**

La creación o actualización de Order no modifica automáticamente el
documento de Customer.

**Confirmation required:** No

---

## OQ-009 — Comportamiento de búsqueda únicamente por `displayName`

**Status:** Open

**Question:**

Cuando únicamente se proporciona `displayName`, ¿la respuesta debe
contener únicamente los items encontrados o también debe incluir los pedidos
que contienen esos items?

**Current proposal:**

Buscar primero los items que coinciden con `displayName` y posteriormente
obtener los pedidos asociados mediante sus `itemId`.

**Impact:**

Determina el contrato de respuesta y la lógica del caso de uso de búsqueda.

**Confirmation required:** Yes

---

## OQ-010 — Búsqueda combinada

**Status:** Open

**Question:**

Cuando se proporcionan filtros de pedido y `displayName` al mismo tiempo,
¿se espera que los filtros de pedido se apliquen primero y posteriormente
se filtre `displayName` únicamente sobre los items de los pedidos
encontrados?

**Current proposal:**

Aplicar primero:

- `orderRef`
- `orderStatus`
- `storeName`

y posteriormente utilizar `displayName` sobre los items asociados a los
pedidos resultantes.

**Impact:**

Determina el comportamiento del endpoint cuando se combinan criterios de
pedido y producto.

**Confirmation required:** Yes

---

## OQ-011 — Persistencia de datos de `/items`

**Status:** Resolved by Design

**Question:**

¿Los datos proporcionados por `/items` deben considerarse un catálogo o
fuente de referencia que debe persistirse dentro de la aplicación, o
solamente deben utilizarse para resolver la información de los pedidos?

**Decision:**

Los datos de `/items` se consideran datos de referencia y se mantienen como
información interna de la aplicación para soportar el modelo Item y la
funcionalidad de búsqueda.

No se implementa una arquitectura específica de consumidores HTTP para la
URL externa.

La decisión completa se encuentra documentada en:

`docs/decisions.md` — Decisión 011.

**Impact:**

El módulo Item forma parte de la persistencia interna de la aplicación y
puede ser utilizado por Search para resolver información de productos.

**Confirmation required:** No

---

## OQ-012 — Item faltante en los datos de prueba

**Status:** Open

**Question:**

El pedido `632005897` contiene el item:

`632005897-749826482`

pero dicho item no aparece dentro del conjunto de datos de referencia
proporcionado para `/items`.

¿Debe agregarse este item manualmente para mantener consistente el conjunto
de datos de prueba?

**Current proposal:**

Agregar un item de prueba para mantener la relación completa entre el pedido
y sus items.

El registro agregado se identificaría explícitamente como dato de prueba y
no como información proveniente del servicio de referencia.

**Impact:**

Evita que exista un pedido cuyo `items` referencia un producto que no puede
ser recuperado desde la colección interna de items.

**Confirmation required:** Yes

---

## OQ-013 — Estructura de `shippingAddress`

**Status:** Open

**Question:**

¿La dirección de envío debe permanecer como una cadena de texto o se espera
una estructura con campos como calle, número, colonia, ciudad, estado y
código postal?

**Current proposal:**

Mantener `shippingAddress` como `String`.

**Impact:**

Si se requiere una estructura detallada, será necesario modificar el modelo
de dominio, DTOs y persistencia.

**Confirmation required:** Yes

---

## Status Summary

| ID | Topic | Status |
|---|---|---|
| OQ-001 | Delivery por pedido o item | Open |
| OQ-002 | Alcance de las operaciones CRUD | Open |
| OQ-003 | Creación de Delivery | Open |
| OQ-004 | Significado de `orderStatus` | Open |
| OQ-005 | Regla de `itemId` | Open |
| OQ-006 | Identificador incremental de Item | Open |
| OQ-007 | Una o múltiples direcciones de entrega | Open |
| OQ-008 | Sincronización de `Customer.orders` | Resolved by Design |
| OQ-009 | Búsqueda únicamente por `displayName` | Open |
| OQ-010 | Búsqueda combinada | Open |
| OQ-011 | Persistencia de datos de `/items` | Resolved by Design |
| OQ-012 | Item faltante en los datos de prueba | Open |
| OQ-013 | Estructura de `shippingAddress` | Open |