# Deploy licencias en VPS (Hostinger / Ubuntu + Docker)

## 1) En el VPS (una sola vez)

```bash
sudo apt update && sudo apt upgrade -y
sudo apt install -y ca-certificates curl git
# Docker
curl -fsSL https://get.docker.com | sudo sh
sudo usermod -aG docker $USER
# cerrá sesión SSH y volvé a entrar
docker --version
docker compose version
```

Abrí puertos en el firewall de Hostinger / ufw:

```bash
sudo ufw allow OpenSSH
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 8080/tcp   # temporal hasta poner nginx+SSL
sudo ufw enable
```

## 2) Subir el código

Opción A — git:

```bash
cd /opt
sudo git clone <TU_REPO_LICENCIAS_O_ATHLAND> athland
cd athland/licencias
```

Opción B — desde tu PC (PowerShell), con SCP:

```powershell
scp -r licencias usuario@IP_VPS:/opt/licencias
```

## 3) Configurar secretos

```bash
cd /opt/licencias   # o la ruta donde quedó
cp .env.docker.example .env
nano .env
```

Cambiá sí o sí:

- `POSTGRES_PASSWORD`
- `JWT_SECRET` (≥ 32 chars)
- `AES_KEY` (32 chars; la misma que usará `apirest`)
- `CORS_ORIGINS` (URLs de `admin_licencias`)

## 4) Levantar

```bash
chmod +x deploy.sh
./deploy.sh
# o:
docker compose up -d --build
```

Comprobar:

```bash
docker compose ps
curl http://127.0.0.1:8080/actuator/health
curl http://IP_VPS:8080/actuator/health
```

- Swagger: `http://IP_VPS:8080/swagger-ui.html`
- Login admin (seed Flyway): `admin@licencias.local` / `Admin123!`  
  **Cambialo en producción.**

## 5) Apuntar admin_licencias

En el FRONT admin, la base URL de la API debe ser:

`http://IP_VPS:8080`  
(o luego `https://licencias.tudominio.com`)

## 6) (Siguiente paso) Nginx + HTTPS

Cuando tengas dominio, se agrega nginx/Caddy delante del puerto 8080. Por ahora con IP:8080 alcanza para probar.

## Comandos útiles

```bash
docker compose logs -f licencias
docker compose logs -f db
docker compose restart licencias
docker compose down          # para (conserva volumen de datos)
docker compose down -v       # BORRA la base — cuidado
```

## Qué incluye este compose

| Servicio     | Contenedor      | Puerto host |
|--------------|-----------------|-------------|
| PostgreSQL 16| `licencias-db`  | 5432        |
| API Spring   | `licencias-api` | 8080        |

Flyway crea el schema y el usuario admin al primer arranque.
