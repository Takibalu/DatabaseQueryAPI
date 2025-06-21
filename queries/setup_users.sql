DROP TABLE users;

CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   name VARCHAR(100),
   email VARCHAR(100)
);

INSERT INTO users (name, email) VALUES
    ('Alice', 'alice@gmail.com'),
    ('Bob', 'bob@gmail.com');