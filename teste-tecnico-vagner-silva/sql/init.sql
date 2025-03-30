SET GLOBAL sql_mode = '';

CREATE TABLE IF NOT EXISTS conta (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    status_conta ENUM('ATIVA', 'INATIVA') NOT NULL DEFAULT 'ATIVA',
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cartao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    numero VARCHAR(16) NOT NULL UNIQUE,
    titular VARCHAR(100) NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    data_validade CHAR(5) NOT NULL,
    tipo ENUM('FISICO', 'VIRTUAL') NOT NULL DEFAULT 'FISICO',
    status ENUM('ATIVO', 'BLOQUEADO', 'INATIVO') NOT NULL DEFAULT 'INATIVO',
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    conta_id BIGINT NOT NULL,
    tracking_id VARCHAR(50),
    delivery_status ENUM('PROCESSANDO', 'ENVIADO', 'EM_TRANSITO', 'ENTREGUE', 'FALHA', 'DEVOLVIDO') NOT NULL DEFAULT 'PROCESSANDO',
    delivery_date DATETIME,
    delivery_return_reason TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (conta_id) REFERENCES conta(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS endereco (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,  
    conta_id BIGINT,
    cartao_id BIGINT,
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    cep VARCHAR(8) NOT NULL,
    CONSTRAINT fk_endereco_conta
        FOREIGN KEY (conta_id) 
        REFERENCES conta(id),
    CONSTRAINT fk_endereco_cartao
        FOREIGN KEY (cartao_id) 
        REFERENCES cartao(id)
);

CREATE TABLE reemissao_cartao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cartao_id BIGINT NOT NULL,
    motivo ENUM('PERDA', 'ROUBO') NOT NULL,
    data_solicitacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (cartao_id) REFERENCES cartao(id) ON DELETE CASCADE
);

-- Criação das contas
INSERT IGNORE INTO conta (nome, cpf, telefone, email) VALUES
('Maria Silva Oliveira', '52998231075', '(11) 987654321', 'maria.silva@email.com'),
('Pedro Almeida Costa', '15370984002', '(21) 998765432', 'pedro.costa@email.com'),
('Ana Paula Santos', '86423517049', '(31) 987651234', 'ana.santos@email.com');

-- Inserção dos endereços
INSERT IGNORE INTO endereco (conta_id, logradouro, numero, cidade, estado, cep) VALUES
(
    (SELECT id FROM conta WHERE cpf = '52998231075'), 
    'Avenida Paulista', 
    '1001', 
    'São Paulo', 
    'SP', 
    '01311-100'
),
(
    (SELECT id FROM conta WHERE cpf = '15370984002'), 
    'Praia de Botafogo', 
    '300', 
    'Rio de Janeiro', 
    'RJ', 
    '22250-040'
),
(
    (SELECT id FROM conta WHERE cpf = '86423517049'), 
    'Avenida Afonso Pena', 
    '4000', 
    'Belo Horizonte', 
    'MG', 
    '30130-009'
);

-- Inserção dos cartões
INSERT IGNORE INTO cartao (
    numero, 
    titular, 
    cvv, 
    data_validade, 
    tipo, 
    status, 
    conta_id
) VALUES
(
    '4539128473625518', 
    'MARIA S OLIVEIRA', 
    '284', 
    '12/27', 
    'FISICO', 
    'INATIVO',
    (SELECT id FROM conta WHERE cpf = '52998231075')
),
(
    '5556342199871234', 
    'PEDRO A COSTA', 
    '761', 
    '08/26', 
    'FISICO', 
    'INATIVO',
    (SELECT id FROM conta WHERE cpf = '15370984002')
),
(
    '4024007159328465', 
    'ANA P SANTOS', 
    '035', 
    '05/28', 
    'FISICO', 
    'INATIVO',
    (SELECT id FROM conta WHERE cpf = '86423517049')
);