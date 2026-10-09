# API de Gestión de Pedidos - Liverpool

API REST desarrollada como solución para el examen técnico Backend de Puerto de Liverpool.

El proyecto permite gestionar clientes, pedidos y entregas mediante APIs REST, además de proporcionar un servicio de búsqueda flexible para consultar pedidos y productos.

## Tecnologías

- Java 17
- Spring Boot
- Spring Web
- Spring Data MongoDB
- MongoDB 8
- Maven
- Docker
- Docker Compose
- Bean Validation
- Lombok
- OpenAPI / Swagger
- JUnit
- Mockito
- Checkstyle
- Git

## Arquitectura

El proyecto utiliza un **Monolito Modular con una Clean Architecture pragmática**.

La aplicación está organizada por módulos de negocio:

ññtext
src/main/java/com/liverpool/appsales/exam/

├── customer/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
│
├── order/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
│
├── delivery/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
│
├── item/
│   ├── domain/
│   ├── application/
│   └── infrastructure/
│
├── search/
│   ├── domain/
│   ├── application/
│   └── presentation/
│
└── presentation/
    └── GlobalExceptionHandler.java
ññ

La dirección principal de dependencias es:

ññtext
Presentation
     ↓
Application
     ↓
Domain

Infrastructure
     ↓
implementa las abstracciones de Application
ññ

### Módulos

#### Customer

Responsable de la información de los clientes.

Incluye:

- Creación
- Consulta
- Actualización
- Eliminación
- Relación con pedidos mediante `orderRef`

#### Order

Responsable de la información de los pedidos.

Incluye:

- Creación
- Consulta
- Actualización
- Eliminación
- Items asociados
- Fecha estimada de entrega

#### Delivery

Responsable de la información de entrega.

Incluye:

- Creación
- Consulta
- Actualización
- Eliminación
- Dirección de envío

#### Item

Representa los productos asociados a los pedidos.

Los datos de ejemplo proporcionados por el ejercicio se utilizan como referencia para construir y probar la información de los pedidos.

#### Search

Contiene la lógica de búsqueda de pedidos y productos.

Permite combinar:

- `orderRef`
- `orderStatus`
- `storeName`
- `displayName`

También realiza normalización de texto y búsqueda flexible.

---

## Funcionalidades

### Clientes

- Crear cliente
- Consultar cliente
- Actualizar cliente
- Eliminar cliente
- Asociar pedidos mediante `orderRef`

### Pedidos

- Crear pedido
- Consultar pedido
- Actualizar pedido
- Eliminar pedido
- Asociar items

### Entregas

- Crear entrega
- Consultar entrega
- Actualizar entrega
- Eliminar entrega

### Búsqueda

Permite buscar pedidos utilizando:

- Número de pedido
- Estatus
- Tienda

Y recuperar productos mediante su nombre (`displayName`).

La búsqueda de texto considera:

- Mayúsculas y minúsculas
- Acentos
- Comas y otros signos de puntuación
- Espacios
- Errores menores de escritura

La búsqueda flexible utiliza normalización de texto y comparación mediante distancia de Levenshtein.

---

## Persistencia

La aplicación utiliza MongoDB como base de datos.

Las entidades de dominio se mantienen separadas de los documentos de persistencia mediante clases `Document` y adaptadores de repositorio.

Por ejemplo:

ññtext
Domain
  ↓
Repository abstraction
  ↓
Repository Adapter
  ↓
Spring Data MongoDB
  ↓
MongoDB
ññ

Esto permite mantener las reglas de negocio independientes de los detalles específicos de MongoDB.

---

## Requisitos

Para ejecutar el proyecto se necesita:

- Java 17 o superior
- Docker
- Docker Compose
- Maven Wrapper incluido en el proyecto

No es necesario instalar Maven globalmente, ya que el proyecto utiliza Maven Wrapper.

---

## Configuración de MongoDB

El proyecto utiliza MongoDB 8 como almacenamiento persistente.

La configuración de MongoDB se realiza mediante variables de entorno utilizadas por Docker Compose.

### Variables de entorno

El repositorio incluye un archivo `.env.example` como referencia.

Crear el archivo `.env` en la raíz del proyecto:

ññbash
cp .env.example .env
ññ

El archivo `.env` contiene las credenciales utilizadas por Docker Compose y **no debe versionarse en Git**.

Configuración utilizada para el ambiente de demostración:

- Host: `localhost`
- Puerto: `27017`
- Usuario: `liverpool`
- Password: `liverpool_exam`
- Base de datos: `liverpool_exam`
- Authentication database: `admin`

URI para conectarse directamente desde MongoDB Compass, `mongosh` u otra herramienta:

`mongodb://liverpool:liverpool_exam@localhost:27017/liverpool_exam?authSource=admin`

---

## Ejecución con Docker Compose

El proyecto incluye un `Dockerfile` y un `docker-compose.yml` para levantar la aplicación y MongoDB conjuntamente.

La arquitectura de ejecución es:

ññtext
Docker Compose
│
├── Spring Boot
│   └── Puerto 8080
│
└── MongoDB 8
    └── Puerto 27017
