/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataaccesslayer.vehiclelocation;

import datatransferobject.VehicleLocation;
import database.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of VehicleLocationsDAO
 * @author Ali
 */
public class VehicleLocationsDAOImpl implements VehicleLocationsDAO {
    
    private static final String UPDATE_LOCATION = 
        "INSERT INTO VehicleLocations (vehicle_id, latitude, longitude, timestamp) " +
        "VALUES (?, ?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE latitude = ?, longitude = ?, timestamp = ?";
    
    private static final String GET_CURRENT_LOCATION = 
        "SELECT * FROM VehicleLocations WHERE vehicle_id = ? ORDER BY timestamp DESC LIMIT 1";
    
    private static final String GET_ALL_CURRENT_LOCATIONS = 
        "SELECT vl1.* FROM VehicleLocations vl1 " +
        "INNER JOIN (SELECT vehicle_id, MAX(timestamp) as max_time " +
        "FROM VehicleLocations GROUP BY vehicle_id) vl2 " +
        "ON vl1.vehicle_id = vl2.vehicle_id AND vl1.timestamp = vl2.max_time";
    
    private static final String GET_LOCATION_HISTORY = 
        "SELECT * FROM VehicleLocations WHERE vehicle_id = ? " +
        "ORDER BY timestamp DESC LIMIT ?";
    
    @Override
    public boolean updateLocation(VehicleLocation location) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_LOCATION)) {
            
            pstmt.setInt(1, location.getVehicleId());
            pstmt.setDouble(2, location.getLatitude());
            pstmt.setDouble(3, location.getLongitude());
            pstmt.setTimestamp(4, location.getTimestamp());
            // For UPDATE part
            pstmt.setDouble(5, location.getLatitude());
            pstmt.setDouble(6, location.getLongitude());
            pstmt.setTimestamp(7, location.getTimestamp());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public VehicleLocation getCurrentLocation(int vehicleId) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_CURRENT_LOCATION)) {
            
            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractLocationFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<VehicleLocation> getAllCurrentLocations() {
        List<VehicleLocation> locations = new ArrayList<>();
        
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_ALL_CURRENT_LOCATIONS);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                locations.add(extractLocationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return locations;
    }
    
    @Override
    public List<VehicleLocation> getLocationHistory(int vehicleId, int limit) {
        List<VehicleLocation> locations = new ArrayList<>();
        
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_LOCATION_HISTORY)) {
            
            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                locations.add(extractLocationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return locations;
    }
    
    private VehicleLocation extractLocationFromResultSet(ResultSet rs) throws SQLException {
        VehicleLocation location = new VehicleLocation();
        location.setLocationId(rs.getInt("location_id"));
        location.setVehicleId(rs.getInt("vehicle_id"));
        location.setLatitude(rs.getDouble("latitude"));
        location.setLongitude(rs.getDouble("longitude"));
        location.setTimestamp(rs.getTimestamp("timestamp"));
        return location;
    }
}