CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE photos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    url VARCHAR(255) NOT NULL,
    description TEXT,
    content_type VARCHAR(100) NOT NULL,
    size BIGINT NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    cpf CHAR(11) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    photo_id UUID,
    CONSTRAINT fk_users_photo FOREIGN KEY (photo_id) REFERENCES photos(id) ON DELETE SET NULL
);