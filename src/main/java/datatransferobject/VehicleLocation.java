package datatransferobject;

import java.sql.Timestamp;

/**
 * DTO representing a vehicle's location record.
 */
public class VehicleLocation {

    private int locationId;
    private int vehicleId;
    private double latitude;
    private double longitude;
    private Timestamp timestamp;

    /** Default constructor. */
    public VehicleLocation() {
    }

    /** Creates a location record with the given details. */
    public VehicleLocation(int vehicleId, double latitude, double longitude, Timestamp timestamp) {
        this.vehicleId = vehicleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VehicleLocation{" +
                "locationId=" + locationId +
                ", vehicleId=" + vehicleId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + timestamp +
                '}';
    }
}
