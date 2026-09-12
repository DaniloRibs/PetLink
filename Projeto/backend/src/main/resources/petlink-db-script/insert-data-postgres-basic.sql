INSERT INTO user_model
    (email, full_name, password, document, phone, account_type)
VALUES
    ('joao.silva@email.com', 'João Silva', '123456', '98765432100', '31999990001', 'PERSON'),
    ('maria.souza@email.com', 'Maria Souza', '123456', '39053344705', '31999990002', 'PERSON'),
    ('carlos.oliveira@email.com', 'Carlos Oliveira', '123456', '11144477735', '31999990003', 'PERSON'),
    ('ana.costa@email.com', 'Ana Costa', '123456', '52998224725', '31999990004', 'PERSON'),
    ('bolaoGmae@petcare.com', 'bolaoEntherpriuse', 'admin123', '11222333000181', '31999990005', 'ENTERPRISE');

INSERT INTO pet_model
    (name, specie, breed, birth_date, user_id)
VALUES
    ('Rex', 'DOG', 'Golden Retriever', '2020-05-15', 1),
    ('Mel', 'DOG', 'Shih-Tzu', '2021-08-20', 1),
    ('Mia', 'CAT', 'Siamês', '2019-03-10', 2),
    ('Thor', 'DOG', 'Pastor Alemão', '2018-11-05', 3),
    ('Luna', 'CAT', 'Persa', '2022-01-25', 3),
    ('Nina', 'DOG', 'Poodle', '2020-07-12', 4),
    ('Bob', 'DOG', 'Beagle', '2021-02-18', 4);

INSERT INTO vaccine_model
    (application_date, expiration_date, name, description, batch, pet_id)
VALUES
    ('2026-01-10', '2027-01-10', 'V10',
     'Vacina polivalente canina', 'V10-2026-01', 1),

    ('2026-02-15', '2027-02-15', 'Antirrábica',
     'Vacina contra raiva', 'RAB-2026-02', 1),

    ('2026-03-20', '2027-03-20', 'V10',
     'Vacina polivalente canina', 'V10-2026-03', 2),

    ('2026-04-05', '2027-04-05', 'Antirrábica',
     'Vacina contra raiva', 'RAB-2026-04', 2),

    ('2026-01-25', '2027-01-25', 'V4',
     'Vacina quádrupla felina', 'V4-2026-01', 3),

    ('2026-02-28', '2027-02-28', 'Antirrábica',
     'Vacina contra raiva', 'RAB-2026-02', 3),

    ('2026-05-10', '2027-05-10', 'V10',
     'Vacina polivalente canina', 'V10-2026-05', 4),

    ('2026-05-10', '2027-05-10', 'Antirrábica',
     'Vacina contra raiva', 'RAB-2026-05', 4),

    ('2026-06-15', '2027-06-15', 'V4',
     'Vacina quádrupla felina', 'V4-2026-06', 5),

    ('2026-07-01', '2027-07-01', 'V10',
     'Vacina polivalente canina', 'V10-2026-07', 6),

    ('2026-07-15', '2027-07-15', 'Antirrábica',
     'Vacina contra raiva', 'RAB-2026-07', 6),

    ('2026-08-01', '2027-08-01', 'V10',
     'Vacina polivalente canina', 'V10-2026-08', 7);
