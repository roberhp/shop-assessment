Estrategia de Pruebas

1. Objetivo

La estrategia de pruebas busca validar el comportamiento de la aplicación manteniendo aislada la lógica de negocio de las dependencias externas.

Las pruebas se organizarán de acuerdo con los módulos y responsabilidades de la arquitectura.

⸻

2. Organización de Pruebas

Las pruebas se organizarán por módulo:
```
src/test/java/com/liverpool/appsales/exam
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
└── search
    ├── domain
    ├── application
    ├── infrastructure
    └── presentation
```

La estructura podrá ajustarse cuando la implementación determine qué pruebas son realmente necesarias en cada componente.

⸻

3. Tipos de Pruebas

3.1 Pruebas Unitarias

Se utilizarán para validar:

* Reglas de dominio.
* Casos de uso.
* Transformaciones.
* Lógica de normalización y búsqueda.
* Manejo de escenarios exitosos y errores.

Las pruebas unitarias deberán ejecutarse sin depender de MongoDB ni de los servicios externos.

⸻

3.2 Pruebas de Integración

Se utilizarán cuando sea necesario validar la interacción entre componentes reales de infraestructura.

Principalmente:

* Persistencia con MongoDB.
* Configuración de infraestructura.
* Adaptadores cuya integración no pueda validarse adecuadamente mediante pruebas unitarias.

Las pruebas de integración no sustituirán las pruebas unitarias.

⸻

3.3 Pruebas de Controladores

Se utilizarán para validar el comportamiento de los endpoints REST, incluyendo:

* Requests válidos.
* Validación de entrada.
* Códigos HTTP.
* Estructura de respuestas.
* Manejo de errores.

⸻

4. Estrategia de Mocking

Los mocks o dobles de prueba se utilizarán principalmente en las pruebas unitarias de la capa de aplicación.

Por ejemplo:

Use Case
   │
   ├── CustomerRepository mock
   │
   └── ExternalProvider mock

Esto permite probar la lógica del caso de uso sin depender de:

* MongoDB.
* APIs externas.
* Red.
* Configuración externa.

Los adaptadores reales serán probados mediante pruebas específicas de infraestructura cuando corresponda.

⸻

5. Áreas de Prueba

Customer

Se probarán:

* Creación de clientes.
* Consulta de clientes.
* Actualización de clientes.
* Validaciones relevantes.
* Asociación de orderRef.
* Comportamiento cuando el cliente no existe.

Order

Se probarán:

* Consulta de pedidos.
* Uso de orderRef.
* Transformación del modelo externo al modelo interno.
* Manejo de respuestas inválidas o errores de la integración.

La transformación de orderStatus hacia estimateDeliveryDate se probará una vez definido el formato real proporcionado por el servicio externo.

Search

Se probarán:

* Búsqueda por orderRef.
* Búsqueda por orderStatus.
* Búsqueda por storeName.
* Búsqueda de productos mediante displayName.
* Normalización de mayúsculas y minúsculas.
* Normalización de acentos.
* Eliminación de signos de puntuación relevantes.
* Tolerancia a errores menores de escritura.

La estrategia concreta para la tolerancia a errores se validará mediante casos de prueba que representen los escenarios requeridos.

Infrastructure

Se probarán:

* Adaptador de MongoDB.
* Adaptador de /pedidos.
* Adaptador de /items.
* Transformaciones entre modelos externos y modelos internos.
* Manejo de errores de infraestructura.

Presentation

Se probarán:

* Validación de requests.
* Códigos HTTP.
* Respuestas exitosas.
* Manejo de errores.
* Contratos de los endpoints definidos.

⸻

6. Cobertura

La cobertura será utilizada como indicador de calidad de las pruebas, pero no como único criterio de calidad.

La prioridad será cubrir:

1. Reglas de negocio.
2. Casos de uso.
3. Transformaciones de datos.
4. Escenarios de error.
5. Integraciones relevantes.
6. Contratos de API.

Se evitará escribir pruebas únicamente para incrementar un porcentaje de cobertura sin aportar validación significativa.

⸻

7. Ejecución

Las pruebas podrán ejecutarse mediante Maven:

./mvnw test

La ejecución completa de pruebas deberá formar parte del flujo normal de desarrollo.

⸻

8. Principios

La estrategia de pruebas seguirá los siguientes principios:

* Las pruebas unitarias deben ser rápidas y aisladas.
* Las dependencias externas deben aislarse mediante puertos y dobles de prueba cuando corresponda.
* Las pruebas de integración deben utilizarse para validar integración real, no como sustituto de las pruebas unitarias.
* Los casos de prueba deben representar comportamiento esperado y escenarios de error.
* Las pruebas deben ser mantenibles y legibles.