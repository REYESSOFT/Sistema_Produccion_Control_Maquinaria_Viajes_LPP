CREATE DATABASE IF NOT EXISTS lpp_smart_erp;
USE lpp_smart_erp;

CREATE TABLE IF NOT EXISTS empresas (
    id_empresa INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empresa VARCHAR(100) NOT NULL,
    ruc VARCHAR(20),
    estado VARCHAR(20) DEFAULT 'ACTIVA'
);

INSERT INTO empresas (nombre_empresa, ruc, estado) VALUES
('EQUIPOS PRO', NULL, 'ACTIVA'),
('DEVIALTRANSPORT', NULL, 'ACTIVA');

CREATE TABLE IF NOT EXISTS guias (
    id_guia INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT NOT NULL,
    tipo_guia VARCHAR(100) NOT NULL,
    numero_guia VARCHAR(50) NOT NULL,
    fecha DATE NOT NULL,

    cliente VARCHAR(150),
    proyecto VARCHAR(150),

    placa VARCHAR(50),
    equipo VARCHAR(150),
    chofer_operador VARCHAR(150),

    m3 DECIMAL(10,2) DEFAULT 0,
    horas DECIMAL(10,2) DEFAULT 0,

    material VARCHAR(150),
    cantera VARCHAR(150),
    sector VARCHAR(150),
    origen VARCHAR(150),
    destino VARCHAR(150),

    hora_inicio TIME,
    hora_fin TIME,

    horometro_inicial DECIMAL(10,2),
    horometro_final DECIMAL(10,2),

    combustible VARCHAR(100),
    mantenimiento VARCHAR(150),
    danos VARCHAR(150),

    recibi_conforme VARCHAR(150),
    observaciones TEXT,

    estado VARCHAR(30) DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_empresa) REFERENCES empresas(id_empresa),

    UNIQUE KEY uk_guia_empresa_tipo_numero (
        id_empresa,
        tipo_guia,
        numero_guia
    )
);