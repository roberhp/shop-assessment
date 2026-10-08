Arquitectura General

1. Objetivo

La aplicación se implementará como un Monolito Modular, utilizando principios de Arquitectura Limpia pragmática y Puertos y Adaptadores (Hexagonal).

El objetivo es mantener separadas las reglas de negocio de los detalles de infraestructura, facilitar las pruebas y permitir sustituir tecnologías o servicios externos sin modificar la lógica principal de la aplicación.

La solución se mantiene como un único despliegue debido al alcance del proyecto. No se considera necesario dividir la aplicación en múltiples microservicios.

⸻

2. Estilo de Arquitectura

La solución combina tres conceptos principales:

Monolito Modular

La aplicación se mantiene como un único proyecto y despliegue, pero se divide internamente en módulos relacionados con las principales capacidades del sistema:

* Customer
* Order
* Search

Esto permite mantener una separación clara de responsabilidades sin introducir la complejidad operativa de múltiples servicios.

Arquitectura Limpia

La lógica de negocio y los casos de uso se mantienen independientes de detalles concretos como:

* MongoDB.
* Clientes HTTP.
* URLs de servicios externos.
* Frameworks de persistencia.
* Detalles de presentación.

Puertos y Adaptadores

Las comunicaciones con sistemas externos se realizan mediante abstracciones (ports) definidas por la aplicación y sus implementaciones concretas (adapters).

Esto permite aislar:

* MongoDB.
* API /pedidos.
* API /items.

⸻

3. Módulos

La aplicación se divide en los siguientes módulos:

```
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
├── search
│   ├── domain
│   ├── application
│   ├── infrastructure
│   └── presentation
└── shared
    ├── exception
    └── config
```

Customer

Responsable de la información y operaciones relacionadas con clientes.

Incluye la relación entre un cliente y sus pedidos mediante orderRef.

Order

Responsable de representar y consultar información de pedidos.

El módulo también contiene la integración con el servicio externo /pedidos.

Search

Responsable de las funcionalidades de búsqueda solicitadas por la evaluación, incluyendo búsqueda de pedidos y productos.

Shared

Contiene únicamente elementos realmente transversales a varios módulos, como excepciones o configuración común.

Se evitará utilizar este módulo como un contenedor genérico para lógica de negocio que pertenezca a un módulo específico.

⸻

4. Capas

Cada módulo puede organizarse internamente en las siguientes capas:

Domain

Contiene el modelo de dominio y las reglas de negocio que no dependen de infraestructura ni de frameworks externos.

Application

Contiene los casos de uso de la aplicación y los puertos necesarios para interactuar con dependencias externas.

Infrastructure

Contiene las implementaciones concretas de los puertos, incluyendo:

* Persistencia en MongoDB.
* Consumo de APIs externas.
* Configuraciones relacionadas con infraestructura.

Presentation

Contiene los controladores REST y los DTOs relacionados con la exposición HTTP de la aplicación.

⸻

5. Dirección de Dependencias

La dirección de dependencias apunta hacia el dominio y las reglas de negocio.

Conceptualmente:
```
Presentation
     │
     ▼
Application
     │
     ▼
  Domain
```
Las implementaciones de infraestructura dependen de las abstracciones definidas por las capas internas:

```
┌───────────────┐
│  Application  │
│    Ports      │
└───────▲───────┘
        │
    implements
        │
┌───────┴───────┐
│ Infrastructure│
└───────────────┘
```
El dominio no depende de MongoDB, HTTP ni de otros detalles de infraestructura.

Los adaptadores de infraestructura son responsables de implementar los puertos definidos por la aplicación y realizar las transformaciones necesarias entre modelos externos e internos.

⸻

6. Integraciones Externas

La evaluación proporciona dos servicios externos:

* /pedidos
* /items

La aplicación los consumirá mediante adaptadores específicos.

La capa de aplicación no dependerá directamente de:

* URLs externas.
* Clientes HTTP concretos.
* DTOs pertenecientes al servicio externo.
* Detalles específicos de la implementación de comunicación.

El flujo conceptual será:
```
REST Controller
      │
      ▼
   Use Case
      │
      ▼
Application Port
      │
      ▼
External API Adapter
      │
      ▼
  External API
```

El adaptador será responsable de:

1. Consumir el servicio externo.
2. Interpretar su respuesta.
3. Transformar el modelo externo al modelo utilizado por la aplicación.
4. Manejar errores propios de la integración.

⸻

7. Persistencia

La información de Customer se persistirá utilizando MongoDB, de acuerdo con la evaluación.

La aplicación utilizará un puerto de persistencia en la capa de aplicación y una implementación concreta en infraestructura.

Conceptualmente:

```
Customer Use Case
       │
       ▼
Customer Repository Port
       │
       ▼
MongoDB Repository Adapter
       │
       ▼
     MongoDB
```

La lógica de negocio no dependerá directamente de las APIs de MongoDB.

⸻

8. Transformación de Modelos

Los modelos externos no se utilizarán directamente como modelos de dominio.

Cuando sea necesario, se mantendrán separados:

* DTOs de servicios externos.
* Modelos de dominio.
* Modelos de persistencia.
* DTOs de la API propia.

Esto evita que cambios en un servicio externo se propaguen directamente hacia el dominio de la aplicación.

Un ejemplo es orderStatus: el servicio externo utiliza ese nombre, mientras que el modelo de dominio propone utilizar el concepto estimateDeliveryDate.

⸻

9. Decisiones de Diseño

Las decisiones que afectan la arquitectura y el modelo se registran en:

docs/decisions.md

Entre ellas:

* Customer almacena referencias orderRef.
* orderStatus se propone representar como estimateDeliveryDate.
* shippingAddress se mantiene provisionalmente como String.

Las decisiones provisionales permanecerán identificadas hasta obtener la confirmación correspondiente.

⸻

10. Principios de Diseño

La implementación seguirá los siguientes principios:

* Single Responsibility Principle.
* Dependency Inversion Principle.
* Separación de responsabilidades.
* Dependencia hacia abstracciones.
* Bajo acoplamiento entre módulos.
* Alta cohesión dentro de cada módulo.
* Evitar lógica de negocio dentro de controladores.
* Evitar dependencia directa del dominio con infraestructura.
* Evitar clases o módulos compartidos sin una responsabilidad transversal real.

La arquitectura no se utilizará como una estructura puramente académica. Cada capa y abstracción deberá aportar una responsabilidad concreta al proyecto.