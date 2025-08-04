package dataaccesslayer.vehicles;

import java.util.List;
import businesslayers.builder.Vehicle;

/**
 *
 * @author Chester
 */
public class VehicleLogic {
    private VehicleDAOImpl vehicleDAO = new VehicleDAOImpl();
    /**
     * Retrieves all authors from the database.
     * @return A list of all AuthorDTO objects
     */
    public boolean addVehicle(Vehicle vehicle){
       return vehicleDAO.addVehicle(vehicle);
    }
     /**
     * Retrieves a specific author by their ID.
     * @param id The ID of the author to retrieve
     * @return The AuthorDTO object with the specified ID, or null if not found
     */
    public Vehicle getVehicleById(int vehicleId){
       return vehicleDAO.getVehicleById(vehicleId);
    }
     /**
     * Adds a new author to the database.
     * @param author The AuthorDTO object containing the author information to add
     */
    public Vehicle getVehicleByNumber(String num){
       return vehicleDAO.getVehicleByNumber(num);
    }
    
    /**
     * Retrieves all vehicle
     * @return 
     */
    public List<Vehicle> getAllVehicles(){
        return vehicleDAO.getAllVehicles();
    }
    
    /**
     * Updates an existing Vehicle
     * @param vehicle to be updated
     * @return true if the update was successful
     */
    public boolean updateVehicle(Vehicle vehicle){
        return vehicleDAO.updateVehicle(vehicle);
    }
    
    /**
     * Deletes a vehicle by its ID
     * @param vehicleId vehicle ID to be deleted
     * @return true if the deleted successful
     */
    public boolean deleteVehicle(int vehicleId){
       return vehicleDAO.deleteVehicle(vehicleId);
    }
}
