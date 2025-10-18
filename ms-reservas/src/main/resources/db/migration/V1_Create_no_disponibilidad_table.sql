-- Crear tabla no_disponibilidad
CREATE TABLE IF NOT EXISTS no_disponibilidad (
    id_no_disponiblilidad BIGSERIAL PRIMARY KEY,
    id_oferta int NOT NULL,
    motivo varchar(100) NOT NULL,
    fecha_inicio timestamp NOT NULL,
    fecha_fin timestamp NOT NULL,
    id_reserva int
);

-- Insertar datos de ejemplo
INSERT INTO no_disponibilidad (id_oferta, motivo, fecha_inicio, fecha_fin, id_reserva)
VALUES 
    (101, 'Mantenimiento', '2025-10-20 09:00:00', '2025-10-20 12:00:00', null),
    (102, 'Vacaciones', '2025-10-21 14:00:00', '2025-10-21 18:00:00', 1)
ON CONFLICT (id_no_disponiblilidad) DO NOTHING;
