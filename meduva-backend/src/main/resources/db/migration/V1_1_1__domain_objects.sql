CREATE TABLE IF NOT EXISTS service (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(3000),
    duration_in_min INT NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    itemless TINYINT NOT NULL,
    deleted TINYINT DEFAULT 0 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS room (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(3000),
    deleted TINYINT DEFAULT 0 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS room_service (
    room_id INT NOT NULL,
    service_id INT NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (id),
    FOREIGN KEY (service_id) REFERENCES service (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS equipment_model (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    is_active TINYINT DEFAULT 1 NOT NULL,
    deleted TINYINT DEFAULT 0 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS service_equipment_model (
    equipment_model_id INT NOT NULL,
    service_id INT NOT NULL,
    FOREIGN KEY (equipment_model_id) REFERENCES equipment_model (id),
    FOREIGN KEY (service_id) REFERENCES service (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS equipment_item (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    is_active TINYINT DEFAULT 1 NOT NULL,
    deleted TINYINT DEFAULT 0 NOT NULL,
    equipment_model_id INT NOT NULL,
    room_id INT,
    FOREIGN KEY (equipment_model_id) REFERENCES equipment_model (id),
    FOREIGN KEY (room_id) REFERENCES room (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS worker_service (
    service_id INT NOT NULL,
    worker_id INT NOT NULL,
    FOREIGN KEY (service_id) REFERENCES service (id),
    FOREIGN KEY (worker_id) REFERENCES user (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;