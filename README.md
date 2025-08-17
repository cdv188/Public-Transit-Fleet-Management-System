# Public Transit Fleet Management System

A web-based application designed for public transit agencies to monitor, track, and optimize operations of diesel buses, electric light rail, and diesel-electric trains.

## 👥 Group Members

- Chester Don Valencerina  
- Louis Tran  
- Ali Nayef  

---

## 📌 Project Overview

This system enables real-time GPS tracking, energy/fuel consumption monitoring, predictive maintenance alerts, and performance analytics to enhance operational efficiency and reliability of public transit fleets.

<img width="1916" height="953" alt="ptfmsDashboard" src="https://github.com/user-attachments/assets/468e7e61-f265-47fd-bb3e-7792f9043b00" />

---

## 🧱 Technologies Used

- **Java 21**
- **Java Servlets**
- **Apache Tomcat 9.0.90+**
- **MySQL 8.0.40**
- **Maven**
- **NetBeans IDE**

---

## 🧩 Architecture & Design Patterns

The application uses a **3-tier architecture**:
- **Presentation Layer**
- **Business Logic Layer**
- **Data Access Layer**

### Design Patterns Implemented:
- DAO  
- Builder  
- Strategy  
- Observer  
- Command  

A controller class is used to manage communication across all layers.

---

## 🔐 Functional Requirements

### FR-01: User Registration & Authentication
- User roles: Transit Manager, Operator
- Login/logout and role-based access control

### FR-02: Vehicle Management
- Register vehicles (type, ID, fuel type, capacity, route)
<img width="1916" height="954" alt="ptfmsRegisterVehicle" src="https://github.com/user-attachments/assets/3c9d27e3-1337-40f6-844b-861ff4979cc6" />

### FR-03: GPS Tracking
- Real-time location reporting
- Arrival/departure logging
- Operator break/out-of-service logging
<img width="1917" height="957" alt="ptfmsGPSTracking" src="https://github.com/user-attachments/assets/43471e6b-56b5-4442-a8e4-e703e4bcf6ec" />

### FR-04: Energy/Fuel Monitoring
- Efficiency tracking per vehicle type
- Manager alerts for abnormal usage
<img width="1916" height="955" alt="ptfmsFuel" src="https://github.com/user-attachments/assets/3eac0761-6285-4c71-ba01-0420fab0e567" />

### FR-05: Predictive Maintenance Alerts
- Monitors component wear and diagnostics
- Alerts for servicing
- Schedule maintenance tasks
<img width="1917" height="955" alt="ptfmsAlerts" src="https://github.com/user-attachments/assets/66dbc08f-5e34-42bc-b8da-42392a634c7b" />

### FR-06: Reporting & Analytics
- Dashboards for maintenance and performance
- Reports for cost, efficiency, and fuel/energy
<img width="1916" height="952" alt="ptfmsReports" src="https://github.com/user-attachments/assets/d9e8274a-a6fd-4b10-8975-42d75d73b302" />

---
## 📦 Deliverables

- NetBeans Maven project as `.zip`: `Group#_Final_Project.zip`
- High-Level Design Document (HLD)
- SQL schema and sample data
- UML Diagrams (Use Case, Class, Component, Sequence)
- Source code with full Javadoc documentation
- GitHub repository with individual branches and pull requests

---

## 📁 Project File Structure

```
java/
├───businesslayers/
│   ├───builder/
│   │       Vehicle.java
│   │       VehicleBuilder.java
│   │
│   ├───command/
│   │   │   Command.java
│   │   │
│   │   ├───maintenanceCommand/
│   │   │       MaintenanceDashboardCommand.java
│   │   │       ShowMaintenanceByIdCommand.java
│   │   │       ShowMaintenanceCommand.java
│   │   │
│   │   ├───ReportAndGpsCommand/
│   │   │       GenerateReportCommand.java
│   │   │       LogOperatorStatusCommand.java
│   │   │       RunSystemChecksCommand.java
│   │   │       SimulateGPSCommand.java
│   │   │
│   │   ├───userAndLoginCommand/
│   │   │       LoginCommand.java
│   │   │       LogoutCommand.java
│   │   │       NavigateToRegisterPageCommand.java
│   │   │       RegisterUserCommand.java
│   │   │
│   │   └───vehiclecommand/
│   │           DeleteVehicleCommand.java
│   │           RegisterVehicleCommand.java
│   │           ShowVehicleByIdCommand.java
│   │           ShowVehicleListCommand.java
│   │           UpdateVehicleCommand.java
│   │
│   ├───observer/
│   │       AlertCreationObserver.java
│   │       ComponentWearMonitor.java
│   │       FuelConsumptionMonitor.java
│   │       MonitorObserver.java
│   │       MonitorSubject.java
│   │
│   ├───simplefactory/
│   │       VehicleFactory.java
│   │
│   └───strategy/
│           FuelCostReportStrategy.java
│           OperatorPerformanceReportStrategy.java
│           ReportStrategy.java
│
├───controller/
│       CommandFactory.java
│       FrontController.java
│
├───dataaccesslayer/
│   ├───consumptionlogs/
│   │       ConsumptionLogsDAO.java
│   │       ConsumptionLogsDAOImpl.java
│   │
│   ├───maintenance/
│   │       MaintenanceLog.java
│   │       MaintenanceLogDAO.java
│   │       MaintenanceLogImpl.java
│   │       MaintenanceLogLogic.java
│   │
│   ├───operatorlogs/
│   │       OperatorStatusLogsDAO.java
│   │       OperatorStatusLogsDAOImpl.java
│   │
│   ├───users/
│   │       User.java
│   │       UserDAO.java
│   │       UserDAOImpl.java
│   │
│   ├───vehiclelocation/
│   │       VehicleLocationsDAO.java
│   │       VehicleLocationsDAOImpl.java
│   │
│   └───vehicles/
│           VehicleDAO.java
│           VehicleDAOImpl.java
│           VehicleLogic.java
│
├───database/
│       DataSource.java
│       ptfms_db.sql
│
├───datatransferobject/
│       ConsumptionLog.java
│       OperatorStatusLog.java
│       VehicleLocation.java
│
└───filter/
        SecurityFilter.java
```
---

## 💻 GitHub Workflow

- One **Team Lead** manages a private repository
- Each member works on their own branch
- Pull Requests are required for all merges
- Instructor must be added as a collaborator
  
© 2025 Algonquin College — CST8288: Object-Oriented Programming  
