-- CST8288 Final Project: Public Transit Fleet Management System (PTFMS)
-- This script creates the database, all required tables, and populates them with
-- sample data for testing purposes.

-- =============================================
-- DATABASE CREATION
-- =============================================
DROP DATABASE IF EXISTS `ptfms_db`;
CREATE DATABASE `ptfms_db`;
USE `ptfms_db`;

-- =============================================
-- TABLE STRUCTURES
-- =============================================

-- FR-01: User Registration & Authentication
CREATE TABLE `Users` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL, -- In a real application, this MUST be a hashed password.
    `user_type` ENUM('Manager', 'Operator') NOT NULL
);

-- =============================================
-- ASSOCIATION TABLE: VehicleAssignments
-- This table connects an Operator (User) to a Vehicle for a specific shift or time period.
-- This is the missing link between Users and Vehicles.
-- =============================================
CREATE TABLE `VehicleAssignments` (
    `assignment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `vehicle_id` INT NOT NULL,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME, -- This can be NULL if the shift is currently active
    FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`),
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`)
);

-- FR-02: Vehicle Management
CREATE TABLE `Vehicles` (
    `vehicle_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_number` VARCHAR(50) NOT NULL UNIQUE,
    `vehicle_type` ENUM('Diesel Bus', 'Electric Light Rail', 'Diesel-Electric Train') NOT NULL,
    `fuel_type` VARCHAR(50) NOT NULL,
    `consumption_rate` DECIMAL(10, 2) NOT NULL, -- e.g., Liters per 100km or kWh per mile
    `max_passengers` INT NOT NULL,
    `assigned_route` VARCHAR(50)
);

-- FR-03: GPS Tracking
-- For simplicity, this table can store the latest reported location.
-- A full history table would not have a UNIQUE constraint on vehicle_id.
CREATE TABLE `VehicleLocations` (
    `location_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_id` INT NOT NULL,
    `latitude` DECIMAL(9, 6) NOT NULL,
    `longitude` DECIMAL(9, 6) NOT NULL,
    `timestamp` DATETIME NOT NULL,
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`)
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
    `usage_amount` DECIMAL(10, 2) NOT NULL, -- e.g., Liters or kWh
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`)
);

-- FR-05: Alerts for Predictive Maintenance (Component Tracking)
CREATE TABLE `VehicleComponents` (
    `component_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_id` INT NOT NULL,
    `component_name` VARCHAR(100) NOT NULL, -- e.g., 'Brakes', 'Tires', 'Pantograph'
    `hours_of_use` INT NOT NULL,
    `wear_threshold_hours` INT NOT NULL, -- The number of hours at which maintenance is recommended
    UNIQUE(`vehicle_id`, `component_name`),
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`)
);

