# GestoBar — API

API REST backend del sistema de gestión de bares y restaurantes **GestoBar**. Desarrollada con Spring Boot 3 y PostgreSQL, gestiona autenticación, productos, mesas, tickets de pedido y balance de caja diario.

## Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5 |
| Seguridad | Spring Security (sesión con cookie `JSESSIONID`) |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL 16 (esquema `gestobar`) |
| Migraciones | Flyway |
| Mapeo | MapStruct |
| Boilerplate | Lombok |
| Documentación | SpringDoc OpenAPI (Swagger UI) |
| Build | Gradle (Kotlin DSL) |

## Requisitos previos

- **JDK 21** instalado y en el `PATH`
- **Docker** (para levantar PostgreSQL)
- Gradle (wrapper incluido)

## Puesta en marcha

### 1. Levantar la base de datos

```bat
deploy.bat
```

Inicia un contenedor PostgreSQL 16 en el puerto `5432` y solicita `DB_USER` / `DB_PASSWORD`.

### 2. Establecer variables de entorno

```powershell
$env:DB_USER     = "tuusuario"
$env:DB_PASSWORD = "tucontraseña"
```

### 3. Arrancar la API

```bash
./gradlew bootRun
```

Flyway ejecuta las migraciones al arrancar:

- `V1__init.sql` — crea todas las tablas
- `V2__datos_prueba.sql` — inserta datos de prueba

La API queda disponible en `http://localhost:8080`.

## Endpoints

Ruta base: `/api`

| Módulo | Ruta | Autorización |
|---|---|---|
| Auth | `/api/auth/login`, `/api/auth/logout` | Pública |
| Usuarios | `/api/v1/users/**` | `ROLE_ADMIN` |
| Productos | `/api/v1/products/**` | Autenticado |
| Categorías | `/api/v1/categories/**` | Autenticado |
| Mesas | `/api/v1/tables/**` | Autenticado |
| Tickets | `/api/v1/tickets/**` | Autenticado |
| Balance | `/api/v1/balance/**` | Autenticado |

Documentación interactiva: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Esquema de base de datos

```
users               — cuentas del personal (roles ADMIN / WAITER)
categories          — categorías de productos
products            — artículos del menú con precio de venta y coste
restaurant_tables   — mesas del local con capacidad
tickets             — comandas abiertas/cerradas, vinculadas a mesa y usuario
ticket_details      — líneas de cada comanda (producto + cantidad + precio unitario)
```

## Credenciales de prueba

> Las contraseñas son BCrypt de `password123`

| Usuario | Rol |
|---|---|
| `Admin` | ADMIN |
| `Carlos` | WAITER |
| `María` | WAITER |
| `Pedro` | WAITER |

## Estructura del proyecto

```
src/main/java/com/elias/GestoBar/
├── controller/     Endpoints HTTP (@RestController)
├── service/        Lógica de negocio
├── repository/     Interfaces Spring Data JPA
├── mapper/         Mappers MapStruct (entidad ↔ DTO)
├── dto/            Records de petición / respuesta
├── model/          Entidades JPA
├── security/       SecurityConfig, CustomUserDetailsService
└── exception/      GlobalExceptionHandler, ResourceNotFoundException
```

## Otros comandos

```bash
# Compilar JAR
./gradlew build

# Ejecutar tests
./gradlew test
```
