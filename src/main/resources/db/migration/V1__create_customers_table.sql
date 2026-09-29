CREATE TABLE customers (
    id               BIGSERIAL    PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    apellido         VARCHAR(100) NOT NULL,
    email            VARCHAR(150) NOT NULL,
    dni              VARCHAR(20)  NOT NULL,
    fecha_creacion   TIMESTAMP    NOT NULL DEFAULT now(),
    fecha_nacimiento DATE         NOT NULL,
    CONSTRAINT uk_customers_email UNIQUE (email),
    CONSTRAINT uk_customers_dni   UNIQUE (dni)
);

CREATE INDEX idx_customers_dni   ON customers (dni);
CREATE INDEX idx_customers_email ON customers (email);