-- FR-05: Alerts for Predictive Maintenance (Maintenance Scheduling)
CREATE TABLE `MaintenanceLog` (
    `task_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_id` INT NOT NULL,
    `task_description` TEXT NOT NULL,
    `scheduled_date` DATE,
    `completion_date` DATE,
    `status` ENUM('Scheduled', 'In-Progress', 'Completed', 'Alert') NOT NULL, -- 'Alert' for auto-triggered events
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`)
);

-- =============================================
-- SAMPLE DATA POPULATION
-- =============================================

-- Add Users (1 Manager, 3 Operators)
-- NOTE: Using plain text passwords for project simplicity. In production, use a library like jBcrypt to hash passwords.
INSERT INTO `Users` (`name`, `email`, `password`, `user_type`) VALUES
('Alice Manager', 'manager@ptfms.com', 'manager123', 'Manager'),
('Bob Operator', 'bob@ptfms.com', 'operator123', 'Operator'),
('Charlie Operator', 'charlie@ptfms.com', 'operator456', 'Operator'),
('David Operator', 'david@ptfms.com', 'operator789', 'Operator');

-- Add Vehicles (Buses, Light Rail, Train)
INSERT INTO `Vehicles` (`vehicle_number`, `vehicle_type`, `fuel_type`, `consumption_rate`, `max_passengers`, `assigned_route`) VALUES
('BUS-101', 'Diesel Bus', 'Diesel', 35.5, 60, 'Route 7'),
('BUS-102', 'Diesel Bus', 'CNG', 40.2, 60, 'Route 12'),
('LR-201', 'Electric Light Rail', 'Electricity', 2.5, 150, 'Line 1'),
('LR-202', 'Electric Light Rail', 'Electricity', 2.8, 150, 'Line 2'),
('TRN-301', 'Diesel-Electric Train', 'Diesel', 80.0, 300, 'Corridor Express');

-- Add Initial GPS Locations for vehicles
INSERT INTO `VehicleLocations` (`vehicle_id`, `latitude`, `longitude`, `timestamp`) VALUES
(1, 45.421530, -75.697193, NOW()),
(2, 45.425500, -75.700000, NOW()),
(3, 45.411172, -75.712688, NOW()),
(4, 45.415935, -75.681328, NOW()),
(5, 45.383930, -75.760920, NOW());

-- Add sample operator status logs
INSERT INTO `OperatorStatusLogs` (`user_id`, `status`, `timestamp`) VALUES
(2, 'In-Service', '2025-07-20 08:00:00'),
(2, 'On Break', '2025-07-20 12:05:00'),
(3, 'In-Service', '2025-07-20 09:00:00');

-- Add Fuel/Energy Consumption Logs for the last 3 days
-- BUS-101 (Diesel) - One day has excessive consumption
INSERT INTO `ConsumptionLogs` (`vehicle_id`, `log_date`, `usage_amount`) VALUES
(1, '2025-07-18', 40.0),
(1, '2025-07-19', 42.5),
(1, '2025-07-20', 65.0); -- Excessive consumption to trigger alert

-- LR-201 (Electric)
INSERT INTO `ConsumptionLogs` (`vehicle_id`, `log_date`, `usage_amount`) VALUES
(3, '2025-07-18', 250.0),
(3, '2025-07-19', 255.5),
(3, '2025-07-20', 260.0);

-- TRN-301 (Train)
INSERT INTO `ConsumptionLogs` (`vehicle_id`, `log_date`, `usage_amount`) VALUES
(5, '2025-07-18', 850.0),
(5, '2025-07-19', 840.7),
(5, '2025-07-20', 865.0);

-- Add Vehicle Components with wear data
-- Set one component near its threshold to test predictive alerts
INSERT INTO `VehicleComponents` (`vehicle_id`, `component_name`, `hours_of_use`, `wear_threshold_hours`) VALUES
(1, 'Brakes', 450, 500), -- Close to threshold
(1, 'Tires', 200, 1000),
(3, 'Pantograph', 980, 1000), -- Close to threshold
(3, 'Circuit Breakers', 150, 2000),
(5, 'Engine', 1800, 2500);

-- Add sample maintenance logs
INSERT INTO `MaintenanceLog` (`vehicle_id`, `task_description`, `scheduled_date`, `completion_date`, `status`) VALUES
(1, 'Routine 500-hour inspection.', '2025-06-01', '2025-06-02', 'Completed'),
(3, 'ALERT: Pantograph nearing wear threshold.', NULL, NULL, 'Alert'),
(5, 'Annual engine checkup.', '2025-08-15', NULL, 'Scheduled');

INSERT INTO `VehicleAssignments` (`user_id`, `vehicle_id`, `start_time`, `end_time`) VALUES
-- Operator Bob (user_id 2) drove BUS-101 (vehicle_id 1) for a shift today.
(2, 1, '2025-07-21 06:00:00', '2025-07-21 14:00:00'),

-- Operator Charlie (user_id 3) is currently driving LR-201 (vehicle_id 3). Shift is active.
(3, 3, '2025-07-21 08:30:00', NULL),

-- Operator David (user_id 4) drove the Train (vehicle_id 5) yesterday.
(4, 5, '2025-07-20 10:00:00', '2025-07-20 18:00:00');

