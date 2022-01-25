CREATE TABLE IF NOT EXISTS visit (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    time_from TIMESTAMP NOT NULL,
    time_to TIMESTAMP NOT NULL,
    description VARCHAR(3000),
    booked TINYINT,
    cancelled TINYINT,
    done TINYINT,
    paid TINYINT,
    deleted TINYINT DEFAULT 0 NOT NULL,

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