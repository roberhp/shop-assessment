# Open Questions

Este documento contiene las preguntas identificadas durante el análisis y
desarrollo del proyecto que requieren confirmación sobre reglas de negocio
o comportamiento esperado.

La intención es documentar las ambigüedades detectadas, su impacto técnico
y la decisión provisional utilizada mientras se obtiene una confirmación.

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

## OQ-002 — Operaciones CRUD

**Status:** Open

**Question:**

Cuando el enunciado indica que se requieren 3 APIs CRUD, ¿debemos implementar
las operaciones completas Create, Read, Update y Delete para Customer, Order
y Delivery, o las operaciones esperadas son únicamente las descritas
específicamente para cada recurso?

**Current proposal:**

Implementar inicialmente Create, Read y Update, ya que son las operaciones
explícitamente descritas para Customer.

Evaluar la necesidad de Delete después de confirmar el alcance.

**Impact:**

Determina si debemos agregar endpoints `DELETE` para los recursos.

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

## OQ-008 — Actualización de `Customer.orders`

**Status:** Open

**Question:**

Cuando se crea o modifica un pedido, ¿el campo `orders` del Customer debe
actualizarse automáticamente para reflejar los `orderRef` asociados al
usuario?

**Current proposal:**

Mantener `Customer.orders` como una lista de `orderRef`.

La forma exacta en que esta lista se sincroniza con Order queda pendiente
de confirmar.

**Impact:**

Determina si la creación o actualización de Order debe modificar también
el documento de Customer.

**Confirmation required:** Yes

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

**Status:** Open

**Question:**

¿Los datos proporcionados por `/items` deben considerarse un catálogo o
fuente de referencia que debe persistirse dentro de la aplicación, o
solamente deben utilizarse para resolver la información de los pedidos?

**Current proposal:**

Persistir los items como información interna de la aplicación para permitir
la búsqueda por `displayName` y relacionarlos con los pedidos mediante
`itemId`.

**Impact:**

Determina si el módulo Item debe considerarse únicamente como soporte de
búsqueda o como una colección persistente del sistema.

**Confirmation required:** Yes

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
| OQ-002 | Operaciones CRUD | Open |
| OQ-003 | Creación de Delivery | Open |
| OQ-004 | Significado de `orderStatus` | Open |
| OQ-005 | Regla de `itemId` | Open |
| OQ-006 | Identificador incremental de Item | Open |
| OQ-007 | Una o múltiples direcciones de entrega | Open |
| OQ-008 | Actualización de `Customer.orders` | Open |
| OQ-009 | Búsqueda únicamente por `displayName` | Open |
| OQ-010 | Búsqueda combinada | Open |
| OQ-011 | Persistencia de datos de `/items` | Open |
| OQ-012 | Item faltante en los datos de prueba | Open |
| OQ-013 | Estructura de `shippingAddress` | Open |