CREATE TABLE IF NOT EXISTS visit_status (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO visit_status VALUES
(1, "VISIT_BOOKED"),
(2, "VISIT_DONE"),
(3, "VISIT_CANCELLED");

CREATE TABLE IF NOT EXISTS visit (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    time_from TIMESTAMP NOT NULL,
    time_to TIMESTAMP NOT NULL,
    description VARCHAR(3000),

    paid TINYINT,
    deleted TINYINT DEFAULT 0 NOT NULL,

    visit_status_id INT NOT NULL,
    FOREIGN KEY (visit_status_id) REFERENCES visit_status (id),
    service_id INT NOT NULL,
    FOREIGN KEY (service_id) REFERENCES service (id),
    room_id INT NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS user_visit (
    user_id INT NOT NULL,
    visit_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (visit_id) REFERENCES visit (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS visit_equipment_item (
    visit_id INT NOT NULL,
    equipment_item_id INT NOT NULL,
    FOREIGN KEY (visit_id) REFERENCES visit (id),
    FOREIGN KEY (equipment_item_id) REFERENCES equipment_item (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS room_schedule (
    name VARCHAR(100),
    time_from TIMESTAMP NOT NULL,
    time_to TIMESTAMP NOT NULL,
    description VARCHAR(1000),
    deleted TINYINT NOT NULL,

    room_id INT NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS equipment_schedule (
    name VARCHAR(100),
    time_from TIMESTAMP NOT NULL,
    time_to TIMESTAMP NOT NULL,
    description VARCHAR(1000),
    deleted TINYINT NOT NULL,

    equipment_item_id INT NOT NULL,
    FOREIGN KEY (equipment_item_id) REFERENCES equipment_item (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS worker_schedule (
    name VARCHAR(100),
    time_from TIMESTAMP NOT NULL,
    time_to TIMESTAMP NOT NULL,
    description VARCHAR(1000),
    deleted TINYINT NOT NULL,

    worker_id INT NOT NULL,
    FOREIGN KEY (worker_id) REFERENCES user (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;