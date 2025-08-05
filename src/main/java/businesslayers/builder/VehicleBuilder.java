package businesslayers.builder;

/**
 * Builder for creating {@link Vehicle} objects with a fluent API.
 */
public class VehicleBuilder {

    private String vehicleType;
    private String fuelType;
    private String number;
    private double consumptionRate;
    private int maxCapacity;
    private String route;
    private int vehicleId;

    /**
     * Sets the vehicle number.
     *
     * @param num vehicle number
     * @return this builder
     */
    public VehicleBuilder vehicleNumber(String num) {
        this.number = num;
        return this;
    }

    /**
     * Sets the vehicle type.
     *
     * @param type vehicle type
     * @return this builder
     */
    public VehicleBuilder vehicleType(String type) {
        this.vehicleType = type;
        return this;
    }

    /**
     * Sets the fuel type.
     *
     * @param type fuel type
     * @return this builder
     */
    public VehicleBuilder fuelType(String type) {
        this.fuelType = type;
        return this;
    }

    /**
     * Sets the fuel consumption rate.
     *
     * @param rate fuel consumption rate
     * @return this builder
     */
    public VehicleBuilder consumptionRate(double rate) {
        this.consumptionRate = rate;
        return this;
    }

    /**
     * Sets the route.
     *
     * @param route route
     * @return this builder
     */
    public VehicleBuilder route(String route) {
        this.route = route;
        return this;
    }

    /**
     * Sets the maximum capacity.
     *
     * @param max maximum capacity
     * @return this builder
     */
    public VehicleBuilder maxCapacity(int max) {
        this.maxCapacity = max;
        return this;
    }

    /**
     * Sets the vehicle ID.
     *
     * @param id vehicle ID
     * @return this builder
     */
    public VehicleBuilder vehicleId(int id) {
        this.vehicleId = id;
        return this;
    }

    /**
     * Builds and returns the {@link Vehicle}.
     *
     * @return a new vehicle instance
     */
    public Vehicle build() {
        return new Vehicle(number, vehicleType, fuelType, maxCapacity, consumptionRate, route, vehicleId);
    }
}
