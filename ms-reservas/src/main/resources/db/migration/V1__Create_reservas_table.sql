-- Crear tabla reservas
CREATE TABLE IF NOT EXISTS reservas (
    id_reserva SERIAL,
    id_solicitud int NOT NULL,
    fecha_reserva_inicio timestamp NOT NULL,
    fecha_reserva_fin timestamp NOT NULL,
    estado varchar(100) NOT NULL,
    fecha_creacion timestamp NOT NULL,
    fecha_actualizacion timestamp NOT NULL,
    CONSTRAINT reservas_pk PRIMARY KEY (id_reserva)
);

-- Crear índices para mejorar performance
CREATE INDEX IF NOT EXISTS idx_reservas_estado ON reservas(estado);
CREATE INDEX IF NOT EXISTS idx_reservas_solicitud ON reservas(id_solicitud);
CREATE INDEX IF NOT EXISTS idx_reservas_fecha_inicio ON reservas(fecha_reserva_inicio);
CREATE INDEX IF NOT EXISTS idx_reservas_fecha_fin ON reservas(fecha_reserva_fin);

-- Insertar datos de ejemplo
INSERT INTO reservas (id_solicitud, fecha_reserva_inicio, fecha_reserva_fin, estado, fecha_creacion, fecha_actualizacion) 
VALUES 
    (1, '2024-01-15 10:00:00', '2024-01-15 12:00:00', 'CONFIRMADA', NOW(), NOW()),
    (2, '2024-01-16 14:00:00', '2024-01-16 16:00:00', 'PENDIENTE', NOW(), NOW()),
    (3, '2024-01-17 09:00:00', '2024-01-17 11:00:00', 'CANCELADA', NOW(), NOW())
ON CONFLICT (id_reserva) DO NOTHING;