DROP TABLE IF EXISTS vaccine_model CASCADE;
DROP TABLE IF EXISTS pet_model CASCADE;
DROP TABLE IF EXISTS user_model CASCADE;
DROP TABLE IF EXISTS annoucement_model CASCADE;

CREATE TABLE user_model
(
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    password VARCHAR(60),
    document VARCHAR(14),
    phone VARCHAR(15) NOT NULL,
    account_type VARCHAR(20) NOT NULL
);

CREATE TABLE pet_model
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    specie VARCHAR(15) NOT NULL,
    breed VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    user_id INT NOT NULL,
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

CREATE TABLE annoucement_model
(
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    publication_date DATE NOT NULL,
    event_date DATE,
    location VARCHAR(150) NOT NULL,
    annoucement_type VARCHAR(15) NOT NULL,
    user_id INT NOT NULL,

    CONSTRAINT fk_annoucement_creator  FOREIGN KEY (user_id) REFERENCES user_model(id) ON DELETE CASCADE
);
