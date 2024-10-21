CREATE TABLE IF NOT EXISTS Employee (
    employee_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    clock_state VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Time_entry (
    time_entry_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    entry_type VARCHAR(255) NOT NULL,
    entry_date_time DATETIME  NOT NULL
);

CREATE TABLE IF NOT EXISTS Work_schedule (
    work_schedule_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL UNIQUE,
    hours_monday FLOAT NOT NULL,
    hours_tuesday FLOAT  NOT NULL,
    hours_wednesday FLOAT  NOT NULL,
    hours_thursday FLOAT  NOT NULL,
    hours_friday FLOAT  NOT NULL,
    hours_saturday FLOAT  NOT NULL,
    hours_sunday FLOAT  NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
);
