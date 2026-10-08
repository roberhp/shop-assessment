Diseño de la API

1. Objetivo

Este documento define el diseño inicial de la API REST de la aplicación, distinguiendo entre endpoints explícitamente definidos por la evaluación y endpoints propuestos para cubrir las funcionalidades requeridas.

Los endpoints que no están definidos explícitamente en la evaluación se consideran propuestas de diseño y podrán ajustarse si el evaluador proporciona una definición diferente.

⸻

2. Customer

La evaluación define operaciones para crear, consultar y actualizar información de clientes.

Crear cliente

POST /customers

Responsabilidad:

Crear un nuevo cliente y persistir su información en MongoDB.

Consultar cliente

GET /customers/{userId}

Responsabilidad:

Obtener la información de un cliente identificado mediante userId.

Actualizar cliente

PUT /customers/{userId}

Responsabilidad:

Actualizar la información de un cliente existente.

⸻

3. Order

La evaluación proporciona información de pedidos mediante el servicio externo /pedidos.

Como propuesta de diseño, la API propia expondrá:

Consultar pedido

GET /orders/{orderRef}

Responsabilidad:

Obtener la información de un pedido utilizando orderRef como identificador funcional.

Estado: Propuesto.

La evaluación no define explícitamente este endpoint, por lo que su contrato puede ajustarse si se determina que la información debe exponerse de otra forma.

⸻

4. Search

La evaluación requiere búsqueda de pedidos y productos.

Buscar pedidos

GET /search/orders

Los criterios de búsqueda contemplados son:

* orderRef
* orderStatus
* storeName

La API deberá permitir utilizar los criterios de acuerdo con la necesidad de la consulta.

Buscar productos

GET /search/items

La búsqueda de productos deberá permitir localizar elementos mediante displayName.

La evaluación requiere que la búsqueda soporte:

* Texto/typeahead.
* Diferencias de mayúsculas y minúsculas.
* Diferencias de acentuación.
* Comas y otros signos de puntuación.
* Errores menores de escritura.

La estrategia concreta para tolerar errores menores se definirá durante la implementación.

Una alternativa considerada es utilizar una métrica de distancia de edición, como Levenshtein.

⸻

5. Delivery

La evaluación identifica información de entrega, incluyendo la dirección de envío.

Sin embargo, no define explícitamente los endpoints REST correspondientes ni las operaciones exactas que deben exponerse.

Por este motivo no se agregan endpoints de Delivery como parte del contrato definitivo en esta etapa.

Esta definición queda registrada como pregunta abierta en docs/decisions.md.

⸻

6. DTOs

Los DTOs de la API propia estarán separados de los modelos de dominio.

Esto permite que:

* Los cambios del contrato REST no modifiquen directamente el dominio.
* Las validaciones de entrada permanezcan en la capa de presentación.
* Las respuestas puedan evolucionar independientemente del modelo interno.

Los DTOs pertenecientes a servicios externos tampoco se utilizarán directamente como DTOs de la API propia.

⸻

7. Manejo de Errores

Los errores de la aplicación deberán traducirse a respuestas HTTP apropiadas.

Los controladores no deberán contener lógica de negocio para determinar el resultado de las operaciones.

El manejo común de excepciones se centralizará en la configuración de presentación correspondiente.

El contrato detallado de errores podrá definirse durante la implementación.

⸻

8. Preguntas Abiertas

8.1 Estructura de respuesta de búsqueda

La evaluación no especifica la estructura exacta de las respuestas de las APIs de búsqueda.

Debe definirse antes de considerar el contrato completamente cerrado.

8.2 Operaciones CRUD de Order y Delivery

La evaluación menciona inicialmente tres APIs CRUD relacionadas con información de cliente, entrega y pedido, pero las operaciones detalladas no están definidas con el mismo nivel de precisión para todos los recursos.

No se agregarán endpoints de actualización o eliminación de pedidos o entregas sin una justificación basada en los requerimientos.

8.3 Identificación de Customer

El cliente se identifica mediante userId, de acuerdo con la evaluación.

Los pedidos asociados se representan mediante orderRef.

⸻

9. Estado del Diseño

Confirmado por la evaluación

* Customer: creación.
* Customer: consulta.
* Customer: actualización.
* Customer identificado mediante userId.
* Búsqueda de pedidos.
* Filtros de pedido por orderRef, orderStatus y storeName.
* Búsqueda de productos mediante displayName.
* Tolerancia a diferencias de texto.

Propuesto

* GET /orders/{orderRef}.
* GET /search/orders.
* GET /search/items.

Pendiente de confirmación

* Contrato exacto de respuestas de búsqueda.
* Operaciones CRUD exactas de Order y Delivery.
* Estructura definitiva de la dirección de envío.