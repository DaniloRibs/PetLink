INSERT INTO user_model (email, full_name, password, document, phone, account_type) VALUES
('r@r.com', 'Rodrigo', '12345', NULL, '31999990001', 'PERSON'),
('d@d.com', 'Danilo', '12345', '39053344705', '31999990002', 'PERSON'),
('bruno@bruno.com', 'BB enterprise', '123123', '11222333000181', '31999990005', 'ENTERPRISE'),
('ana@ana.com', 'Ana Clara', '12345', NULL, '11988880001', 'PERSON'),
('bovista@fazenda.com', 'Fazenda Boa Vista', '12345', '76634789000109', '19977770002', 'ENTERPRISE'),
('caofeliz@shop.com', 'PetShop Cão Feliz', '12345', '10682248000104', '11966660003', 'ENTERPRISE'),
('carlos@c.com', 'Carlos Silva', '12345', NULL, '31955550004', 'PERSON'),
('agro@vale.com', 'Agropecuária Vale', '12345', '08745914000180', '19944440005', 'ENTERPRISE'),
('mari@m.com', 'Mariana Costa', '12345', NULL, '11933330006', 'PERSON'),
('joao@j.com', 'Joao Souza', '12345', NULL, '11922220007', 'PERSON');

INSERT INTO animal_model (name, species, breed, birth_date, user_id, gender) VALUES
('Rex', 'DOG', 'Labrador', '2020-05-10', 2, 'M'),
('Mimosa', 'COW', 'Holandesa', '2019-03-15', 5, 'F'),
('Trovão', 'HORSE', 'Mangalarga', '2018-11-20', 5, 'M'),
('Mia', 'CAT', 'Siamês', '2021-08-05', 4, 'F'),
('Pé de Pano', 'HORSE', 'Crioulo', '2017-02-10', 8, 'M'),
('Bolinha', 'DOG', 'Poodle', '2022-12-01', 6, 'F'),
('Pipoca', 'BIRD', 'Calopsita', '2023-01-15', 7, 'M'),
('Peppa', 'PIG', 'Landrace', '2023-05-20', 5, 'F'),
('Thor', 'DOG', 'Pastor Alemão', '2021-06-30', 6, 'M'),
('Malhada', 'COW', 'Nelore', '2020-04-12', 8, 'F'),
('Branquinha', 'SHEEP', 'Dorper', '2022-09-08', 8, 'F'),
('Luna', 'CAT', 'Persa', '2019-10-25', 9, 'F'),
('Caramelo', 'DOG', 'SRD', '2020-01-01', 1, 'M'),
('Galo Doido', 'BIRD', 'Caipira', '2023-03-03', 3, 'M'),
('Fumaça', 'CAT', 'SRD', '2023-11-10', 10, 'M');

INSERT INTO farm_animal_model (identifier, for_sell, weight, animal_id) VALUES
('V-001', false, 450.5, 2),
('H-001', true, 520.0, 3),
('H-002', false, 600.0, 5),
('P-001', true, 120.0, 8),
('V-002', true, 480.0, 10),
('S-001', true, 65.0, 11),
('G-001', true, 3.5, 14);

INSERT INTO pet_model (for_adoption, animal_id) VALUES
(false, 1),
(true, 4),
(true, 6),
(false, 7),
(true, 9),
(false, 12),
(false, 13),
(true, 15);

INSERT INTO vaccine_model (application_date, expiration_date, name, description, batch, pet_id) VALUES
('2023-01-10', '2024-01-10', 'Antirrábica', 'Vacina anual contra a raiva', 'RAB-001', 1),
('2023-05-15', '2024-05-15', 'V10', 'Múltipla canina', 'V10-998', 1),
('2023-02-20', '2024-02-20', 'V4', 'Múltipla felina', 'V4-112', 2),
('2023-10-01', '2024-10-01', 'Antirrábica', 'Vacina anual contra a raiva', 'RAB-002', 3),
('2023-11-15', '2024-11-15', 'V10', 'Múltipla canina', 'V10-999', 5),
('2023-06-12', '2024-06-12', 'Antirrábica', 'Vacina anual contra a raiva', 'RAB-003', 6),
('2022-12-05', '2023-12-05', 'V10', 'Múltipla canina', 'V10-888', 7),
('2023-11-20', '2024-11-20', 'V4', 'Múltipla felina', 'V4-115', 8);

INSERT INTO announcement_model (title, description, event_date, location, announcement_type, user_id, contact) VALUES
('Feira de Adoção de Cães', 'Venha adotar seu novo melhor amigo.', '2024-05-10', 'Praça Central', 'EVENT', 6, '11966660003'),
('Procura-se Gato', 'Meu gato sumiu próximo ao centro.', NULL, 'Rua das Flores, 123', 'LOST', 4, '11988880001'),
('Venda de Cavalos', 'Cavalos de raça para montaria.', '2024-06-15', 'Fazenda Boa Vista', 'SELL', 5, '19977770002'),
('Encontrei um Cachorro', 'Cachorro preto, porte médio, com coleira azul.', NULL, 'Av. Paulista, 1000', 'FOUND', 9, '11933330006'),
('Leilão de Gado', 'Excelente oportunidade para pecuaristas.', '2024-07-20', 'Agropecuária Vale', 'EVENT', 8, '19944440005'),
('Gato SRD para Adoção', 'Lindo filhote de gato.', NULL, 'PetShop Cão Feliz', 'ADOPTION', 6, '11966660003');

INSERT INTO adoption_model (pet_id, owner_id, description, contact, adopted, publication_date) VALUES
(2, 4, 'Gatinha muito mansa e carinhosa, precisa de um lar telado.', '11988880001', false, '2024-01-10'),
(3, 6, 'Poodle fêmea resgatada, vacinada e vermifugada.', '11966660003', true, '2024-02-15'),
(5, 6, 'Pastor Alemão filhote, ideal para companhia e sítios.', '11966660003', false, '2024-03-20'),
(8, 10, 'Gatinho preto muito brincalhão e sapeca.', '11922220007', false, '2024-04-05');