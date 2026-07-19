#!/usr/bin/env bash
# Deploy rápido en el VPS (desde la carpeta licencias/)
set -euo pipefail

if [[ ! -f .env ]]; then
  echo "Falta .env — copiá: cp .env.docker.example .env && nano .env"
  exit 1
fi

docker compose pull db || true
docker compose build --no-cache licencias
docker compose up -d
docker compose ps
echo ""
echo "Health:"
curl -fsS "http://127.0.0.1:${LICENCIAS_PUBLISH_PORT:-8080}/actuator/health" || true
echo ""
echo "Listo. Swagger: http://IP_DEL_VPS:8080/swagger-ui.html"
echo "Admin seed: admin@licencias.local / Admin123!"
