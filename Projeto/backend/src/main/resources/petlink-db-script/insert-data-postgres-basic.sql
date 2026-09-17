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
    (title, description, event_date, location, announcement_type, user_id)
VALUES
         (
             'Feira de Adoção de Cães',
             'Feira para adoção responsável de cães resgatados.',
             '2026-09-20',
             'Praça Central',
             'ADOPTION',
             3
         ),
         (
             'Campanha de Vacinação',
             'Campanha de vacinação gratuita para cães e gatos.',
             '2026-09-25',
             'Centro Comunitário',
             'VACCINE',
             3
         ),
         (
             'Cachorro Perdido',
             'Cachorro de porte médio desapareceu nas proximidades do bairro.',
             '2026-09-05',
             'Bairro Centro',
             'LOST',
             3
         );

INSERT INTO adoption_model
    (pet_id, owner_id, description, contact, adopted, publication_date)
VALUES
    (1, 1, 'Tiririca é um passarinho bem calmo, procurando um novo lar com bastante carinho.', '(35) 99999-0000', false, '2026-02-01');