ññ

### Primera ejecución

Después de clonar el repositorio:

ññbash
cp .env.example .env
docker compose up --build
ññ

Docker Compose construirá la imagen de Spring Boot y levantará los contenedores de la aplicación y MongoDB.

La aplicación estará disponible en:

`http://localhost:8080`

MongoDB estará disponible en:

`localhost:27017`

### Detener la aplicación

ññbash
docker compose down
ññ

Los datos de MongoDB se mantienen en un volumen Docker.

### Reinicializar MongoDB

Para eliminar el volumen y comenzar nuevamente con una base de datos limpia:

ññbash
docker compose down -v
docker compose up --build
ññ

> El uso de `docker compose down -v` elimina los datos persistidos de MongoDB.

---

## Ejecución local de Spring Boot

También es posible ejecutar Spring Boot directamente en la máquina local mientras MongoDB se ejecuta mediante Docker.

Primero levantar MongoDB:

ññbash
docker compose up -d mongodb
ññ

Después ejecutar la aplicación:

ññbash
./mvnw spring-boot:run
ññ

Cuando Spring Boot se ejecuta localmente, utiliza la configuración local de MongoDB definida mediante `application.properties`.

La aplicación estará disponible en:

`http://localhost:8080`

---

## Datos de prueba

El proyecto incluye un `DataInitializer` que carga datos iniciales para facilitar la ejecución local y las pruebas manuales.

El seed puede controlarse mediante:

ññproperties
app.seed.enabled=true
ññ

Para desactivarlo:

ññproperties
app.seed.enabled=false
ññ

Los datos de prueba incluyen:

- Clientes
- Pedidos
- Items
- Entregas

El seed verifica previamente si los registros ya existen para evitar insertar duplicados cada vez que inicia la aplicación.

---

## Documentación de la API

La API está documentada mediante OpenAPI.

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

Desde Swagger se pueden consultar y ejecutar los endpoints disponibles.

---

# APIs

## Customers

### Crear cliente

ññtext
POST /customers
ññ

### Consultar cliente

ññtext
GET /customers/{userId}
ññ

### Actualizar cliente

ññtext
PUT /customers/{userId}
ññ

### Eliminar cliente

ññtext
DELETE /customers/{userId}
ññ

---

## Orders

### Crear pedido

ññtext
POST /orders
ññ

### Consultar pedido

ññtext
GET /orders/{orderRef}
ññ

### Actualizar pedido

ññtext
PUT /orders/{orderRef}
ññ

### Eliminar pedido

ññtext
DELETE /orders/{orderRef}
ññ

---

## Deliveries

### Crear entrega

ññtext
POST /deliveries
ññ

### Consultar entrega

ññtext
GET /deliveries/{deliveryId}
ññ

### Actualizar entrega

ññtext
PUT /deliveries/{deliveryId}
ññ

### Eliminar entrega

ññtext
DELETE /deliveries/{deliveryId}
ññ

---

# Search

La búsqueda se expone mediante:

ññtext
GET /search
ññ

## Buscar por orderRef

ññtext
GET /search?orderRef=3010091676
ññ

## Buscar por orderStatus

ññtext
GET /search?orderStatus=2025-12-06
ññ

## Buscar por storeName

ññtext
GET /search?storeName=L SANTA FE
ññ

## Buscar por displayName

ññtext
GET /search?displayName=Monitor
ññ

## Combinar filtros

ññtext
GET /search?orderRef=3010091676&displayName=Pantalon
ññ

Cuando existen filtros relacionados con el pedido, primero se identifican los pedidos que cumplen esos criterios.

Posteriormente, los items asociados a dichos pedidos pueden filtrarse mediante `displayName`.

Cuando únicamente se proporciona `displayName`, se buscan primero los items coincidentes y posteriormente se recuperan los pedidos asociados.

---

# Validaciones y manejo de errores

La aplicación utiliza Bean Validation para validar las solicitudes.

Algunos ejemplos:

- Campos obligatorios vacíos
- Formatos de correo inválidos
- Listas de items vacías

Los errores se centralizan mediante un `GlobalExceptionHandler`.

Respuestas principales:

| Situación | HTTP |
|---|---:|
| Creación exitosa | 201 |
| Consulta exitosa | 200 |
| Actualización exitosa | 200 |
| Eliminación exitosa | 204 |
| Recurso inexistente | 404 |
| Recurso duplicado | 409 |
| Error de validación | 400 |

---

# Pruebas

El proyecto incluye pruebas automatizadas utilizando JUnit y Mockito.

Para ejecutar todas las pruebas:

ññbash
./mvnw test
ññ

Para ejecutar la validación completa del proyecto:

ññbash
./mvnw clean verify
ññ

La validación completa incluye las pruebas y Checkstyle.

---

# Cobertura de código

La cobertura puede analizarse utilizando JaCoCo.

Para generar el reporte sin modificar la configuración del proyecto:

ññbash
./mvnw clean test org.jacoco:jacoco-maven-plugin:report
ññ

Después de ejecutar el comando, el reporte HTML se genera en:

