-- ===============================
-- Tabela de roles
-- ===============================
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- ===============================
-- Tabela de patios
-- ===============================
CREATE TABLE patio (
    id_patio BIGINT PRIMARY KEY,
    nm_patio VARCHAR(200) NOT NULL,
    capacidade INT NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- ===============================
-- Tabela de administradores
-- ===============================
CREATE TABLE administrador (
    cpf VARCHAR(11) PRIMARY KEY,
    nm_adm VARCHAR(200) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    id_patio BIGINT NOT NULL,
    FOREIGN KEY (id_patio) REFERENCES patio(id_patio)
);

-- ===============================
-- Tabela de motos
-- ===============================
CREATE TABLE moto (
    placa VARCHAR(10) PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    km_atual DOUBLE PRECISION NOT NULL,
    data_ultimo_check DATE NOT NULL,
    id_patio BIGINT,
    FOREIGN KEY (id_patio) REFERENCES patio(id_patio) ON DELETE SET NULL
);

-- ===============================
-- Tabela de monitoracao
-- ===============================
CREATE TABLE monitoracao (
    id_monitoracao SERIAL PRIMARY KEY,
    tipo_evento VARCHAR(50) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    placa_moto VARCHAR(10) NOT NULL,
    FOREIGN KEY (placa_moto) REFERENCES moto(placa)
);
