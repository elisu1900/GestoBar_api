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
- **Docker Desktop** corriendo
- Gradle (wrapper incluido)

## `deploy.bat` — gestor Docker

`deploy.bat` es el script de gestión de todos los contenedores del proyecto. Al ejecutarlo pide las credenciales de PostgreSQL una sola vez y presenta un menú principal con tres secciones:

```
[1] Gestionar base de datos     → inicia, para, reinicia, inspecciona o resetea
                                   el contenedor PostgreSQL de forma individual
[2] Gestionar API backend       → construye la imagen Docker de la API y gestiona
                                   su contenedor de forma individual
[3] Stack completo              → levanta o para DB + API juntos con docker-compose
[0] Salir
```

> **Para desarrollar** usa solo `[1]` (BD en Docker) y arranca la API con `gradlew bootRun`.
> El contenedor de la API (`[2]` y `[3]`) es para despliegue o prueba final, no para desarrollo día a día.

---

## Modo desarrollo (recomendado)

Para el ciclo normal de desarrollo la API corre en local con recarga rápida, y solo la base de datos vive en Docker.

### 1. Levantar la base de datos

Ejecuta `deploy.bat`, introduce tus credenciales y selecciona `[1] → [1] Iniciar contenedor`.

### 2. Establecer variables de entorno

```powershell
$env:DB_USER     = "tuusuario"
$env:DB_PASSWORD = "tucontraseña"
```

### 3. Arrancar la API

```powershell
.\gradlew.bat bootRun
```

Flyway ejecuta las migraciones al arrancar:

- `V1__init.sql` — crea todas las tablas
- `V2__datos_prueba.sql` — inserta datos de prueba

La API queda disponible en `http://localhost:8080`.

---

## Despliegue completo con Docker

Para levantar **toda la aplicación en contenedores** (base de datos + API) sin necesidad de JDK en la máquina destino.

### Opción A — mediante `deploy.bat` (recomendado en Windows)

1. Ejecuta `deploy.bat` e introduce tus credenciales.
2. Selecciona `[3] Stack completo → [1] Levantar stack (build + up)`.

El script compila el JAR con Gradle, construye la imagen Docker y levanta ambos contenedores automáticamente.

### Opción B — manualmente desde PowerShell

```powershell
# 1. Compilar el JAR
.\gradlew.bat bootJar -x test

# 2. Establecer credenciales
$env:DB_USER     = "tuusuario"
$env:DB_PASSWORD = "tucontraseña"

# 3. Levantar el stack
docker compose up -d --build
```

### Verificar que todo está en pie

```powershell
docker compose ps
docker logs gestobar-api --tail 20
```

Cuando veas `Started GestoBarApplication` en los logs, la API está lista.

| Servicio | URL |
|---|---|
| API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| PostgreSQL | `localhost:5432` |

### Parar el stack

```powershell
docker compose down          # para y elimina contenedores, conserva datos
docker compose down -v       # ídem + elimina el volumen (borra todos los datos)
```

> Los datos de PostgreSQL se persisten en el volumen Docker `gestobar-pgdata` y sobreviven a reinicios.

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
