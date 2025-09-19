-- @author intraron
-- Script de inicialización de la base de datos.
-- Contiene las tablas necesarias para el proyecto de gestión de préstamos.

CREATE TABLE IF NOT EXISTS userdata (
    id VARCHAR(255) PRIMARY KEY,
    nombres VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    correo_electronico VARCHAR(255) NOT NULL,
    salario_base DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS loan (
    id UUID PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    loan_amount DOUBLE PRECISION NOT NULL,
    loan_term INTEGER NOT NULL,
    loan_type VARCHAR(255) NOT NULL,
    interest_rate DOUBLE PRECISION NOT NULL,
    request_status VARCHAR(255) NOT NULL,
    base_salary DOUBLE PRECISION NOT NULL,
    total_monthly_debt DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS solicitudes (
    id UUID PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    loan_amount DOUBLE PRECISION NOT NULL,
    loan_term INTEGER NOT NULL,
    loan_type VARCHAR(255),
    interest_rate DOUBLE PRECISION,
    request_status VARCHAR(50) NOT NULL,
    base_salary DOUBLE PRECISION,
    total_monthly_debt DOUBLE PRECISION
);