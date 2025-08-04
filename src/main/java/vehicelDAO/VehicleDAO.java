package vehicelDAO;
import java.util.List;
import vehicleSimpleFactory.Vehicle;
/**
 * DAO interface for vehicle Operations
 * @author Chester
 */
public interface VehicleDAO {
    /**
     * Adds a new vehicle to the database
     * @param vehicle the vehicle to be added
     * @return true if the vehicle was added successfully
     */
    public boolean addVehicle(Vehicle vehicle);
    
    /**
     * Retrieves the vehicle by it's ID
     * @param vehicleId
     * @return The vehicle object
     */
    public Vehicle getVehicleById(int vehicleId);
    
    /**
     * Retrieves the vehicle by it's Number
     * @param num vehicle Number
     * @return vehicle Object
     */
    public Vehicle getVehicleByNumber(String num);
    
    /**
     * Retrieves all vehicle
     * @return 
     */
    public List<Vehicle> getAllVehicles();
    
    /**
     * Updates an existing Vehicle
     * @param vehicle to be updated
     * @return true if the update was successful
     */
    public boolean updateVehicle(Vehicle vehicle);
    
    /**
     * Deletes a vehicle by its ID
     * @param vehicleId vehicle ID to be deleted
     * @return true if the deleted successful
     */
    public boolean deleteVehicle(int vehicleId);
    
}
