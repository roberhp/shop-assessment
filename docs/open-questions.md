# Open Questions

Este documento contiene las preguntas identificadas durante el análisis y desarrollo del proyecto que requieren confirmación sobre reglas de negocio o comportamiento esperado.

La intención es documentar las ambigüedades detectadas, su impacto técnico y la decisión provisional utilizada mientras se obtiene una confirmación.

---

## OQ-001 — Delivery por pedido o por item

**Status:** Open

**Question:**

¿La dirección de entrega debe asociarse al pedido completo o puede existir una dirección de entrega diferente para cada producto/item dentro de una misma orden?

**Current proposal:**

Inicialmente, cada Delivery se asociará únicamente a un `orderRef` y contendrá una sola dirección de envío (`shippingAddress`).

**Impact:**

Si se permite una dirección diferente por item, Delivery deberá relacionarse también con `itemId`.

**Confirmation required:** Yes

---

## OQ-002 — Operaciones CRUD

**Status:** Open

**Question:**

Cuando el enunciado indica que se requieren 3 APIs CRUD, ¿debemos implementar las operaciones completas Create, Read, Update y Delete para Customer, Order y Delivery, o las operaciones esperadas son únicamente las descritas específicamente para cada recurso?

**Current proposal:**

Implementar inicialmente Create, Read y Update, ya que son las operaciones explícitamente descritas para Customer. Evaluar la necesidad de Delete después de confirmar el alcance.

**Impact:**

Determina si debemos agregar endpoints `DELETE` para los recursos.

**Confirmation required:** Yes

---

## OQ-003 — Creación de Delivery

**Status:** Open

**Question:**

¿Todo pedido debe tener obligatoriamente un registro de Delivery, o la información de entrega puede crearse posteriormente y existir temporalmente un pedido sin una dirección asociada?

**Current proposal:**

Permitir que Delivery sea administrado de forma independiente y asociado a un `orderRef`.

**Impact:**

Determina las reglas de validación y la relación entre Order y Delivery.

**Confirmation required:** Yes

---

## OQ-004 — Significado de `orderStatus`

**Status:** Open

**Question:**

En los datos de `/pedidos`, ¿el campo `orderStatus` representa directamente la fecha estimada de entrega, o existe otro campo que contiene la fecha y `orderStatus` representa realmente el estado del pedido?

**Current proposal:**

Mapear conceptualmente la información solicitada como `estimateDeliveryDate` y utilizar `LocalDate`, ya que el requerimiento menciona la fecha estimada de entrega y no se requiere información de hora.

**Impact:**

Determina el mapeo definitivo entre los datos de entrada y el modelo interno de Order.

**Confirmation required:** Yes

---

## OQ-005 — Regla de `itemId`

**Status:** Open

**Question:**

¿Debemos considerar `itemId` como un identificador funcional compuesto por `orderRef-skuId`, o debe tratarse únicamente como un identificador recibido desde la fuente de datos?

**Current proposal:**

Persistir `itemId` explícitamente dentro de `OrderItem`, junto con `skuId` y `quantity`.

Actualmente se observa que:

`itemId = orderRef-skuId`

pero no se impondrá todavía una validación que reconstruya el valor automáticamente.

**Impact:**

Determina si la aplicación debe validar la composición de `itemId` o simplemente conservar el valor recibido.

**Confirmation required:** Yes

---

## OQ-006 — Identificador incremental de Item

**Status:** Open

**Question:**

¿El `id` incremental de `/items` tiene alguna función dentro de la lógica del sistema o solamente identifica técnicamente cada registro?

**Current proposal:**

Considerarlo inicialmente como un identificador técnico independiente de `itemId` y `skuId`.

**Impact:**

Determina si debe formar parte del modelo de negocio o únicamente del modelo de persistencia/fuente de datos.

**Confirmation required:** Yes

---

## OQ-007 — Una o múltiples direcciones de entrega

**Status:** Open

**Question:**

Si un pedido contiene múltiples items, ¿todos los items deben compartir la misma dirección de entrega?

**Current proposal:**

Inicialmente asumir una única dirección de entrega por pedido.

Esta decisión es provisional y está relacionada con OQ-001.

**Impact:**

Si los items pueden tener diferentes destinos, será necesario asociar Delivery también con `itemId`.

**Confirmation required:** Yes

---

## OQ-008 — Actualización de `Customer.orders`

**Status:** Open

**Question:**

Cuando se crea o modifica un pedido, ¿el campo `orders` del Customer debe actualizarse automáticamente para reflejar los `orderRef` asociados al usuario?

**Current proposal:**

Mantener `Customer.orders` como una lista de `orderRef`. La forma exacta en que esta lista se sincroniza con Order queda pendiente de confirmar.

**Impact:**

Determina si la creación o actualización de Order debe modificar también el documento de Customer.

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