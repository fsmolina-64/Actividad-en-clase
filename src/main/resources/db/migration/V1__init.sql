-- Migración inicial: crea tabla de ejemplo
CREATE TABLE IF NOT EXISTS ejemplo (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice de ejemplo
CREATE INDEX IF NOT EXISTS idx_ejemplo_nombre ON ejemplo(nombre);