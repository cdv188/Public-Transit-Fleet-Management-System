package businesslayers.builder;

/**
 * Represents a vehicle with its properties and details.
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
     * Constructs a Vehicle with specified parameters.
     */
    Vehicle(String number, String vehicleType, String fuelType, int maxCapacity, double consumptionRate, String route, int vehicleId){
        this.consumptionRate = consumptionRate;
        this.number = number;
        this.maxCapacity = maxCapacity;
        this.fuelType = fuelType;
        this.vehicleType = vehicleType;
        this.route = route;
        this.vehicleId = vehicleId;
    }
    /**
     * Default constructor.
     */
    public Vehicle() {
    }
    /**
     * Returns formatted vehicle details as a string.
     */
    public String getVehicleDetails(){
        return String.format("Number: %s\nType: %s\nFuel Type: %s\nConsumption Rate: %.2f"
                        + "\nMax Capacity: %d\nRoute: %s", getNumber(), getVehicleType(), getFuelType(), getConsumptionRate(), getMaxCapacity(), getRoute());
    }
    /**
     * Creates and returns a new VehicleBuilder instance.
     */
    public static VehicleBuilder builder(){
        return new VehicleBuilder();
    }

    /**
     * @return the vehicleType
     */
    public String getVehicleType() {
        return vehicleType;
    }

    /**
     * @return the fuelType
     */
    public String getFuelType() {
        return fuelType;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the consumptionRate
     */
    public double getConsumptionRate() {
        return consumptionRate;
    }

    /**
     * @return the maxCapacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * @return the route
     */
    public String getRoute() {
        return route;
    }
    /**
     * @return the vehicleId
     */
    public int getVehicleId(){
        return vehicleId;
    }
    
    
}
