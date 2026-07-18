-- V1__schema_inicial.sql
-- Esquema inicial del servidor central de licencias

CREATE TABLE planes (
    id                      BIGSERIAL PRIMARY KEY,
    nombre                  VARCHAR(100) NOT NULL UNIQUE,
    precio                  NUMERIC(14, 2) NOT NULL,
    cantidad_usuarios       INTEGER NOT NULL,
    cantidad_sucursales     INTEGER NOT NULL,
    cantidad_dispositivos   INTEGER NOT NULL,
    descripcion             VARCHAR(500),
    activo                  BOOLEAN NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted                 BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at              TIMESTAMPTZ
);

CREATE TABLE empresas (
    id                  BIGSERIAL PRIMARY KEY,
    nombre              VARCHAR(200) NOT NULL,
    cuit                VARCHAR(20) NOT NULL,
    email               VARCHAR(150) NOT NULL,
    telefono            VARCHAR(50),
    estado              VARCHAR(30) NOT NULL,
    plan_id             BIGINT NOT NULL REFERENCES planes(id),
    fecha_alta          DATE NOT NULL,
    fecha_vencimiento   DATE NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted             BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMPTZ,
    CONSTRAINT uq_empresas_cuit UNIQUE (cuit)
);

CREATE INDEX idx_empresas_estado ON empresas(estado) WHERE deleted = FALSE;
CREATE INDEX idx_empresas_plan ON empresas(plan_id) WHERE deleted = FALSE;
CREATE INDEX idx_empresas_vencimiento ON empresas(fecha_vencimiento) WHERE deleted = FALSE;

CREATE TABLE licencias (
    id                              BIGSERIAL PRIMARY KEY,
    codigo                          VARCHAR(64) NOT NULL,
    empresa_id                      BIGINT NOT NULL REFERENCES empresas(id),
    estado                          VARCHAR(30) NOT NULL,
    cantidad_maxima_dispositivos    INTEGER NOT NULL,
    cantidad_maxima_sucursales      INTEGER NOT NULL,
    fecha_creacion                  TIMESTAMPTZ NOT NULL,
    fecha_vencimiento               DATE NOT NULL,
    created_at                      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted                         BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at                      TIMESTAMPTZ,
    CONSTRAINT uq_licencias_codigo UNIQUE (codigo)
);

CREATE INDEX idx_licencias_empresa ON licencias(empresa_id) WHERE deleted = FALSE;
CREATE INDEX idx_licencias_estado ON licencias(estado) WHERE deleted = FALSE;
CREATE INDEX idx_licencias_vencimiento ON licencias(fecha_vencimiento) WHERE deleted = FALSE;

CREATE TABLE usuarios_globales (
    id              BIGSERIAL PRIMARY KEY,
    nombre          VARCHAR(150) NOT NULL,
    email           VARCHAR(150) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    rol             VARCHAR(30) NOT NULL,
    empresa_id      BIGINT REFERENCES empresas(id),
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted         BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ,
    CONSTRAINT uq_usuarios_email UNIQUE (email)
);

CREATE INDEX idx_usuarios_empresa ON usuarios_globales(empresa_id) WHERE deleted = FALSE;
CREATE INDEX idx_usuarios_rol ON usuarios_globales(rol) WHERE deleted = FALSE;

CREATE TABLE dispositivos (
    id              BIGSERIAL PRIMARY KEY,
    uuid            VARCHAR(64) NOT NULL,
    nombre          VARCHAR(150) NOT NULL,
    tipo            VARCHAR(30) NOT NULL,
    ultimo_acceso   TIMESTAMPTZ,
    empresa_id      BIGINT NOT NULL REFERENCES empresas(id),
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted         BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ,
    CONSTRAINT uq_dispositivos_uuid UNIQUE (uuid)
);

CREATE INDEX idx_dispositivos_empresa ON dispositivos(empresa_id) WHERE deleted = FALSE;

CREATE TABLE conexiones_empresa (
    id                      BIGSERIAL PRIMARY KEY,
    empresa_id              BIGINT NOT NULL REFERENCES empresas(id),
    host                    VARCHAR(255) NOT NULL,
    puerto                  INTEGER NOT NULL,
    database_name           VARCHAR(100) NOT NULL,
    username                VARCHAR(100) NOT NULL,
    password_encriptada     VARCHAR(512) NOT NULL,
    ssl                     BOOLEAN NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted                 BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at              TIMESTAMPTZ,
    CONSTRAINT uq_conexiones_empresa UNIQUE (empresa_id)
);

CREATE TABLE auditorias (
    id              BIGSERIAL PRIMARY KEY,
    usuario_id      BIGINT REFERENCES usuarios_globales(id),
    fecha           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    accion          VARCHAR(100) NOT NULL,
    ip              VARCHAR(50),
    detalle         VARCHAR(1000),
    recurso         VARCHAR(100),
    recurso_id      BIGINT
);

CREATE INDEX idx_auditorias_fecha ON auditorias(fecha DESC);
CREATE INDEX idx_auditorias_usuario ON auditorias(usuario_id);
CREATE INDEX idx_auditorias_recurso ON auditorias(recurso, recurso_id);

CREATE TABLE refresh_tokens (
    id              BIGSERIAL PRIMARY KEY,
    token_hash      VARCHAR(128) NOT NULL,
    usuario_id      BIGINT NOT NULL REFERENCES usuarios_globales(id),
    expiry_date     TIMESTAMPTZ NOT NULL,
    revoked         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_refresh_token_hash UNIQUE (token_hash)
);

CREATE INDEX idx_refresh_tokens_usuario ON refresh_tokens(usuario_id);
CREATE INDEX idx_refresh_tokens_expiry ON refresh_tokens(expiry_date);
