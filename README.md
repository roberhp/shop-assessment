# Backend - Gestión de Pedidos

API REST desarrollada como solución al examen técnico Backend para la gestión de clientes, pedidos, entregas y búsqueda de información de pedidos y productos.

La aplicación está desarrollada con Java y Spring Boot, utiliza MongoDB como base de datos y sigue una arquitectura de Modular Monolith con una organización basada en Clean Architecture pragmática.

## Tecnologías

- Java 17
- Spring Boot
- Maven
- MongoDB
- Docker
- Docker Compose
- OpenAPI / Swagger
- JUnit
- Mockito
- CheckStyle

## Arquitectura

El proyecto está organizado por módulos funcionales:

- `customer`: gestión de clientes.
- `order`: gestión de pedidos.
- `delivery`: gestión de entregas.
- `item`: información de productos asociados a pedidos.
- `search`: búsqueda de pedidos y productos.
- `presentation`: manejo global de excepciones.

Cada módulo utiliza una separación por responsabilidades:

- `domain`: entidades y modelos de dominio.
- `application`: casos de uso y abstracciones necesarias.
- `infrastructure`: persistencia y detalles técnicos.
- `presentation`: controladores REST y DTOs.

La dependencia principal entre capas sigue el flujo:

```text
Presentation → Application → Domain
```

Las implementaciones de infraestructura proporcionan las capacidades técnicas necesarias, como la persistencia en MongoDB.

Para mayor detalle consultar:

- `docs/architecture.md`
- `docs/decisions.md`
- `docs/open-questions.md`

## Requisitos

Para ejecutar el proyecto con Docker:

- Docker
- Docker Compose

Para ejecutarlo directamente:

- Java 17+
- Maven o Maven Wrapper

## Ejecución con Docker

La forma recomendada de ejecutar el proyecto es mediante Docker Compose.

### 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd <NOMBRE_DEL_REPOSITORIO>
```

### 2. Levantar la aplicación

```bash
docker compose up --build
```

Esto levanta:

- La API Spring Boot.
- Una instancia de MongoDB.
- La configuración necesaria para conectar ambos servicios.

La API estará disponible en:

`http://localhost:8080`

### 3. Detener la aplicación

```bash
docker compose down
```

Los datos de MongoDB se mantienen en un volumen Docker.

Para eliminar también los datos persistidos:

```bash
docker compose down -v
```

## Ejecución local sin Docker

También es posible ejecutar la aplicación directamente desde Maven.

Primero se debe disponer de una instancia de MongoDB ejecutándose localmente.

La configuración por defecto utiliza:

`mongodb://localhost:27017/liverpool_exam`

Después ejecutar:

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en:

`http://localhost:8080`


## Acceso a MongoDB

Cuando la aplicación se ejecuta mediante Docker Compose, MongoDB está disponible dentro del contenedor `liverpool-exam-mongodb`.

Para conectarse directamente a MongoDB desde la terminal, primero asegúrate de que los servicios estén levantados:

```bash
docker compose up -d
```

Después, abre una sesión de MongoDB utilizando `mongosh`:

```bash
docker exec -it liverpool-exam-mongodb mongosh -u liverpool -p liverpool_exam --authenticationDatabase admin
```

Una vez dentro de MongoDB, selecciona la base de datos de la aplicación:

```javascript
use liverpool_exam
```

Para consultar las colecciones disponibles:

```javascript
show collections
```

### Consultas de ejemplo

Consultar todos los clientes:

```javascript
db.customers.find().pretty()
```

Consultar un cliente específico mediante su `userId`:

```javascript
db.customers.findOne({
    userId: "75c97531-abf5-4524-8107-90aa48d08efc"
})
```

Consultar un pedido específico mediante su `orderRef`:

```javascript
db.orders.findOne({
    orderRef: "3010091676"
})
```

Consultar los pedidos de una tienda:

```javascript
db.orders.find({
    storeName: "L  SANTA FE"
}).pretty()
```

Consultar productos cuyo nombre contenga "Levi":

```javascript
db.items.find({
    displayName: /Levi/i
}).pretty()
```

Para salir de `mongosh`:

```javascript
exit
```


## Datos de prueba

La aplicación incluye un inicializador de datos de prueba.

Al iniciar la aplicación se crean datos de ejemplo para:

