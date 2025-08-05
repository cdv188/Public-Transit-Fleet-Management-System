package dataaccesslayer.vehicles;

import database.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import businesslayers.builder.Vehicle;

/**
 * Implementation of {@link VehicleDAO} for managing vehicles in the database.
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
        "UPDATE vehicles SET vehicle_number = ?, vehicle_type = ?, fuel_type = ?, consumption_rate = ?, max_passengers = ?, assigned_route = ? WHERE vehicle_id = ?";

    private static final String DELETE_VEHICLE =
        "DELETE FROM vehicles WHERE vehicle_id = ?";

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_VEHICLE)) {

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

    @Override
    public Vehicle getVehicleById(int vehicleId) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(VEHICLE_BY_ID)) {

            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return buildVehicleFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Vehicle getVehicleByNumber(String num) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(VEHICLE_BY_NUM)) {

            pstmt.setString(1, num);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return buildVehicleFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(ALL_VEHICLE);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                vehicles.add(buildVehicleFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public boolean updateVehicle(Vehicle vehicle) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_VEHICLE)) {

            pstmt.setString(1, vehicle.getNumber());
            pstmt.setString(2, vehicle.getVehicleType());
            pstmt.setString(3, vehicle.getFuelType());
            pstmt.setDouble(4, vehicle.getConsumptionRate());
            pstmt.setInt(5, vehicle.getMaxCapacity());
            pstmt.setString(6, vehicle.getRoute());
            pstmt.setInt(7, vehicle.getVehicleId());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteVehicle(int vehicleId) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_VEHICLE)) {

            pstmt.setInt(1, vehicleId);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Builds a {@link Vehicle} from the current row of the result set. */
    private Vehicle buildVehicleFromResultSet(ResultSet rs) throws SQLException {
        return Vehicle.builder()
                .vehicleNumber(rs.getString("vehicle_number"))
                .vehicleId(rs.getInt("vehicle_id"))
                .vehicleType(rs.getString("vehicle_type"))
                .fuelType(rs.getString("fuel_type"))
                .consumptionRate(rs.getDouble("consumption_rate"))
                .maxCapacity(rs.getInt("max_passengers"))
                .route(rs.getString("assigned_route"))
                .build();
    }
}
