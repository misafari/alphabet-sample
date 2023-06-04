CREATE TABLE app_user
(
    username   VARCHAR(190) PRIMARY KEY,
    password   VARCHAR(255) NOT NULL,
    deleted_at TIMESTAMP
);

CREATE TABLE role
(
    id   INTEGER SERIAL DEFAULT VALUE,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE user_role
(
    username VARCHAR(190) NOT NULL,
    role_id  INTEGER      NOT NULL
);
