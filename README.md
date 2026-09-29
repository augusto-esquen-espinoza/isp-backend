# ISP Customers — Microservicio (Backend)

Microservicio REST para la gestión de clientes y el cálculo de indicadores de
natalidad. Forma parte del Reto Técnico I.

## Tabla de contenido

- [Descripción](#descripción)
- [Stack tecnológico](#stack-tecnológico)
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Configuración](#configuración)
- [API REST](#api-rest)
- [Indicadores](#indicadores)
- [Colección de Postman](#colección-de-postman)
- [Pruebas](#pruebas)
- [Estructura del proyecto](#estructura-del-proyecto)

## Descripción

El microservicio **customers** expone dos recursos:

1. **Customers**: creación de clientes y consulta por `dni`, `email` o sin
   filtros.
2. **Indicators**: indicadores de natalidad calculados sobre la fecha de
   nacimiento de los clientes.

## Stack tecnológico

| Componente | Tecnología |
| --- | --- |
| Lenguaje | Java 17 |
| Framework | Spring Boot 4.1.1 (Spring Framework 7) |
| Build | Maven (Maven Wrapper incluido) |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL 16 |
| Migraciones | Flyway |
| Validación | Jakarta Bean Validation |
| Documentación API | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5 + Mockito + AssertJ |
| Contenedores | Docker / Docker Compose |

## Arquitectura

Arquitectura en capas con separación de responsabilidades:

```
Controller  ->  Service (interface + impl)  ->  Repository  ->  PostgreSQL
     |                |
     v                v
   DTOs          Mapper / Entity
```

- Los **controllers** sólo orquestan la petición HTTP y validan la entrada.
- Los **services** contienen la lógica de negocio y son la única capa
  transaccional.
- Los **repositories** encapsulan el acceso a datos.
- Se usan **DTOs** (Java records) para no exponer la entidad.
- Los errores se traducen a respuestas **RFC 7807 (Problem Details)**.

## Requisitos

- Docker y Docker Compose **o** Java 17+ (para ejecución local).
- No se requiere Maven instalado: el proyecto incluye el Maven Wrapper.

## Instalación y ejecución

### Opción A — Docker Compose (recomendada)

Levanta la base de datos y el microservicio juntos:

```bash
docker compose up --build
```

La API queda disponible en `http://localhost:8080`.

### Opción B — Ejecución local

1. Levanta únicamente la base de datos:

   ```bash
   docker compose up -d db
   ```

2. Ejecuta la aplicación con el Maven Wrapper:

   - Linux/macOS:

     ```bash
     ./mvnw spring-boot:run
     ```

   - Windows (PowerShell / CMD):

     ```powershell
     .\mvnw.cmd spring-boot:run
     ```

Flyway aplica automáticamente la migración
`src/main/resources/db/migration/V1__create_customers_table.sql` al iniciar.

## Configuración

Todas las variables tienen valores por defecto para desarrollo local:

| Variable | Descripción | Default |
| --- | --- | --- |
| `DB_URL` | JDBC URL de PostgreSQL | `jdbc:postgresql://localhost:5432/isp_customers` |
| `DB_USER` | Usuario de la base de datos | `isp_user` |
| `DB_PASSWORD` | Contraseña de la base de datos | `isp_pass` |
| `SERVER_PORT` | Puerto HTTP | `8080` |

## Migraciones

El esquema se versiona con Flyway en `src/main/resources/db/migration`. La
aplicación aplica las migraciones pendientes automáticamente al iniciar.

Para aplicar **solo las migraciones** sin reiniciar la aplicación (por ejemplo,
tras agregar un nuevo archivo `V2__...sql`), tienes dos opciones.

### Con Java instalado (Maven Wrapper)

```bash
# Linux/macOS
./mvnw flyway:migrate

# Windows
.\mvnw.cmd flyway:migrate
```

### Sin Java local (Docker)

Si no tienes Java en la máquina pero sí Docker, usa el servicio `migrate`
(definido con `profiles` en `docker-compose.yml`):

```bash
docker compose up -d db
docker compose --profile tools run --rm migrate
```

Para otro objetivo de Flyway, sobreescribe el comando:

```bash
docker compose --profile tools run --rm migrate mvn -B flyway:info "-Ddb.url=jdbc:postgresql://db:5432/isp_customers"
```

Otros comandos útiles (con Maven local):

| Comando | Descripción |
| --- | --- |
| `./mvnw flyway:info` | Muestra las migraciones aplicadas y pendientes. |
| `./mvnw flyway:migrate` | Aplica las migraciones pendientes. |
| `./mvnw flyway:validate` | Valida que el historial coincida con los archivos. |
| `./mvnw flyway:clean` | Elimina los objetos del esquema (usar con cuidado). |

Por defecto apunta a `jdbc:postgresql://localhost:5432/isp_customers`. Puedes
apuntar a otra base de datos sin tocar el `pom.xml`:

```bash
./mvnw flyway:migrate -Ddb.url=jdbc:postgresql://localhost:5432/otra_bd -Ddb.user=usuario -Ddb.password=clave
```

> La base de datos debe existir: Flyway crea las tablas dentro de ella, no la
> base de datos en sí.

## API REST

Base URL: `http://localhost:8080`

### 1. Crear cliente

`POST /api/customers`

```json
{
  "nombre": "Augusto",
  "apellido": "Esquen",
  "email": "augusto.esquen@example.com",
  "dni": "12345678",
  "fechaNacimiento": "1995-05-20"
}
```

Respuesta `201 Created`:

```json
{
  "id": 1,
  "nombre": "Augusto",
  "apellido": "Esquen",
  "email": "augusto.esquen@example.com",
  "dni": "12345678",
  "fechaCreacion": "2026-01-01T10:00:00",
  "fechaNacimiento": "1995-05-20"
}
```

Errores:

- `400 Bad Request`: payload inválido (Problem Detail con `errors`).
- `409 Conflict`: `dni` o `email` ya registrados.

### 2. Consultar clientes

`GET /api/customers` — sin filtros.
`GET /api/customers?dni=12345678` — por DNI.
`GET /api/customers?email=augusto.esquen@example.com` — por email.
`GET /api/customers?dni=12345678&email=...` — por ambos.

Respuesta `200 OK`: arreglo de clientes.

### 3. Indicadores

`GET /api/indicators`

```json
{
  "birthsByMonthYear": [
    { "year": 1995, "month": 5, "count": 1 },
    { "year": 1990, "month": 1, "count": 3 }
  ],
  "peakMonthYear": { "year": 1990, "month": 1, "count": 3 },
  "lowestMonthYear": { "year": 1995, "month": 5, "count": 1 },
  "birthRateByMonth": [
    { "month": 1, "monthName": "Enero", "count": 3, "rate": 75.00 },
    { "month": 2, "monthName": "Febrero", "count": 0, "rate": 0.00 }
  ],
  "totalCustomers": 4
}
```

Documentación interactiva (Swagger UI): `http://localhost:8080/swagger-ui.html`.

## Indicadores

- **Cantidad de clientes nacidos por mes/año**: agrupación por año y mes de
  `fechaNacimiento`.
- **Mes/Año con mayor cantidad**: registro con el `count` máximo.
- **Mes/Año con menor cantidad**: registro con el `count` mínimo entre los
  meses que registran al menos un nacimiento.
- **Tasa de natalidad de cada mes**: para cada mes calendario (1–12) se
  calcularon los nacimientos acumulados de todos los años y se dividieron entre
  el total de clientes, expresado como porcentaje con dos decimales:

  ```
  tasa(mes) = nacimientos(mes) / totalClientes * 100
  ```

## Colección de Postman

Importa `postman/ISP_Customers.postman_collection.json` en Postman. La colección
incluye:

- **Customers**
  - Create customer (con tests de respuesta 201).
  - Get all customers (sin filtros).
  - Get customers by dni.
  - Get customers by email.
- **Indicators**
  - Get birth indicators.

Variable de colección: `baseUrl` (`http://localhost:8080`).

## Pruebas

```bash
# Linux/macOS
./mvnw test

# Windows
.\mvnw.cmd test
```

Con Docker (sin Java local):

```bash
docker run --rm -v "$PWD:/app" -w /app maven:3.9-eclipse-temurin-17 mvn -B test
```

Pruebas incluidas:

- `CustomerServiceImplTest`: creación, duplicados y consultas.
- `IndicatorServiceImplTest`: cálculo de indicadores (incluye caso sin datos).
- `CustomerControllerTest`: slice web con `@WebMvcTest` (201, 400 y 200).

## Estructura del proyecto

```
src/main/java/com/isp/customers/
├── CustomersApplication.java
├── config/          # CORS y OpenAPI
├── controller/      # CustomerController, IndicatorController
├── dto/             # Requests y responses (records)
├── entity/          # Customer
├── exception/       # Excepciones y manejo global (Problem Details)
├── mapper/          # CustomerMapper
├── repository/      # CustomerRepository
└── service/         # Interfaces e implementaciones

src/main/resources/
├── application.yml
└── db/migration/V1__create_customers_table.sql
```
