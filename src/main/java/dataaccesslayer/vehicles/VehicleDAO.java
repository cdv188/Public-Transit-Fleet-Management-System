package dataaccesslayer.vehicles;

import java.util.List;
import businesslayers.builder.Vehicle;

/**
 * DAO interface for vehicle operations.
 */
public interface VehicleDAO {

    /**
     * Adds a new vehicle.
     *
     * @param vehicle the vehicle to add
     * @return true if successful
     */
    boolean addVehicle(Vehicle vehicle);

    /**
     * Retrieves a vehicle by its ID.
     *
     * @param vehicleId the vehicle ID
     * @return the vehicle, or null if not found
     */
    Vehicle getVehicleById(int vehicleId);

    /**
     * Retrieves a vehicle by its number.
     *
     * @param num the vehicle number
     * @return the vehicle, or null if not found
     */
    Vehicle getVehicleByNumber(String num);

    /**
     * Retrieves all vehicles.
     *
     * @return list of all vehicles
     */
    List<Vehicle> getAllVehicles();

    /**
     * Updates an existing vehicle.
     *
     * @param vehicle the vehicle to update
     * @return true if successful
     */
    boolean updateVehicle(Vehicle vehicle);

    /**
     * Deletes a vehicle by its ID.
     *
     * @param vehicleId the vehicle ID
     * @return true if successful
     */
    boolean deleteVehicle(int vehicleId);
}
