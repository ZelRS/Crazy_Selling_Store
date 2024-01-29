-- liquibase formatted sql

-- changeset YuriPetukhov:1

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255),
  firstName VARCHAR(255),
  lastName VARCHAR(255),
  phone VARCHAR(255),
  role VARCHAR(255),
  image VARCHAR(255),
  password VARCHAR(255)
);

CREATE TABLE ads (
  pk SERIAL PRIMARY KEY,
  price INT,
  title VARCHAR(255),
  description VARCHAR(255),
  email VARCHAR(255),
  image VARCHAR(255),
  user_id INT,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comments (
  pk SERIAL PRIMARY KEY,
  createdAt BIGINT,
  text VARCHAR(255),
  authorImage VARCHAR(255),
  user_id INT,
  FOREIGN KEY (user_id) REFERENCES users(id)
);


