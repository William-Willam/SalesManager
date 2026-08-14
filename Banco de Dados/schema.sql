-- Sales Manager - Modelo do banco de dados
-- MySQL 8+

CREATE DATABASE IF NOT EXISTS sales_manager
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE sales_manager;

-- ==============================
-- USUARIO (administrador, gerente ou atendente)
-- ==============================
CREATE TABLE usuario (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(120) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    senha       VARCHAR(255) NOT NULL,
    papel       ENUM('ADMIN', 'GERENTE', 'ATENDENTE') NOT NULL,
    criado_por  INT,                      -- quem cadastrou este usuário (ADMIN cadastra GERENTE/ATENDENTE)
    ativo       BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_criado_por
        FOREIGN KEY (criado_por) REFERENCES usuario(id)
        ON DELETE SET NULL
);

-- ==============================
-- CATEGORIA (ex: lanches, bebidas, sobremesas)
-- ==============================
CREATE TABLE categoria (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nome    VARCHAR(100) NOT NULL UNIQUE
);

-- ==============================
-- PRODUTO
-- ==============================
CREATE TABLE produto (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(150) NOT NULL,
    descricao       VARCHAR(255),
    preco           DECIMAL(10,2) NOT NULL,
    categoria_id    INT NOT NULL,
    ativo           BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_produto_categoria
        FOREIGN KEY (categoria_id) REFERENCES categoria(id)
        ON DELETE RESTRICT
);

-- ==============================
-- VENDA
-- ==============================
CREATE TABLE venda (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id        INT NOT NULL,      -- atendente que registrou a venda
    data              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total             DECIMAL(10,2) NOT NULL DEFAULT 0,
    forma_pagamento   ENUM('DINHEIRO', 'CARTAO', 'PIX', 'VALE_REFEICAO') NOT NULL,
    status            ENUM('FINALIZADA', 'CANCELADA') NOT NULL DEFAULT 'FINALIZADA',
    CONSTRAINT fk_venda_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE RESTRICT
);

-- ==============================
-- ITEM_VENDA
-- ==============================
CREATE TABLE item_venda (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    venda_id        INT NOT NULL,
    produto_id      INT NOT NULL,
    quantidade      INT NOT NULL,
    preco_unitario  DECIMAL(10,2) NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_item_venda_venda
        FOREIGN KEY (venda_id) REFERENCES venda(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_item_venda_produto
        FOREIGN KEY (produto_id) REFERENCES produto(id)
        ON DELETE RESTRICT
);

-- ==============================
-- Dados iniciais (para testes)
-- ==============================
INSERT INTO usuario (nome, email, senha, papel, criado_por) VALUES
('Administrador Padrão', 'admin@lanchonete.com', 'CHANGE_ME_HASH', 'ADMIN', NULL);

INSERT INTO usuario (nome, email, senha, papel, criado_por) VALUES
('Gerente Padrão', 'gerente@lanchonete.com', 'CHANGE_ME_HASH', 'GERENTE', 1),
('Atendente Padrão', 'atendente@lanchonete.com', 'CHANGE_ME_HASH', 'ATENDENTE', 1);

INSERT INTO categoria (nome) VALUES
('Lanches'), ('Bebidas'), ('Sobremesas');
