INSERT INTO user_model
    (email, full_name, password, document, phone, account_type)
VALUES
    ('a@a.com', 'João Silva', '12345', '98765432100', '31999990001', 'PERSON'),
    ('maria.souza@email.com', 'Maria Souza', '123456', '39053344705', '31999990002', 'PERSON'),
    ('carlos.oliveira@email.com', 'Carlos Oliveira', '123456', '11144477735', '31999990003', 'PERSON'),
    ('ana.costa@email.com', 'Ana Costa', '123456', '52998224725', '31999990004', 'PERSON'),
    ('bolaoGames@petcare.com', 'bolaoEntherpriuse', '123123', '11222333000181', '31999990005', 'ENTERPRISE');

INSERT INTO pet_model
    (name, species, breed, birth_date, user_id, for_adoption)
VALUES
    ('Rex', 'DOG', 'Golden Retriever', '2020-05-15', 1, false),
    ('Mel', 'DOG', 'Shih-Tzu', '2021-08-20', 1, false),
    ('Mia', 'CAT', 'Siamês', '2019-03-10', 2, false),
    ('Thor', 'DOG', 'Pastor Alemão', '2018-11-05', 3, false),
    ('Luna', 'CAT', 'Persa', '2022-01-25', 3, true),
    ('Nina', 'DOG', 'Poodle', '2020-07-12', 4, false),
    ('bolinho', 'DOG', 'fofinho', '2021-02-18', 5, false);

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
     'Vacina contra raiva', 'RAB-2026-02', 5);


INSERT INTO annoucement_model
    (title, description, publication_date, event_date, location, announcement_type, user_id)
VALUES
         (
             'Feira de Adoção de Cães',
             'Feira para adoção responsável de cães resgatados.',
             '2026-09-01',
             '2026-09-20',
             'Praça Central',
             'ADOPTION',
             5
         ),
         (
             'Campanha de Vacinação',
             'Campanha de vacinação gratuita para cães e gatos.',
             '2026-09-03',
             '2026-09-25',
             'Centro Comunitário',
             'VACCINE',
             5
         ),
         (
             'Cachorro Perdido',
             'Cachorro de porte médio desapareceu nas proximidades do bairro.',
             '2026-09-05',
             '2026-09-05',
             'Bairro Centro',
             'VACCINE',
             5
         );

INSERT INTO adoption_model
    (pet_id, owner_id, description, contact, adopted, publication_date)
VALUES
    (3, 4, 'Tiririca é um passarinho bem calmo, procurando um novo lar com bastante carinho.', '(35) 99999-0000', false, '2026-02-01'),
    (4, 2, 'Thor está disponível para adoção, muito dócil e já castrado.', 'thor.adocao@example.com', true, '2026-01-15'),
    (2, 2, 'Lucy é uma gata tranquila, ideal para apartamento. Já vacinada.', '(35) 98888-1234', false, '2026-03-05');