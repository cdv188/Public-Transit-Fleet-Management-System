/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.VehicleLocation;
import java.util.List;

/**
 * DAO interface for VehicleLocations operations
 * @author Ali
 */
public interface VehicleLocationsDAO {
    /**
     * Add or update a vehicle's location
     * @param location The location to save
     * @return true if successful
     */
    boolean updateLocation(VehicleLocation location);
    
    /**
     * Get the current location of a vehicle
     * @param vehicleId The vehicle ID
     * @return The current location or null
     */
    VehicleLocation getCurrentLocation(int vehicleId);
    
    /**
     * Get all current vehicle locations
     * @return List of all current locations
     */
    List<VehicleLocation> getAllCurrentLocations();
    
    /**
     * Get location history for a vehicle
     * @param vehicleId The vehicle ID
     * @param limit Number of records to retrieve
     * @return List of historical locations
     */
    List<VehicleLocation> getLocationHistory(int vehicleId, int limit);
}