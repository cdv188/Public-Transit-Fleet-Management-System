package businesslayers.builder;

/**
 * Builder class for creating Vehicle objects.
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
     * Sets the vehicle number and returns the builder.
     */
    public VehicleBuilder vehicleNumber(String num){
        this.number = num;
        return this;
    }
    /**
     * Sets the vehicle type and returns the builder.
     */
    public VehicleBuilder vehicleType(String type){
        this.vehicleType = type;
        return this;
    }
    /**
     * Sets the fuel type and returns the builder.
     */
    public VehicleBuilder fuelType(String type){
        this.fuelType = type;
        return this;
    }
    /**
     * Sets the consumption rate and returns the builder.
     */
    public VehicleBuilder consumptionRate(double rate){
        this.consumptionRate = rate;
        return this;
    }
    /**
     * Sets the route and returns the builder.
     */
    public VehicleBuilder route(String route){
        this.route = route;
        return this;
    }
    /**
     * Sets the max capacity and returns the builder.
     */
    public VehicleBuilder maxCapacity(int max){
        this.maxCapacity = max;
        return this;
    }
    /**
     * Sets the vehicle ID and returns the builder.
     */
    public VehicleBuilder vehicleId (int id){
        this.vehicleId = id;
        return this;
    }
    /**
     * Builds and returns the Vehicle object.
     */
    public Vehicle build(){
        return new Vehicle(number, vehicleType, fuelType, maxCapacity, consumptionRate, route, vehicleId);
    }
}
