package businesslayers.builder;

/**
 *
 * @author Chester
 */
public class VehicleBuilder {
    private String vehicleType;
    private String fuelType;
    private String number;
    private double consumptionRate;
    private int maxCapacity;
    private String route;
    private int vehicleId;
    
    public VehicleBuilder vehicleNumber(String num){
        this.number = num;
        return this;
    }
    
    public VehicleBuilder vehicleType(String type){
        this.vehicleType = type;
        return this;
    }
    
    public VehicleBuilder fuelType(String type){
        this.fuelType = type;
        return this;
    }
    
    public VehicleBuilder consumptionRate(double rate){
        this.consumptionRate = rate;
        return this;
    }
    
    public VehicleBuilder route(String route){
        this.route = route;
        return this;
    }
    
    public VehicleBuilder maxCapacity(int max){
        this.maxCapacity = max;
        return this;
    }
    
    public VehicleBuilder vehicleId (int id){
        this.vehicleId = id;
        return this;
    }
    
    public Vehicle build(){
        return new Vehicle(number, vehicleType, fuelType, maxCapacity, consumptionRate, route, vehicleId);
    }
}