- Clientes
- Pedidos
- Items
- Entregas

El seed está habilitado por defecto mediante:

`app.seed.enabled=true`

Para deshabilitarlo se puede utilizar:

```bash
APP_SEED_ENABLED=false
```

Los datos de prueba están basados en la información proporcionada para el ejercicio técnico.

## Documentación de la API

La documentación de los endpoints está disponible mediante Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

Desde Swagger se pueden consultar y probar los endpoints disponibles.

## Principales endpoints

### Clientes

```text
POST   /customers
GET    /customers/{userId}
PUT    /customers/{userId}
DELETE /customers/{userId}
```

Los clientes contienen:

- `userId`
- Nombre
- Apellido paterno
- Apellido materno
- Correo electrónico
- `orders`

El campo `orders` contiene los números de pedido (`orderRef`) asociados al cliente.

### Pedidos

```text
POST   /orders
GET    /orders/{orderRef}
PUT    /orders/{orderRef}
DELETE /orders/{orderRef}
```

### Entregas

```text
POST   /deliveries
GET    /deliveries/{deliveryId}
PUT    /deliveries/{deliveryId}
DELETE /deliveries/{deliveryId}
```

### Búsqueda

La búsqueda permite utilizar los siguientes criterios:

- `orderRef`
- `orderStatus`
- `storeName`
- `displayName`

La búsqueda de texto normaliza:

- Mayúsculas y minúsculas.
- Acentos.
- Signos de puntuación.
- Espacios.

También se considera una tolerancia para errores menores de escritura.

Consultar Swagger para conocer los parámetros y ejemplos disponibles.

## Pruebas

Para ejecutar las pruebas:

```bash
./mvnw test
```

Para ejecutar la validación completa del proyecto:

```bash
./mvnw clean verify
```

Esta validación incluye las pruebas automatizadas y CheckStyle.

## CheckStyle

El proyecto utiliza CheckStyle para validar el estilo del código.

La validación se ejecuta mediante:

```bash
./mvnw verify
```

El proyecto debe finalizar sin violaciones de CheckStyle.

## Variables de configuración

La aplicación permite configurar la conexión a MongoDB mediante:

```text
SPRING_DATA_MONGODB_URI
```

Y el seed de datos mediante:

```text
APP_SEED_ENABLED
```

Para Docker Compose la configuración necesaria ya está definida en el archivo:

`docker-compose.yml`

El archivo `.env.example` contiene un ejemplo de configuración.

Los archivos con información de entorno local no deben incluirse en el repositorio.

## Estructura del proyecto

```text
src/
├── main/
│   ├── java/
│   │   └── com/liverpool/appsales/exam/
│   │       ├── customer/
│   │       ├── order/
│   │       ├── delivery/
│   │       ├── item/
│   │       ├── search/
│   │       └── presentation/
│   │
│   └── resources/
│       └── application.properties
│
└── test/
    └── java/
        └── com/liverpool/appsales/exam/

docs/
├── architecture.md
├── decisions.md
└── open-questions.md
```

## Documentación adicional

### Arquitectura

`docs/architecture.md`

Describe la arquitectura utilizada, la organización de módulos y la separación de responsabilidades.

### Decisiones de diseño

`docs/decisions.md`

Contiene las principales decisiones tomadas durante el desarrollo y el motivo de cada una.

### Preguntas abiertas

`docs/open-questions.md`

Contiene aspectos del requerimiento que no estaban completamente definidos en el ejercicio y que podrían requerir confirmación.

## Diseño y consideraciones

La solución evita el patrón MVC solicitado en el ejercicio.

Se utiliza una arquitectura modular con separación entre dominio, casos de uso, presentación e infraestructura.

Algunas decisiones de implementación se mantienen deliberadamente simples cuando el requerimiento no especifica un comportamiento más complejo. Estas decisiones están documentadas en `docs/decisions.md`.

## Repositorio

Repositorio:

`<URL_DEL_REPOSITORIO>`

## Estado de validación

La solución ha sido validada mediante:

```bash
./mvnw clean verify
```

Incluyendo:

- Pruebas automatizadas.
- Validación de CheckStyle.
- Compilación del proyecto.

## Licencia

Proyecto desarrollado como parte de un ejercicio técnico.