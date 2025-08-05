package dataaccesslayer.vehicles;

import java.util.List;
import businesslayers.builder.Vehicle;

/**
 * Business logic layer for vehicle operations.
 */
public class VehicleLogic {

    private VehicleDAOImpl vehicleDAO = new VehicleDAOImpl();

    /**
     * Adds a new vehicle.
     *
     * @param vehicle the vehicle to add
     * @return true if successful
     */
    public boolean addVehicle(Vehicle vehicle) {
        return vehicleDAO.addVehicle(vehicle);
    }

    /**
     * Retrieves a vehicle by its ID.
     *
     * @param vehicleId the vehicle ID
     * @return the vehicle, or null if not found
     */
    public Vehicle getVehicleById(int vehicleId) {
        return vehicleDAO.getVehicleById(vehicleId);
    }

    /**
     * Retrieves a vehicle by its number.
     *
     * @param num the vehicle number
     * @return the vehicle, or null if not found
     */
    public Vehicle getVehicleByNumber(String num) {
        return vehicleDAO.getVehicleByNumber(num);
    }

    /**
     * Retrieves all vehicles.
     *
     * @return list of all vehicles
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.getAllVehicles();
    }

    /**
     * Updates an existing vehicle.
     *
     * @param vehicle the vehicle to update
     * @return true if successful
     */
    public boolean updateVehicle(Vehicle vehicle) {
        return vehicleDAO.updateVehicle(vehicle);
    }

    /**
     * Deletes a vehicle by its ID.
     *
     * @param vehicleId the vehicle ID
     * @return true if successful
     */
    public boolean deleteVehicle(int vehicleId) {
        return vehicleDAO.deleteVehicle(vehicleId);
    }
}
