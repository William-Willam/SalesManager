CREATE TABLE usuario (
                         id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome        VARCHAR(120) NOT NULL,
                         email       VARCHAR(150) NOT NULL UNIQUE,
                         senha       VARCHAR(255) NOT NULL,
                         papel       ENUM('ADMIN', 'GERENTE', 'ATENDENTE') NOT NULL,
                         criado_por  BIGINT,
                         ativo       BIT NOT NULL,
                         criado_em   DATETIME(6) NOT NULL,
                         CONSTRAINT fk_usuario_criado_por FOREIGN KEY (criado_por) REFERENCES usuario(id)
);

CREATE TABLE categoria (
                           id      BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nome    VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE produto (
                         id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome            VARCHAR(150) NOT NULL,
                         descricao       VARCHAR(255),
                         preco           DECIMAL(10,2) NOT NULL,
                         categoria_id    BIGINT NOT NULL,
                         ativo           BIT NOT NULL,
                         CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE venda (
                       id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                       usuario_id        BIGINT NOT NULL,
                       data              DATETIME(6) NOT NULL,
                       total             DECIMAL(10,2) NOT NULL,
                       forma_pagamento   ENUM('DINHEIRO', 'CARTAO', 'PIX', 'VALE_REFEICAO') NOT NULL,
                       status            ENUM('FINALIZADA', 'CANCELADA') NOT NULL,
                       CONSTRAINT fk_venda_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE item_venda (
                            id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                            venda_id        BIGINT NOT NULL,
                            produto_id      BIGINT NOT NULL,
                            quantidade      INT NOT NULL,
                            preco_unitario  DECIMAL(10,2) NOT NULL,
                            subtotal        DECIMAL(10,2) NOT NULL,
                            CONSTRAINT fk_item_venda_venda FOREIGN KEY (venda_id) REFERENCES venda(id) ON DELETE CASCADE,
                            CONSTRAINT fk_item_venda_produto FOREIGN KEY (produto_id) REFERENCES produto(id)
);