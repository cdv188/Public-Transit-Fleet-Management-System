package dataaccesslayer.vehicles;
import database.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import businesslayers.builder.Vehicle;
import businesslayers.simplefactory.VehicleFactory;

/**
 * Implementation of VehicleDAO
 */
public class VehicleDAOImpl implements VehicleDAO {
    private static final String INSERT_VEHICLE =
            "INSERT INTO vehicles (vehicle_number, vehicle_type, fuel_type, consumption_rate, max_passengers, assigned_route) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String VEHICLE_BY_ID = 
            "SELECT * FROM vehicles WHERE vehicle_id = ?";
    
    private static final String VEHICLE_BY_NUM = 
            "SELECT * FROM vehicles WHERE vehicle_number = ?";
    
    private static final String ALL_VEHICLE = 
            "SELECT * FROM vehicles";
    
    private static final String UPDATE_VEHICLE = 
            "UPDATE vehicles SET vehicle_number = ?,  vehicle_type = ?, fuel_type = ?, consumption_rate = ?, max_passengers = ?, assigned_route = ? WHERE vehicle_id = ?";
    
    /**
     * Add Vehicle
     * @param vehicle
     * @return 
     */
    @Override
    public boolean addVehicle(Vehicle vehicle) {
        try(Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_VEHICLE)){
            pstmt.setString(1, vehicle.getNumber());
            pstmt.setString(2, vehicle.getVehicleType());
            pstmt.setString(3, vehicle.getFuelType());
            pstmt.setDouble(4, vehicle.getConsumptionRate());
            pstmt.setInt(5, vehicle.getMaxCapacity());
            pstmt.setString(6, vehicle.getRoute());
            
            pstmt.executeUpdate();
            
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Retrieve vehicle by Id
     * @param vehicleId
     * @return 
     */
    @Override
    public Vehicle getVehicleById(int vehicleId) {
        Vehicle vehicle = null;
        try(Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(VEHICLE_BY_ID)){
            
            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                vehicle = Vehicle.builder()
                        .vehicleNumber(rs.getString("vehicle_number"))
                        .vehicleId(rs.getInt("vehicle_id"))
                        .vehicleType(rs.getString("vehicle_type"))
                        .fuelType(rs.getString("fuel_type"))
                        .consumptionRate(rs.getDouble("consumption_rate"))
                        .maxCapacity(rs.getInt("max_passengers"))
                        .route(rs.getString("assigned_route"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }
    /**
     * Retrieves all vehicles by Number
     * @return 
     */
    @Override
    public Vehicle getVehicleByNumber(String num) {
        Vehicle vehicle = null;
        try(Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(VEHICLE_BY_NUM)){
            
            pstmt.setString(1, num);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                vehicle = Vehicle.builder()
                        .vehicleNumber(rs.getString("vehicle_number"))
                        .vehicleId(rs.getInt("vehicle_id"))
                        .vehicleType(rs.getString("vehicle_type"))
                        .fuelType(rs.getString("fuel_type"))
                        .consumptionRate(rs.getDouble("consumption_rate"))
                        .maxCapacity(rs.getInt("max_passengers"))
                        .route(rs.getString("assigned_route"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        try(Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(ALL_VEHICLE)){
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Vehicle vehicle = new Vehicle();
                
                vehicle = Vehicle.builder()
                        .vehicleNumber(rs.getString("vehicle_number"))
                        .vehicleId(rs.getInt("vehicle_id"))
                        .vehicleType(rs.getString("vehicle_type"))
                        .fuelType(rs.getString("fuel_type"))
                        .consumptionRate(rs.getDouble("consumption_rate"))
                        .maxCapacity(rs.getInt("max_passengers"))
                        .route(rs.getString("assigned_route"))
                        .build();
                
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
    
    /**
     * Updates Vehicle
     * @param vehicle
     * @return true if updated success
     */
    @Override
    public boolean updateVehicle(Vehicle vehicle) {
        try(Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_VEHICLE)){
             pstmt.setString(1, vehicle.getNumber());
             pstmt.setString(2, vehicle.getVehicleType());
             pstmt.setString(3, vehicle.getFuelType());
             pstmt.setDouble(4, vehicle.getConsumptionRate());
             pstmt.setInt(5, vehicle.getMaxCapacity());
             pstmt.setString(6, vehicle.getRoute());
             pstmt.setInt(7, vehicle.getVehicleId());
             
             pstmt.executeUpdate();
             
             return true;
             
        }catch(SQLException e){
             e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete the vehicle from every table to handle foreign key constraints
     * @param vehicleId
     * @return true if it was success 
     */
    @Override
    public boolean deleteVehicle(int vehicleId) {
        try(Connection conn = DataSource.getInstance().getConnection();){
            // Delete the vehicle 
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM vehicles WHERE vehicle_id = ?")) {
                pstmt.setInt(1, vehicleId);
                pstmt.executeUpdate();
            }
            return true;
             
        }catch(SQLException e){
             e.printStackTrace();
        }
       return false;
    }
    
    
}
