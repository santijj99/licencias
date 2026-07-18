-- V2__datos_semilla.sql
-- Planes base y super administrador inicial (password: Admin123!)

INSERT INTO planes (nombre, precio, cantidad_usuarios, cantidad_sucursales, cantidad_dispositivos, descripcion, activo)
VALUES
    ('Basico', 29.99, 5, 1, 3, 'Plan inicial para comercios pequeños', TRUE),
    ('Profesional', 79.99, 25, 5, 15, 'Plan para cadenas medianas', TRUE),
    ('Enterprise', 199.99, 200, 50, 100, 'Plan ilimitado para grandes operaciones', TRUE);

-- BCrypt hash de Admin123! (strength 10)
INSERT INTO usuarios_globales (nombre, email, password, rol, empresa_id, activo)
VALUES (
    'Super Administrador',
    'admin@licencias.local',
    '$2b$10$MKKqX8Mdp4JZzt1S0k0t8uxCsdkRFvFRdWaGfdCB0qOsbksoWRx7a',
    'SUPER_ADMIN',
    NULL,
    TRUE
);
