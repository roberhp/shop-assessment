# Arquitectura

## Visión general

La aplicación implementa un **Monolito Modular con una implementación
pragmática de Clean Architecture**.

La solución mantiene un único despliegue y separa las responsabilidades
por módulos funcionales.

La arquitectura busca:

- Mantener separadas las responsabilidades.
- Evitar el acoplamiento entre la lógica de negocio y MongoDB.
- Facilitar las pruebas unitarias.
- Mantener los módulos organizados y con límites claros.
- Permitir que la aplicación crezca sin introducir complejidad innecesaria.

La arquitectura evita utilizar MVC como patrón principal, de acuerdo con
los requisitos de la evaluación.

---

## Módulos

La aplicación se divide en los siguientes módulos:

- `customer`
- `order`
- `delivery`
- `item`
- `search`

Cada módulo contiene únicamente las responsabilidades relacionadas con su
propio dominio.

### Customer

Gestiona la información de los clientes.

Responsabilidades principales:

- Crear clientes.
- Consultar clientes.
- Actualizar clientes.
- Eliminar clientes.
- Mantener las referencias de pedidos asociados mediante `orderRef`.

La asociación entre clientes y pedidos utiliza `userId` como relación
entre ambos modelos.

### Order

Gestiona la información de los pedidos.

Responsabilidades principales:

- Crear pedidos.
- Consultar pedidos.
- Actualizar pedidos.
- Eliminar pedidos.
- Gestionar los productos asociados al pedido.

Un pedido se identifica mediante `orderRef`.

### Delivery

Gestiona la información relacionada con la entrega.

Responsabilidades principales:

- Crear información de entrega.
- Consultar información de entrega.
- Actualizar información de entrega.
- Eliminar información de entrega.

Actualmente `shippingAddress` se representa como `String`.

Esta representación se mantiene como una decisión provisional debido a que
la evaluación no define una estructura detallada para la dirección.

### Item

Representa los productos asociados a los pedidos.

Contiene información como:

- `itemId`
- `skuId`
- `quantity`
- `displayName`
- información relacionada con el estado de entrega cuando corresponde.

### Search

Centraliza la funcionalidad de búsqueda flexible.

Permite realizar búsquedas utilizando:

- `orderRef`
- `orderStatus`
- `storeName`
- `displayName`

El módulo también contiene la lógica de normalización y fuzzy matching.

---

## Organización por capas

Los módulos utilizan una separación conceptual entre las siguientes capas:

- Domain
- Application
- Infrastructure
- Presentation

La estructura general es:

```text
src/main/java/com/liverpool/appsales/exam/
├── customer/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
├── order/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
├── delivery/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
├── item/
│   ├── domain/
│   ├── application/
│   └── infrastructure/
├── search/
│   ├── domain/
│   ├── application/
│   └── presentation/
└── presentation/
    └── GlobalExceptionHandler.java
```

---

## Domain

La capa de dominio contiene los modelos principales de negocio.

Ejemplos:

- `Customer`
- `Order`
- `OrderItem`

Los modelos de dominio no dependen directamente de Spring Data MongoDB
ni de los detalles de persistencia.

---

## Application

La capa de aplicación contiene los casos de uso y las abstracciones
necesarias para ejecutar las operaciones del sistema.

Ejemplos de responsabilidades:

- Crear un cliente.
- Consultar un cliente.
- Actualizar un cliente.
- Crear un pedido.
- Consultar un pedido.
- Ejecutar búsquedas.

Los casos de uso dependen de abstracciones de repositorio y no de
implementaciones concretas de MongoDB.

---

## Infrastructure

La capa de infraestructura contiene los detalles técnicos necesarios
para persistir la información.

Actualmente utiliza:

- Spring Data MongoDB.
- MongoDB.

Los documentos utilizados por MongoDB se mantienen separados de los
modelos de dominio.

El flujo de persistencia es:

```text
Caso de uso
    ↓
Abstracción de repositorio
    ↓
Adapter de persistencia
    ↓
Spring Data MongoDB
    ↓
MongoDB
```

Los adapters son responsables de convertir entre los modelos de dominio
y los documentos de persistencia.

---

## Presentation

La capa de presentación contiene los controladores REST y los DTOs
utilizados por la API.

Los controladores:

- Reciben requests HTTP.
- Validan los datos de entrada mediante DTOs.
- Invocan los casos de uso correspondientes.
- Transforman los resultados a DTOs de respuesta.

Los modelos de dominio no se utilizan directamente como contratos de la
API REST.

---

## Dirección de dependencias

