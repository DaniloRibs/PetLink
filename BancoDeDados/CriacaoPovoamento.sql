--Criacão das tabelas
CREATE TABLE Usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE Pessoa (
    usuario_id INT PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    CONSTRAINT fk_pessoa_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

CREATE TABLE Instituicao (
    usuario_id INT PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    CONSTRAINT fk_instituicao_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE
);

CREATE TABLE Pet (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    raca VARCHAR(50) NOT NULL,
    data_nascimento DATE NOT NULL,
    usuario_id INT NOT NULL,
    CONSTRAINT fk_pet_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    CONSTRAINT chk_pet_tipo CHECK (tipo IN ('Cachorro', 'Gato', 'Hamster', 'Pássaro', 'Outro'))
);

CREATE TABLE Vacina (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE Doacao (
    id SERIAL PRIMARY KEY,
    data_doacao DATE NOT NULL,
    usuario_id INT NOT NULL, 
    pet_id INT NOT NULL UNIQUE, 
    CONSTRAINT fk_doacao_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    CONSTRAINT fk_doacao_pet FOREIGN KEY (pet_id) REFERENCES Pet(id)
);

CREATE TABLE Adocao (
    id SERIAL PRIMARY KEY,
    data_adocao DATE NOT NULL,
    usuario_id INT NOT NULL,
    pet_id INT NOT NULL UNIQUE, 
    CONSTRAINT fk_adocao_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    CONSTRAINT fk_adocao_pet FOREIGN KEY (pet_id) REFERENCES Pet(id)
);

CREATE TABLE Anuncia_Vacina (
    instituicao_id INT NOT NULL,
    vacina_id INT NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    data_anuncio DATE NOT NULL,
    PRIMARY KEY (instituicao_id, vacina_id, data_anuncio),
    CONSTRAINT fk_anuncia_instituicao FOREIGN KEY (instituicao_id) REFERENCES Instituicao(usuario_id),
    CONSTRAINT fk_anuncia_vacina FOREIGN KEY (vacina_id) REFERENCES Vacina(id)
);

CREATE TABLE Aplicacao_Vacina (
    pet_id INT NOT NULL,
    vacina_id INT NOT NULL,
    data_aplicacao DATE NOT NULL,
    PRIMARY KEY (pet_id, vacina_id, data_aplicacao),
    CONSTRAINT fk_aplicacao_pet FOREIGN KEY (pet_id) REFERENCES Pet(id),
    CONSTRAINT fk_aplicacao_vacina FOREIGN KEY (vacina_id) REFERENCES Vacina(id)
);

-- Povoamento das tabelas com dados fictícios para testes e desenvolvimento.

INSERT INTO Usuario (nome, email, senha) VALUES
('Carlos Oliveira', 'carlos.oliveira@email.com', 'senha1234'),
('Ana Beatriz Souza', 'ana.souza@email.com', 'senha2345'),
('Associação Protetora dos Animais (APA)', 'contato@apa.org.br', 'senha3456'),
('Clínica Veterinária Saúde Animal', 'atendimento@saudeanimal.com.br', 'senha4567');


INSERT INTO Pessoa (usuario_id, cpf) VALUES
(1, '12345678901'),
(2, '98765432109');

INSERT INTO Instituicao (usuario_id, cnpj) VALUES
(3, '12345678000199'),
(4, '98765432000188');


INSERT INTO Pet (nome, tipo, raca, data_nascimento, usuario_id) VALUES
('Rex', 'Cachorro', 'Vira-lata', '2023-05-10', 3),
('Mia', 'Gato', 'Siamês', '2024-02-15', 3),
('Thor', 'Cachorro', 'Labrador', '2021-11-20', 3),
('Luna', 'Gato', 'Persa', '2022-08-05', 4),
('Apolo', 'Cachorro', 'Vira-lata', '2016-01-13', 4);


INSERT INTO Vacina (nome) VALUES
('Antirrábica'),
('V10 (Quádrupla Canina)'),
('V4 (Quádrupla Felina)'),
('Gripe Canina');

INSERT INTO Doacao (data_doacao, usuario_id, pet_id) VALUES
('2024-01-15', 1, 1),
('2024-03-01', 2, 2);


INSERT INTO Adocao (data_adocao, usuario_id, pet_id) VALUES
('2024-05-20', 2, 3);


INSERT INTO Anuncia_Vacina (instituicao_id, vacina_id, localizacao, data_anuncio) VALUES
(4, 1, 'Campanha na Praça Central, Santa Rita do Sapucaí - MG', '2026-06-15'),
(4, 2, 'Clínica Sede - Rua Cel. Rennó, 120', '2026-06-16'),
(3, 1, 'Sede da ONG APA - Av. das Nações, 450', '2026-07-01');


INSERT INTO Aplicacao_Vacina (pet_id, vacina_id, data_aplicacao) VALUES
(1, 1, '2024-05-12'),
(1, 2, '2024-06-12'), 
(2, 3, '2024-04-20'), 
(3, 1, '2023-11-25'); 