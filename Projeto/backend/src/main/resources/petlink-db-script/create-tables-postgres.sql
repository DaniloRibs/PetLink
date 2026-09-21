DROP TABLE IF EXISTS announcement_model CASCADE;
DROP TABLE IF EXISTS adoption_model CASCADE;
DROP TABLE IF EXISTS farm_animal_sale_item CASCADE;
DROP TABLE IF EXISTS farm_animal_sale CASCADE;
DROP TABLE IF EXISTS vaccine_model CASCADE;
DROP TABLE IF EXISTS farm_animal_model CASCADE;
DROP TABLE IF EXISTS pet_model CASCADE;
DROP TABLE IF EXISTS animal_model CASCADE;
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

CREATE TABLE animal_model
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    species VARCHAR(15) NOT NULL,
    breed VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    user_id INT NOT NULL,
    gender CHAR NOT NULL,
    CONSTRAINT fk_pet_user FOREIGN KEY (user_id) REFERENCES user_model(id) ON DELETE CASCADE
);

CREATE TABLE farm_animal_model
(
    id SERIAL PRIMARY KEY,
    identifier VARCHAR(10) NOT NULL,
    for_sell BOOLEAN NOT NULL,
    weight NUMERIC NOT NULL,
    animal_id INT NOT NULL,
    CONSTRAINT fk_farm_animal_animal FOREIGN KEY (animal_id) REFERENCES animal_model(id) ON DELETE CASCADE
);

CREATE TABLE pet_model
(
    id SERIAL PRIMARY KEY,
    for_adoption BOOLEAN NOT NULL,
    animal_id INT NOT NULL,
    CONSTRAINT fk_pet_animal FOREIGN KEY (animal_id) REFERENCES animal_model(id) ON DELETE CASCADE
);


CREATE TABLE vaccine_model
(
    id SERIAL PRIMARY KEY,
    application_date DATE NOT NULL,
    expiration_date DATE,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(100),
    batch VARCHAR(15),
    animal_id INT NOT NULL,
    CONSTRAINT fk_vaccine_pet FOREIGN KEY (animal_id) REFERENCES animal_model(id) ON DELETE CASCADE
);

CREATE TABLE announcement_model
(
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    event_date DATE,
    location VARCHAR(150),
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

    CONSTRAINT fk_adoption_pet FOREIGN KEY (pet_id) REFERENCES animal_model(id) ON DELETE CASCADE,
    CONSTRAINT fk_adoption_owner FOREIGN KEY (owner_id) REFERENCES user_model(id) ON DELETE CASCADE
);

CREATE TABLE farm_animal_sale
(
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    price_type VARCHAR(15) NOT NULL,
    price_per_arroba NUMERIC,
    price NUMERIC NOT NULL,
    user_id INT NOT NULL,
    contact VARCHAR(100) NOT NULL,
    CONSTRAINT fk_farm_animal_sale_user FOREIGN KEY (user_id) REFERENCES user_model(id) ON DELETE CASCADE
);

CREATE TABLE farm_animal_sale_item
(
    id SERIAL PRIMARY KEY,
    farm_animal_sale_id INT NOT NULL,
    farm_animal_id INT NOT NULL,
    CONSTRAINT fk_sale_item_sale FOREIGN KEY (farm_animal_sale_id) REFERENCES farm_animal_sale(id) ON DELETE CASCADE,
    CONSTRAINT fk_sale_item_animal FOREIGN KEY (farm_animal_id) REFERENCES farm_animal_model(id) ON DELETE CASCADE
);