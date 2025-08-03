package vehicle;

/**
 *
 * @author Chester
 */
public class Vehicle {
    private String vehicleType;
    private String fuelType;
    private String number;
    private double consumptionRate;
    private int maxCapacity;
    private String route;
    
    Vehicle(String number, String vehicleType, String fuelType, int maxCapacity, double consumptionRate, String route){
        this.consumptionRate = consumptionRate;
        this.number = number;
        this.maxCapacity = maxCapacity;
        this.fuelType = fuelType;
        this.vehicleType = vehicleType;
        this.route = route;
    }
    
    public String getVehicleDetails(){
        return String.format("Number: %s\nType: %s\nFuel Type: %s\nConsumption Rate: %.2f"
                        + "\nMax Capacity: %d\nRoute: %s",
                        number, vehicleType, fuelType,
                        consumptionRate, maxCapacity,route);
    }
    
    public static VehicleBuilder builder(){
        return new VehicleBuilder();
    }
}
