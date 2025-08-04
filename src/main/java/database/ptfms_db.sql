DROP DATABASE IF EXISTS `ptfms_db`;
CREATE DATABASE `ptfms_db`;
USE `ptfms_db`;

-- -----------------------------------------------------
-- Table `Users`
-- -----------------------------------------------------
CREATE TABLE `Users` (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_type ENUM('Manager', 'Operator') NOT NULL
);

-- -----------------------------------------------------
-- Table `Vehicles`
-- -----------------------------------------------------
CREATE TABLE `Vehicles` (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_number VARCHAR(50) NOT NULL UNIQUE,
    vehicle_type ENUM('Diesel Bus', 'Electric Light Rail', 'Diesel-Electric Train') NOT NULL,
    fuel_type VARCHAR(50) NOT NULL,
    consumption_rate DECIMAL(10, 2) NOT NULL,
    max_passengers INT NOT NULL,
    assigned_route VARCHAR(50)
);

-- -----------------------------------------------------
-- Table `VehicleAssignments`
-- -----------------------------------------------------
CREATE TABLE `VehicleAssignments` (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- -----------------------------------------------------
-- Table `VehicleLocations`
-- -----------------------------------------------------
CREATE TABLE `VehicleLocations` (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    latitude DECIMAL(9, 6) NOT NULL,
    longitude DECIMAL(9, 6) NOT NULL,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- -----------------------------------------------------
-- Table `OperatorStatusLogs`
-- -----------------------------------------------------
CREATE TABLE `OperatorStatusLogs` (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    status ENUM('In-Service', 'On Break', 'Out-of-Service') NOT NULL,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- -----------------------------------------------------
-- Table `ConsumptionLogs`
-- -----------------------------------------------------
CREATE TABLE `ConsumptionLogs` (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    log_date DATE NOT NULL,
    usage_amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- -----------------------------------------------------
-- Table `VehicleComponents`
-- -----------------------------------------------------
CREATE TABLE `VehicleComponents` (
    component_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    component_name VARCHAR(100) NOT NULL,
    hours_of_use INT NOT NULL,
    wear_threshold_hours INT NOT NULL,
    UNIQUE(vehicle_id, component_name),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- -----------------------------------------------------
-- Table `MaintenanceLog`
-- -----------------------------------------------------
CREATE TABLE `MaintenanceLog` (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    task_description TEXT NOT NULL,
    scheduled_date DATE,
    completion_date DATE,
    status ENUM('Scheduled', 'In-Progress', 'Completed', 'Alert') NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- -----------------------------------------------------
-- INSERT statements (preserved from original)
-- -----------------------------------------------------

-- Example Users
INSERT INTO Users (name, email, password, user_type) VALUES
('John Manager', 'manager1@example.com', 'pass123', 'Manager'),
('Jane Operator', 'operator1@example.com', 'pass123', 'Operator'),
('Mike Operator', 'operator2@example.com', 'pass123', 'Operator');

-- Example Vehicles
INSERT INTO Vehicles (vehicle_number, vehicle_type, fuel_type, consumption_rate, max_passengers, assigned_route) VALUES
('BUS-001', 'Diesel Bus', 'Diesel', 25.5, 50, 'Route 1'),
('TRAIN-101', 'Diesel-Electric Train', 'Diesel-Electric', 200.0, 200, 'Route 2'),
('TRAM-202', 'Electric Light Rail', 'Electric', 150.0, 100, 'Route 3');

-- Example VehicleAssignments
INSERT INTO VehicleAssignments (user_id, vehicle_id, start_time, end_time) VALUES
(2, 1, '2025-08-01 08:00:00', NULL),
(3, 2, '2025-08-01 09:00:00', NULL);

-- Example VehicleLocations
INSERT INTO VehicleLocations (vehicle_id, latitude, longitude, timestamp) VALUES
(1, 45.4215, -75.6972, NOW()),
(2, 45.5017, -73.5673, NOW());

-- Example OperatorStatusLogs
INSERT INTO OperatorStatusLogs (user_id, status, timestamp) VALUES
(2, 'In-Service', NOW()),
(3, 'On Break', NOW());

-- Example ConsumptionLogs
INSERT INTO ConsumptionLogs (vehicle_id, log_date, usage_amount) VALUES
(1, '2025-08-01', 30.0),
(2, '2025-08-01', 210.0);

-- Example VehicleComponents
INSERT INTO VehicleComponents (vehicle_id, component_name, hours_of_use, wear_threshold_hours) VALUES
(1, 'Engine', 900, 1000),
(1, 'Brakes', 800, 900),
(2, 'Engine', 1900, 2000);

-- Example MaintenanceLog
INSERT INTO MaintenanceLog (vehicle_id, task_description, scheduled_date, completion_date, status) VALUES
(1, 'Oil change', '2025-08-10', NULL, 'Scheduled'),
(2, 'Brake replacement', '2025-08-12', NULL, 'Scheduled');

