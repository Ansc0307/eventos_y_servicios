-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2025-09-19 23:45:37.737

-- tables
-- Table: no_disponibilidad
CREATE TABLE no_disponibilidad (
    id_no_disponiblilidad serial  ,
    id_oferta int  ,
    motivo varchar(100)  ,
    fecha_inicio timestamp  ,
    fecha_fin timestamp ,
    id_reserva int,
    CONSTRAINT no_disponibilidad_pk PRIMARY KEY (id_no_disponiblilidad)
);

-- Table: reservas
CREATE TABLE reservas (
    id_reserva serial  ,
    id_solicitud int  ,
    fecha_reserva_inicio timestamp  ,
    fecha_reserva_fin timestamp  ,
    estado varchar(100)  ,
    fecha_creacion timestamp  ,
    fecha_actualizacion timestamp  ,
    CONSTRAINT reservas_pk PRIMARY KEY (id_reserva)
);

-- Table: solicitudes
CREATE TABLE solicitudes (
    id_solicitud serial  ,
    fecha_solicitud timestamp  ,
    estado_solicitud varchar(100)  ,
    id_organizador int  ,
    id_proovedor int  ,
    id_oferta int  ,
    id_proovedor_nombre varchar(100)  ,
    id_organizador_nombre varchar(100)  ,
    CONSTRAINT solicitudes_pk PRIMARY KEY (id_solicitud)
);



