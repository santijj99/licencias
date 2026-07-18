# Servidor Central de Licencias — SaaS multi-empresa

API lista para producción con Clean Architecture, JWT, Flyway y PostgreSQL.

## Stack

- Java 17 + Spring Boot 4.1
- PostgreSQL 16
- Hibernate / Spring Data JPA
- Spring Security + JWT + Refresh Token
- BCrypt + AES-GCM (credenciales de DB tenant)
- MapStruct, Flyway, SpringDoc OpenAPI, Actuator

## Arranque rápido

```bash
docker compose up -d
./mvnw spring-boot:run
```

Swagger: http://localhost:8080/swagger-ui.html

Usuario seed:

- email: `admin@licencias.local`
- password: `Admin123!`

## Endpoints clave

| Método | Path | Descripción |
|--------|------|-------------|
| POST | `/api/v1/auth/login` | Login JWT |
| POST | `/api/v1/auth/refresh` | Renovar token |
| POST | `/api/v1/auth/logout` | Revocar refresh |
| POST | `/api/v1/licencias/validar` | Validar licencia + dispositivo |
| CRUD | `/api/v1/empresas` | Empresas |
| CRUD | `/api/v1/planes` | Planes |
| CRUD | `/api/v1/licencias` | Licencias |
| CRUD | `/api/v1/usuarios` | Usuarios globales |
| CRUD | `/api/v1/dispositivos` | Dispositivos |
| CRUD | `/api/v1/conexiones` | Conexión DB por empresa |
| GET | `/api/v1/auditorias` | Auditoría |

## Variables de entorno (producción)

```env
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:postgresql://host:5432/licencias
DB_USERNAME=...
DB_PASSWORD=...
JWT_SECRET=<base64-o-texto-min-32-chars>
AES_KEY=<exactamente-16-24-o-32-bytes>
CORS_ORIGINS=https://tu-frontend
```

## Flujo de validación de licencia

1. El POS/backend de ventas llama `POST /api/v1/licencias/validar`.
2. Se valida estado de licencia, vencimiento, empresa y cupo de dispositivos.
3. Se registra/actualiza el dispositivo.
4. Se responde con datos de conexión (password AES) + JWT de dispositivo.
