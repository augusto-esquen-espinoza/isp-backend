# Regla: Arquitectura

## Capas

```
controller  ->  service (interface + impl)  ->  repository  ->  PostgreSQL
```

| Capa | Paquete | Responsabilidad |
| --- | --- | --- |
| Controller | `com.isp.customers.controller` | HTTP: routing, validación de entrada, códigos de estado. Sin lógica de negocio. |
| Service | `com.isp.customers.service` | Lógica de negocio y límites transaccionales. |
| Repository | `com.isp.customers.repository` | Acceso a datos. |
| DTO | `com.isp.customers.dto` | Contratos de entrada/salida (records). |
| Entity | `com.isp.customers.entity` | Mapeo JPA. Nunca se expone en la API. |
| Mapper | `com.isp.customers.mapper` | Conversión entidad <-> DTO. |
| Exception | `com.isp.customers.exception` | Excepciones de dominio y manejo global. |
| Config | `com.isp.customers.config` | CORS, OpenAPI y configuración transversal. |

## Reglas

- Cada servicio se declara como interface + implementación (`XxxService` /
  `XxxServiceImpl`) para facilitar el testeo y el desacoplamiento.
- La inyección de dependencias es por constructor. No usar `@Autowired` en
  campos.
- Anotar la clase de servicio con `@Transactional(readOnly = true)` y las
  operaciones de escritura con `@Transactional`.
- No acceder a `EntityManager` desde controllers.
- Toda respuesta de error se construye con `ProblemDetail` (RFC 7807) desde
  `GlobalExceptionHandler`.
- Preferir consultas derivadas de Spring Data; usar `@Query` (JPQL) cuando la
  consulta no sea expresable como método derivado.