ññtext
target/site/jacoco/index.html
ññ

El reporte permite revisar:

- Cobertura de líneas
- Cobertura de instrucciones
- Cobertura de métodos
- Cobertura de clases

La cobertura se utiliza como herramienta para identificar código sin pruebas, no como objetivo de alcanzar artificialmente un porcentaje determinado.

---

# Calidad de código

El proyecto utiliza Checkstyle para mantener una estructura y estilo de código consistente.

Para ejecutar las validaciones:

ññbash
./mvnw clean verify
ññ

---

# Estructura de documentación

La documentación adicional del proyecto se encuentra en:

ññtext
docs/
├── architecture.md
├── api-design.md
├── decisions.md
└── open-questions.md
ññ

### Architecture

Describe la arquitectura modular y la separación de responsabilidades.

### API Design

Describe el diseño de los endpoints y sus contratos.

### Decisions

Registra las decisiones tomadas durante el desarrollo y su justificación.

### Open Questions

Registra los puntos del requerimiento que no están suficientemente definidos y que podrían confirmarse con el evaluador.

---

# Decisiones importantes

## Monolito modular

Se eligió un monolito modular porque el alcance del ejercicio no justifica dividir la aplicación en múltiples microservicios.

Los módulos mantienen responsabilidades separadas y pueden evolucionar independientemente dentro de la misma aplicación.

## Clean Architecture pragmática

Se utiliza separación entre:

- Domain
- Application
- Infrastructure
- Presentation

Sin introducir una implementación formal de arquitectura hexagonal que agregue complejidad innecesaria para el alcance actual.

## MongoDB

MongoDB se utiliza porque forma parte explícita de los requerimientos del ejercicio para la persistencia de clientes y se mantiene como tecnología de persistencia principal.

## Customer.orders

El campo `orders` del cliente contiene los valores de `orderRef` de los pedidos asociados al usuario.

## estimateDeliveryDate

La fecha estimada de entrega se representa mediante `LocalDate`, ya que el requerimiento solamente especifica una fecha y no un horario.

## shippingAddress

La dirección de envío actualmente se representa como un `String`.

La posibilidad de convertirla posteriormente en una estructura con campos independientes queda documentada como una pregunta abierta.

## Eliminación

Actualmente las operaciones DELETE realizan eliminación física.

No se implementó soft delete porque el requerimiento no especifica un estado de baja ni reglas de recuperación histórica.

En un sistema productivo podría evaluarse soft delete dependiendo de los requerimientos de auditoría, trazabilidad e historial.

## Búsqueda flexible

La búsqueda normaliza los textos para ignorar diferencias de:

- Mayúsculas
- Minúsculas
- Acentos
- Puntuación
- Espacios

Además, se utiliza una comparación basada en distancia de Levenshtein para errores menores de escritura.

---

# Datos de referencia

El ejercicio proporciona información de ejemplo relacionada con pedidos e items.

Estos datos se utilizan como referencia para construir la información persistida y demostrar la funcionalidad de la aplicación.

No se implementó una arquitectura de integración con servicios externos para `/pedidos` y `/items`, ya que en la solución actual se consideran datos de referencia para el dominio del ejercicio.

---

# Consideraciones de diseño

La solución prioriza:

- Separación de responsabilidades
- Clean Code
- Bajo acoplamiento
- Validación de entrada
- Manejo centralizado de excepciones
- Persistencia separada del dominio
- Pruebas automatizadas
- Documentación
- Facilidad de mantenimiento

Se evitó introducir componentes de infraestructura que no fueran necesarios para los requerimientos actuales.

---

# Posibles mejoras futuras

Algunas mejoras podrían evaluarse si el sistema evolucionara:

- Soft delete
- Auditoría de cambios
- Paginación en búsquedas
- Índices específicos de MongoDB
- Optimización de búsqueda fuzzy
- Pruebas de integración con MongoDB
- Testcontainers
- Despliegue en cloud
- Observabilidad y métricas
- Autenticación y autorización
- Estructuración de `shippingAddress`
- Definición más precisa de los estados del pedido

Estas mejoras no forman parte del alcance mínimo implementado.

---

# Ejecución rápida

Para levantar el ambiente completo mediante Docker:

ññbash
cp .env.example .env
docker compose up --build
ññ

Después abrir:

`http://localhost:8080/swagger-ui/index.html`

Para conectarse directamente a MongoDB:

`mongodb://liverpool:liverpool_exam@localhost:27017/liverpool_exam?authSource=admin`

---

# Estado del proyecto

El proyecto contiene:

- APIs CRUD de clientes
- APIs CRUD de pedidos
- APIs CRUD de entregas
- Servicio de búsqueda
- Persistencia MongoDB
- Datos iniciales de prueba
- Validaciones
- Manejo global de errores
- OpenAPI / Swagger
- Pruebas automatizadas
- Checkstyle
- Dockerfile
- Docker Compose
- Configuración de credenciales mediante variables de entorno
- Documentación de arquitectura
- Registro de decisiones de diseño
- Registro de preguntas abiertas