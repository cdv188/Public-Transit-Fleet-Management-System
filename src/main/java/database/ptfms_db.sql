-- CST8288 Final Project: Public Transit Fleet Management System (PTFMS)
DROP DATABASE IF EXISTS `ptfms_db`;
CREATE DATABASE `ptfms_db`;
USE `ptfms_db`;

-- FR-01: User Registration & Authentication
CREATE TABLE `Users` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `user_type` ENUM('Manager', 'Operator') NOT NULL
);

-- ASSOCIATION TABLE: VehicleAssignments
CREATE TABLE `VehicleAssignments` (
    `assignment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `vehicle_id` INT NOT NULL,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME,
    FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`),
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`) ON DELETE CASCADE
);

-- FR-02: Vehicle Management
CREATE TABLE `Vehicles` (
    `vehicle_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_number` VARCHAR(50) NOT NULL UNIQUE,
    `vehicle_type` VARCHAR(50) NOT NULL,
    `fuel_type` VARCHAR(50) NOT NULL,
    `consumption_rate` DECIMAL(10, 2) NOT NULL,
    `max_passengers` INT NOT NULL,
    `assigned_route` VARCHAR(50)
);

-- FR-03: GPS Tracking
CREATE TABLE `VehicleLocations` (
    `location_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_id` INT NOT NULL,
    `latitude` DECIMAL(9, 6) NOT NULL,
    `longitude` DECIMAL(9, 6) NOT NULL,
    `timestamp` DATETIME NOT NULL,
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`) ON DELETE CASCADE
);

-- FR-03: Operator Status Logging
CREATE TABLE `OperatorStatusLogs` (
    `log_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `status` ENUM('In-Service', 'On Break', 'Out-of-Service') NOT NULL,
    `timestamp` DATETIME NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`)
);

-- FR-04: Monitoring Energy/Fuel Consumption
CREATE TABLE `ConsumptionLogs` (
    `log_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_id` INT NOT NULL,
    `log_date` DATE NOT NULL,
    `usage_amount` DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`) ON DELETE CASCADE
);

-- FR-05: Alerts for Predictive Maintenance (Component Tracking)
CREATE TABLE `VehicleComponents` (
    `component_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_id` INT NOT NULL,
    `component_name` VARCHAR(100) NOT NULL,
    `hours_of_use` INT NOT NULL,
    `wear_threshold_hours` INT NOT NULL,
    UNIQUE(`vehicle_id`, `component_name`),
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`) ON DELETE CASCADE
);

-- FR-05: Alerts for Predictive Maintenance (Maintenance Scheduling)
CREATE TABLE `MaintenanceLog` (
    `task_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_id` INT NOT NULL,
    `task_description` TEXT NOT NULL,
    `scheduled_date` DATE,
    `completion_date` DATE,
    `status` ENUM('Scheduled', 'In-Progress', 'Completed', 'Alert') NOT NULL,
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`) ON DELETE CASCADE
);

-- SAMPLE DATA

-- Add Users (1 Manager, 3 Operators)
INSERT INTO `Users` (`name`, `email`, `password`, `user_type`) VALUES
('Alice Manager', 'cst8288@ptfms.com', 'cst8288', 'Manager'),
('Bob Operator', 'bob@ptfms.com', 'operator123', 'Operator'),
('Charlie Operator', 'charlie@ptfms.com', 'operator456', 'Operator'),
('David Operator', 'david@ptfms.com', 'operator789', 'Operator');

INSERT INTO `Vehicles` (`vehicle_number`, `vehicle_type`, `fuel_type`, `consumption_rate`, `max_passengers`, `assigned_route`) VALUES
('BUS-101', 'Diesel Bus', 'Diesel', 35.5, 60, 'Route 7'),
('BUS-102', 'Diesel Bus', 'CNG', 40.2, 60, 'Route 12'),
('LR-201', 'Electric Light Rail', 'Electricity', 2.5, 150, 'Line 1'),
('LR-202', 'Electric Light Rail', 'Electricity', 2.8, 150, 'Line 2'),
('TRN-301', 'Diesel-Electric Train', 'Diesel', 80.0, 300, 'Corridor Express');

INSERT INTO `VehicleLocations` (`vehicle_id`, `latitude`, `longitude`, `timestamp`) VALUES
(1, 45.421530, -75.697193, NOW()),
(2, 45.425500, -75.700000, NOW()),
(3, 45.411172, -75.712688, NOW()),
(4, 45.415935, -75.681328, NOW()),
(5, 45.383930, -75.760920, NOW());

INSERT INTO `OperatorStatusLogs` (`user_id`, `status`, `timestamp`) VALUES
(2, 'In-Service', '2025-07-20 08:00:00'),
(2, 'On Break', '2025-07-20 12:05:00'),
(3, 'In-Service', '2025-07-20 09:00:00');

INSERT INTO `ConsumptionLogs` (`vehicle_id`, `log_date`, `usage_amount`) VALUES
(1, '2025-07-18', 40.0),
(1, '2025-07-19', 42.5),
(1, '2025-07-20', 65.0);

INSERT INTO `ConsumptionLogs` (`vehicle_id`, `log_date`, `usage_amount`) VALUES
(3, '2025-07-18', 250.0),
(3, '2025-07-19', 255.5),
(3, '2025-07-20', 260.0);


INSERT INTO `ConsumptionLogs` (`vehicle_id`, `log_date`, `usage_amount`) VALUES
(5, '2025-07-18', 850.0),
(5, '2025-07-19', 840.7),
(5, '2025-07-19', 2840.7),
(5, '2025-07-20', 865.0);

INSERT INTO `VehicleComponents` (`vehicle_id`, `component_name`, `hours_of_use`, `wear_threshold_hours`) VALUES
(1, 'Brakes', 450, 500),
(1, 'Tires', 200, 1000),
(3, 'Pantograph', 980, 1000),
(3, 'Circuit Breakers', 150, 2000),
(5, 'Engine', 1800, 2500);

INSERT INTO `MaintenanceLog` (`vehicle_id`, `task_description`, `scheduled_date`, `completion_date`, `status`) VALUES
(1, 'Routine 500-hour inspection.', '2025-06-01', '2025-06-02', 'Completed'),
(3, 'ALERT: Pantograph nearing wear threshold.', NULL, NULL, 'Alert'),
(5, 'Annual engine checkup.', '2025-08-15', NULL, 'Scheduled');

