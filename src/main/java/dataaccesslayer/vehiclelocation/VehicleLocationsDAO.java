package dataaccesslayer.vehiclelocation;

import datatransferobject.VehicleLocation;
import java.util.List;

/**
 * DAO interface for vehicle location operations.
 */
public interface VehicleLocationsDAO {

    /**
     * Adds or updates a vehicle's location.
     *
     * @param location the location to save
     * @return true if successful
     */
    boolean updateLocation(VehicleLocation location);

    /**
     * Retrieves the current location of a vehicle.
     *
     * @param vehicleId the vehicle ID
     * @return the current location or null
     */
    VehicleLocation getCurrentLocation(int vehicleId);

    /**
     * Retrieves the current location of all vehicles.
     *
     * @return list of current locations
     */
    List<VehicleLocation> getAllCurrentLocations();

    /**
     * Retrieves recent location history for a vehicle.
     *
     * @param vehicleId the vehicle ID
     * @param limit     the maximum number of records
     * @return list of historical locations
     */
    List<VehicleLocation> getLocationHistory(int vehicleId, int limit);
}
