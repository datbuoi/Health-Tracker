-- Create Database
CREATE DATABASE IF NOT EXISTS health_tracker;
USE health_tracker;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    birth_date DATE,
    gender VARCHAR(10),
    height_cm FLOAT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Weight Logs Table
CREATE TABLE IF NOT EXISTS weight_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    weight_kg FLOAT NOT NULL,
    bmi FLOAT NOT NULL,
    log_date DATE NOT NULL,
    note TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Water Logs Table
CREATE TABLE IF NOT EXISTS water_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amount_ml INT NOT NULL,
    log_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sleep Logs Table
CREATE TABLE IF NOT EXISTS sleep_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    sleep_time DATETIME NOT NULL,
    wake_time DATETIME NOT NULL,
    quality VARCHAR(20),
    note TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert a default user (optional, for testing)
-- INSERT INTO users (username, password, full_name, birth_date, gender, height_cm) 
-- VALUES ('admin', 'admin123', 'Default Admin', '2000-01-01', 'Nam', 170.0);
