# Estrategia de Pruebas

## 1. Objetivo

La estrategia de pruebas busca validar el comportamiento de la aplicación
manteniendo aislada la lógica de negocio de las dependencias de
infraestructura.

Las pruebas se organizan de acuerdo con los módulos y responsabilidades de
la arquitectura.

La prioridad es validar comportamiento funcional, reglas de negocio,
escenarios de error, persistencia y contratos de API.

---

## 2. Organización de Pruebas

Las pruebas se organizan por módulo:

```text
src/test/java/com/liverpool/appsales/exam
├── customer
│   ├── application
│   ├── infrastructure
│   └── presentation
├── order
│   ├── application
│   ├── infrastructure
│   └── presentation
├── delivery
│   ├── application
│   ├── infrastructure
│   └── presentation
├── item
│   ├── application
│   └── infrastructure
└── search
    ├── application
    └── presentation
```

La estructura puede ajustarse cuando la implementación determine qué
pruebas son realmente necesarias en cada componente.

---

## 3. Tipos de Pruebas

### 3.1 Pruebas Unitarias

Se utilizan para validar:

- Reglas de negocio.
- Casos de uso.
- Transformaciones.
- Lógica de normalización y búsqueda.
- Asociación entre Customer y Order.
- Manejo de escenarios exitosos y errores.

Las pruebas unitarias deben ejecutarse sin depender de MongoDB.

Las dependencias de infraestructura se aíslan mediante mocks o dobles de
prueba cuando es necesario.

---

### 3.2 Pruebas de Integración

Se utilizan cuando es necesario validar la interacción entre componentes
reales de infraestructura.

Principalmente:

- Persistencia con MongoDB.
- Configuración de infraestructura.
- Adaptadores de persistencia.
- Conversión entre modelos de dominio y documentos de MongoDB.

Las pruebas de integración no sustituyen las pruebas unitarias.

---

### 3.3 Pruebas de Controladores

Se utilizan para validar el comportamiento de los endpoints REST,
incluyendo:

- Requests válidos.
- Validación de entrada.
- Códigos HTTP.
- Estructura de respuestas.
- Manejo de errores.
- Contratos de los endpoints.

Los controladores se prueban de forma aislada utilizando las capas de
aplicación como dependencias simuladas.

---

## 4. Estrategia de Mocking

Los mocks o dobles de prueba se utilizan principalmente en las pruebas
unitarias de la capa de aplicación.

Por ejemplo:

```text
Use Case
   │
   ├── CustomerRepository mock
   │
   └── OrderRepository mock
```

Esto permite probar las reglas del caso de uso sin depender directamente de
MongoDB.

Por ejemplo, la actualización de Customer puede probarse verificando que:

1. El cliente exista.
2. Los pedidos proporcionados pertenezcan al mismo `userId`.
3. Los `orderRef` válidos sean almacenados.
4. Una asociación inválida sea rechazada.
5. Una lista vacía elimine las asociaciones existentes.
6. Un valor `null` preserve las asociaciones existentes.

Los adaptadores reales de persistencia se validan mediante pruebas
específicas de infraestructura cuando corresponda.

---

## 5. Áreas de Prueba

### Customer

Se prueban:

- Creación de clientes.
- Consulta de clientes.
- Actualización de clientes.
- Eliminación de clientes.
- Validaciones relevantes.
- Inicialización de `orders` como lista vacía.
- Asociación de `orderRef`.
- Validación de que los pedidos pertenezcan al `userId` del cliente.
- Conservación de pedidos cuando `orders` es `null`.
- Eliminación de asociaciones cuando `orders` es una lista vacía.
- Comportamiento cuando el cliente no existe.

---

### Order

Se prueban:

- Creación de pedidos.
- Consulta de pedidos.
- Actualización de pedidos.
- Eliminación de pedidos.
- Uso de `orderRef`.
- Asociación mediante `userId`.
- Consulta de pedidos por `userId`.
- Manejo de pedidos inexistentes.
- Transformación entre documentos de persistencia y modelos de dominio.

Los datos de `/pedidos` se consideran datos de referencia utilizados para
definir y poblar los modelos internos de la aplicación.

No se realizan pruebas de integración contra el servicio externo porque la
aplicación no implementa un consumidor HTTP para dicha URL.

---

### Delivery

Se prueban:

- Creación de entregas.
- Consulta de entregas.
- Actualización de entregas.
- Eliminación de entregas.
- Asociación mediante `orderRef`.
- Validaciones relevantes.
- Manejo de entregas inexistentes.
- Persistencia y recuperación desde MongoDB.

---

### Item

Se prueban:

- Persistencia de información de productos.
- Consulta de items.
- Asociación mediante `itemId`.
- Uso de `displayName` para búsqueda.
- Transformación entre documento de persistencia y modelo de dominio.

Los datos de `/items` se consideran datos de referencia utilizados para
definir y poblar la información interna de Item.

No se realizan pruebas de integración contra el servicio externo porque la
aplicación no implementa un consumidor HTTP para dicha URL.

---

### Search

Se prueban:

- Búsqueda por `orderRef`.
- Búsqueda por `orderStatus`.
- Búsqueda por `storeName`.
- Búsqueda de productos mediante `displayName`.
- Normalización de mayúsculas y minúsculas.
- Normalización de acentos.
- Normalización de puntuación.
- Normalización de espacios.
- Tolerancia a errores menores de escritura.
- Búsqueda combinando filtros de pedido y `displayName`.
- Relación entre items y pedidos.

La estrategia concreta para la tolerancia a errores se valida mediante casos
de prueba que representan los escenarios requeridos.

---

### Infrastructure

Se prueban:

- Adaptadores de MongoDB.
- Conversión entre modelos de dominio y documentos de persistencia.
- Persistencia y recuperación de información.
- Manejo de errores de infraestructura.
- Consultas específicas de MongoDB utilizadas por los repositorios.

No se incluyen adaptadores HTTP para `/pedidos` ni `/items`, debido a que
dichas URLs se consideran datos de referencia para el proyecto.

---

### Presentation

Se prueban:

- Validación de requests.
- Códigos HTTP.
- Respuestas exitosas.
- Manejo de errores.
- Contratos de los endpoints definidos.
- Serialización y deserialización de DTOs.

---

## 6. Cobertura

La cobertura se utiliza como indicador de calidad de las pruebas, pero no
como único criterio de calidad.

La prioridad es cubrir:

1. Reglas de negocio.
2. Casos de uso.
3. Transformaciones de datos.
4. Escenarios de error.
5. Persistencia relevante.
6. Contratos de API.

Se evita escribir pruebas únicamente para incrementar un porcentaje de
cobertura sin aportar validación significativa.

---

## 7. Ejecución

Las pruebas pueden ejecutarse mediante Maven:

```bash
./mvnw test
```

Para ejecutar el proceso completo de validación:

```bash
./mvnw clean verify
```

La ejecución completa de pruebas y validaciones forma parte del flujo normal
de desarrollo.

---

## 8. Principios

La estrategia de pruebas sigue los siguientes principios:

- Las pruebas unitarias deben ser rápidas y aisladas.
- Las dependencias de infraestructura deben aislarse mediante dobles de
  prueba cuando corresponda.
- Las pruebas de integración deben utilizarse para validar integración real,
  no como sustituto de las pruebas unitarias.
- Los casos de prueba deben representar comportamiento esperado y escenarios
  de error.
- Los casos de prueba deben ser mantenibles y legibles.
- Las pruebas deben validar comportamiento y no detalles de implementación
  innecesarios.
- Las reglas de negocio importantes deben tener cobertura mediante pruebas
  unitarias.