La dirección principal de dependencias es:

```text
Presentation
      ↓
Application
      ↓
Domain
```

Infrastructure proporciona las implementaciones técnicas utilizadas por
Application.

La lógica de negocio no depende directamente de MongoDB.

---

## Persistencia

MongoDB es la tecnología de persistencia utilizada por la aplicación.

Se utiliza un documento de persistencia separado para cada agregado o
modelo persistido.

Esta separación permite evitar que los detalles específicos de MongoDB
formen parte de la lógica de negocio.

MongoDB es un requisito de la evaluación, por lo que no se introduce una
abstracción destinada a cambiar de tecnología de base de datos.

---

## Datos de referencia `/pedidos` y `/items`

La evaluación proporciona las referencias:

- `/pedidos`
- `/items`

Estas referencias se utilizan como fuente de datos para definir la
estructura y poblar la información utilizada por la aplicación.

La aplicación no implementa una arquitectura específica de consumidores
HTTP para estos servicios.

Por lo tanto, no se agregan adapters de integración externos únicamente
para consumir dichas URLs.

La aplicación mantiene sus propios modelos y persistencia interna para:

- Customer.
- Order.
- Delivery.
- Item.

---

## Asociación entre Customer y Order

Los clientes mantienen referencias a sus pedidos mediante `orderRef`.

El vínculo entre un cliente y sus pedidos se establece mediante
`userId`.

La asociación se gestiona explícitamente desde la operación de actualización
del cliente.

Antes de asociar un `orderRef`, se valida que el pedido corresponda al mismo
`userId` del cliente.

De esta forma se evita asociar accidentalmente a un cliente un pedido
perteneciente a otro usuario.

---

## DTOs

Los DTOs se utilizan para separar los contratos HTTP de los modelos
internos.

Se utilizan DTOs específicos para:

- Requests de creación.
- Requests de actualización.
- Responses.

Esto evita exponer directamente los modelos de dominio o los documentos
de MongoDB a través de la API.

---

## Arquitectura de búsqueda

La funcionalidad de búsqueda se encuentra separada en el módulo `search`.

El flujo general es:

```text
Request
   ↓
SearchController
   ↓
SearchUseCase
   ↓
Normalización de texto
   ↓
Aplicación de filtros
   ↓
Fuzzy matching cuando corresponde
   ↓
SearchResponse
```

La búsqueda de texto normaliza:

- Mayúsculas y minúsculas.
- Acentos.
- Puntuación.
- Comas.
- Espacios adicionales.

Para tolerar errores pequeños de escritura se utiliza fuzzy matching basado
en Levenshtein Distance.

---

## Prioridad de filtros

Cuando se utilizan filtros relacionados con el pedido, estos tienen
prioridad sobre la búsqueda por `displayName`.

El comportamiento definido es:

### Sin filtros

Se buscan los pedidos y sus items asociados.

### Filtros de pedido

Se filtran primero los pedidos mediante:

- `orderRef`
- `orderStatus`
- `storeName`

Posteriormente se recuperan sus items asociados.

### Filtros de pedido + `displayName`

Primero se filtran los pedidos.

Después se buscan los items asociados a dichos pedidos utilizando
`displayName`.

### Solo `displayName`

Primero se buscan los items que coinciden con el texto.

Posteriormente se identifican los pedidos asociados a dichos items.

Este comportamiento se mantiene como una decisión de diseño documentada
en `docs/decisions.md`.

---

## Manejo de errores

Los errores de la aplicación se manejan mediante un
`GlobalExceptionHandler`.

El componente centraliza errores como:

- Recursos no encontrados.
- Recursos duplicados.
- Errores de validación.
- Violaciones de restricciones.

Esto permite mantener los controladores enfocados en la gestión de
requests y responses.

---

## Principios utilizados

La implementación busca aplicar los siguientes principios:

- Clean Code.
- Single Responsibility Principle.
- Dependency Inversion.
- Separación de responsabilidades.
- DTOs para contratos REST.
- Separación entre dominio y persistencia.
- Bajo acoplamiento.
- Alta cohesión.
- Código testeable.

Los patrones y abstracciones se utilizan de manera pragmática y no se
introducen capas únicamente para incrementar la complejidad de la solución.

---

## Evolución de la arquitectura

La arquitectura actual está diseñada como un monolito modular.

Si el sistema creciera significativamente, los módulos podrían evolucionar
de manera independiente antes de considerar una separación física en
servicios.

La prioridad actual es mantener una solución simple, mantenible y
adecuada al alcance de la evaluación.