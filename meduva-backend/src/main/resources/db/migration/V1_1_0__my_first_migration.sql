CREATE TABLE IF NOT EXISTS role (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS user (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(40) NOT NULL,
    login VARCHAR(20) DEFAULT NULL,
    password VARCHAR(256) NOT NULL,
    name varchar(30) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    phone_number VARCHAR(11) DEFAULT NULL,
    deleted TINYINT DEFAULT 0 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS user_role (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS refresh_token (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(256) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS password_reset_token (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(256) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO role VALUES
(null, "ROLE_CLIENT"),
(null, "ROLE_WORKER"),
(null, "ROLE_RECEPTIONIST"),
(null, "ROLE_ADMIN");

-- hashed password is 1234 --
INSERT INTO user VALUES
(1, "john.doe@gmail.com", "sampleLogin", "$2a$10$9qx.WFyh9819GyUlQh0M5uj6JZo7FXETpDH5BWeMpJYKY91afWZ2y", "John", "Doe", "48123123123", 0),
(2, "aaa.doe@gmail.com", "client", "$2a$10$9qx.WFyh9819GyUlQh0M5uj6JZo7FXETpDH5BWeMpJYKY91afWZ2y", "John", "Doe", "48123123123", 0),
(3, "bbb.doe@gmail.com", "worker", "$2a$10$9qx.WFyh9819GyUlQh0M5uj6JZo7FXETpDH5BWeMpJYKY91afWZ2y", "John", "Doe", "48123123123", 0),
(4, "ccc.doe@gmail.com", "receptionist", "$2a$10$9qx.WFyh9819GyUlQh0M5uj6JZo7FXETpDH5BWeMpJYKY91afWZ2y", "John", "Doe", "48123123123", 0),
(5, "ddd.doe@gmail.com", "admin", "$2a$10$9qx.WFyh9819GyUlQh0M5uj6JZo7FXETpDH5BWeMpJYKY91afWZ2y", "John", "Doe", "48123123123", 0);

INSERT INTO user_role VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 1),
(3, 1),
(3, 2),
(4, 1),
(4, 2),
(4, 3),
(5, 1),
(5, 2),
(5, 3),
(5, 4);