# CLAUDE.md

Guía de contexto para asistentes de IA que trabajen en este repositorio.

## Descripción

Microservicio REST **customers** (Spring Boot 4 / Java 17) encargado de la
gestión de clientes y del cálculo de indicadores de natalidad.

## Stack

- Java 17, Spring Boot 4.1.1 (Spring Framework 7).
- Maven con Maven Wrapper (`mvnw` / `mvnw.cmd`).
- Spring Data JPA + Hibernate, PostgreSQL 16, Flyway.
- Jakarta Bean Validation, springdoc-openapi.
- JUnit 5 + Mockito + AssertJ.

## Comandos

| Acción | Comando |
| --- | --- |
| Compilar | `./mvnw clean compile` |
| Ejecutar | `./mvnw spring-boot:run` |
| Tests | `./mvnw test` |
| Empaquetar | `./mvnw clean package` |
| Levantar DB + API | `docker compose up --build` |

En Windows usa `.\mvnw.cmd` en lugar de `./mvnw`.

## Reglas del repositorio

Las reglas detalladas están en `.claude/rules/`:

- `.claude/rules/architecture.md` — capas y responsabilidades.
- `.claude/rules/coding-standards.md` — estilo y convenciones.
- `.claude/rules/testing.md` — estrategia y convenciones de pruebas.

## Convenciones rápidas

- No exponer entidades JPA: usar DTOs (`record`).
- La lógica de negocio vive en `service`, no en `controller`.
- Los errores se devuelven como RFC 7807 (`ProblemDetail`).
- Toda tabla se crea mediante una migración Flyway versionada.
- Los tests unitarios de servicios no levantan contexto de Spring (Mockito puro).

## Flujo de trabajo

1. Toda tabla nueva se crea mediante una migración Flyway versionada.
2. Todo endpoint nuevo se cubre con al menos una prueba.
3. Antes de finalizar un cambio, ejecutar `./mvnw test`.
