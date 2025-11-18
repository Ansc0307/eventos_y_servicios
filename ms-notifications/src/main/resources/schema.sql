-- TipoNotificacion
CREATE TABLE IF NOT EXISTS tipo_notificacion (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT
);

-- Prioridad
CREATE TABLE IF NOT EXISTS prioridad (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT
);

-- Notificacion
CREATE TABLE IF NOT EXISTS notificacion (
    id BIGSERIAL PRIMARY KEY,
    asunto VARCHAR(255) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT now(),
    leido BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    id_prioridad BIGINT,
    id_tipo BIGINT,
    CONSTRAINT fk_prioridad FOREIGN KEY (id_prioridad) REFERENCES prioridad(id),
    CONSTRAINT fk_tipo_notificacion FOREIGN KEY (id_tipo) REFERENCES tipo_notificacion(id)
);
