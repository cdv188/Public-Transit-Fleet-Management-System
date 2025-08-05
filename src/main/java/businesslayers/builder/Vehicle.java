package businesslayers.builder;

/**
 * Represents a vehicle with type, fuel type, capacity, route, and consumption rate.
 */
public class Vehicle {

    private String vehicleType;
    private String fuelType;
    private String number;
    private double consumptionRate;
    private int maxCapacity;
    private String route;
    private int vehicleId;

    /**
     * Creates a new vehicle with all details specified.
     *
     * @param number           vehicle number
     * @param vehicleType      type of vehicle
     * @param fuelType         fuel type
     * @param maxCapacity      maximum capacity
     * @param consumptionRate  fuel consumption rate
     * @param route            route assigned
     * @param vehicleId        unique vehicle ID
     */
    Vehicle(String number, String vehicleType, String fuelType, int maxCapacity, double consumptionRate, String route, int vehicleId) {
        this.consumptionRate = consumptionRate;
        this.number = number;
        this.maxCapacity = maxCapacity;
        this.fuelType = fuelType;
        this.vehicleType = vehicleType;
        this.route = route;
        this.vehicleId = vehicleId;
    }

    /** Default constructor. */
    public Vehicle() {
    }

    /**
     * Returns formatted vehicle details.
     *
     * @return a string with vehicle details
     */
    public String getVehicleDetails() {
        return String.format("Number: %s\nType: %s\nFuel Type: %s\nConsumption Rate: %.2f"
                        + "\nMax Capacity: %d\nRoute: %s",
                getNumber(), getVehicleType(), getFuelType(), getConsumptionRate(), getMaxCapacity(), getRoute());
    }

    /**
     * Returns a new {@link VehicleBuilder}.
     *
     * @return a vehicle builder
     */
    public static VehicleBuilder builder() {
        return new VehicleBuilder();
    }

    /** @return vehicle type */
    public String getVehicleType() {
        return vehicleType;
    }

    /** @return fuel type */
    public String getFuelType() {
        return fuelType;
    }

    /** @return vehicle number */
    public String getNumber() {
        return number;
    }

    /** @return fuel consumption rate */
    public double getConsumptionRate() {
        return consumptionRate;
    }

    /** @return maximum capacity */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /** @return route */
    public String getRoute() {
        return route;
    }

    /** @return vehicle ID */
    public int getVehicleId() {
        return vehicleId;
    }
}
