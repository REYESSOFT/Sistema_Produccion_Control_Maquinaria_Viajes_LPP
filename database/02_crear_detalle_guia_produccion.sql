USE lpp_smart_erp;

CREATE TABLE IF NOT EXISTS guia_produccion_detalle (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_guia INT NOT NULL,
    numero_fila INT NOT NULL,
    proyecto VARCHAR(150),
    sector VARCHAR(150),
    cantera VARCHAR(150),
    material VARCHAR(150),
    hora_origen TIME,
    hora_destino TIME,

    CONSTRAINT fk_detalle_guia
        FOREIGN KEY (id_guia)
        REFERENCES guias(id_guia)
        ON DELETE CASCADE
);