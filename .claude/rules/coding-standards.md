# Regla: Estándares de código

## Estilo

- Formato de código: 4 espacios, una declaración por línea.
- Nombres de clases en `PascalCase`, métodos y variables en `camelCase`,
  constantes en `UPPER_SNAKE_CASE`.
- Paquetes en minúsculas y sin abreviaturas.
- Los DTOs se declaran como `record` inmutables.
- Las entidades usan Lombok (`@Getter`, `@Setter`, `@Builder`,
  `@NoArgsConstructor`, `@AllArgsConstructor`) y no usan `@Data`.

## Validación

- La validación de entrada se declara con anotaciones de Jakarta Bean Validation
  en los DTOs de request.
- Los controllers reciben el body con `@Valid @RequestBody`.
- La fecha de creación es responsabilidad del servidor (`@CreationTimestamp`);
  nunca la envía el cliente.

## Base de datos

- Toda tabla se crea con una migración Flyway versionada
  (`V<version>__<descripcion>.sql`).
- `spring.jpa.hibernate.ddl-auto=validate`: Hibernate valida el esquema, Flyway
  lo crea. No usar `update`/`create`.
- Los campos únicos se declaran tanto en la migración como en la entidad
  (`@UniqueConstraint`).

## API

- Rutas bajo `/api`.
- Códigos de estado explícitos (`201` en creación, `200` en consulta,
  `400`/`409`/`500` en error).
- Documentar los endpoints con anotaciones de springdoc (`@Operation`, `@Tag`).

## Prohibiciones

- No usar `System.out.println`; usar logging.
- No dejar `TODO` sin contexto ni código comentado.
- No exponer entidades JPA en los controllers.
- No introducir lógica de negocio en los controllers.
