---Criação das tabelas
DROP TABLE IF EXISTS announcement_model CASCADE;
DROP TABLE IF EXISTS adoption_model CASCADE;
DROP TABLE IF EXISTS vaccine_model CASCADE;
DROP TABLE IF EXISTS pet_model CASCADE;
DROP TABLE IF EXISTS user_model CASCADE;

CREATE TABLE user_model
(
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    password VARCHAR(60) NOT NULL,
    document VARCHAR(14),
    phone VARCHAR(15),
    account_type VARCHAR(20) NOT NULL
);

CREATE TABLE pet_model
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    species VARCHAR(15) NOT NULL,
    breed VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    user_id INT NOT NULL,
    for_adoption BOOLEAN NOT NULL,
    CONSTRAINT fk_pet_user FOREIGN KEY (user_id) REFERENCES user_model(id) ON DELETE CASCADE
);

CREATE TABLE vaccine_model
(
    id SERIAL PRIMARY KEY,
    application_date DATE NOT NULL,
    expiration_date DATE NOT NULL,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(100) NOT NULL,
    batch VARCHAR(15) NOT NULL,
    pet_id INT NOT NULL,
    CONSTRAINT fk_vaccine_pet FOREIGN KEY (pet_id) REFERENCES pet_model(id) ON DELETE CASCADE
);

CREATE TABLE announcement_model
(
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    event_date DATE,
    location VARCHAR(150) NOT NULL,
    announcement_type VARCHAR(15) NOT NULL,
    user_id INT NOT NULL,
    contact VARCHAR(150),

    CONSTRAINT fk_announcement_creator  FOREIGN KEY (user_id) REFERENCES user_model(id) ON DELETE CASCADE
);

CREATE TABLE adoption_model
(
    id SERIAL PRIMARY KEY,
    pet_id INT NOT NULL,
    owner_id INT NOT NULL,
    description TEXT NOT NULL,
    contact VARCHAR(100),
    adopted BOOLEAN NOT NULL DEFAULT FALSE,
    publication_date DATE NOT NULL,

    CONSTRAINT fk_adoption_pet FOREIGN KEY (pet_id) REFERENCES pet_model(id) ON DELETE CASCADE,
    CONSTRAINT fk_adoption_owner FOREIGN KEY (owner_id) REFERENCES user_model(id) ON DELETE CASCADE
);

-- Povoamento das tabelas com dados fictícios para testes e desenvolvimento.
INSERT INTO user_model
    (email, full_name, password, document, phone, account_type)
VALUES
    ('r@r.com', 'Rodrigo', '12345', '98765432100', '31999990001', 'PERSON'),
    ('d@d.com', 'Danilo', '12345', '39053344705', '31999990002', 'PERSON'),
    ('bruno@bruno.com', 'BB enterprise', '123123', '11222333000181', '31999990005', 'ENTERPRISE');

INSERT INTO pet_model
    (name, species, breed, birth_date, user_id, for_adoption)
VALUES
    ('Tiririca', 'BIRD', 'Cacatua', '2020-05-15', 1, true),
    ('Thor', 'DOG', 'Shih-Tzu', '2021-08-20', 1, false),
    ('Mia', 'CAT', 'Siamês', '2019-03-10', 2, false),
    ('Zeca', 'OTHER', 'Mangalarga Marchador', '2021-02-18', 3, false);

INSERT INTO vaccine_model
    (application_date, expiration_date, name, description, batch, pet_id)
VALUES
    ('2026-01-10', '2027-01-10', 'V10',
     'Vacina polivalente canina', 'V10-2026-01', 1),

    ('2026-02-15', '2027-02-15', 'Antirrábica',
     'Vacina contra raiva', 'RAB-2026-02', 1),

    ('2026-04-05', '2027-04-05', 'Antirrábica',
     'Vacina contra raiva', 'RAB-2026-04', 2),

    ('2026-01-25', '2027-01-25', 'V4',
     'Vacina quádrupla felina', 'V4-2026-01', 3),

    ('2026-02-28', '2027-02-28', 'Antirrábica',
     'Vacina contra raiva', 'RAB-2026-02', 4);


INSERT INTO announcement_model
    (title, description, event_date, location, announcement_type, user_id, contact)
VALUES
         ('Feira de Adoção de Cães','Feira para adoção responsável de cães resgatados.','2026-09-20',
            'Praça Central','ADOPTION',3,null),
         ('Campanha de Vacinação','Campanha de vacinação gratuita para cães e gatos.','2026-09-25',
            'Centro Comunitário','VACCINE',3, null),
         ('Cachorro Perdido','Cachorro de porte médio desapareceu nas proximidades do bairro.',
            '2026-09-05','Bairro Centro', 'LOST',3,null);

INSERT INTO adoption_model
    (pet_id, owner_id, description, contact, adopted, publication_date)
VALUES
    (1, 1, 'Tiririca é um passarinho bem calmo, procurando um novo lar com bastante carinho.', '(35) 99999-0000', false, '2026-02-01');