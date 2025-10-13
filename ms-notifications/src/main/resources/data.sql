INSERT INTO prioridad (nombre, descripcion) VALUES 
('BAJA', 'Prioridad baja'),
('MEDIA', 'Prioridad media'),
('ALTA', 'Prioridad alta')
ON CONFLICT DO NOTHING;  -- evita duplicados si se reinicia la app
