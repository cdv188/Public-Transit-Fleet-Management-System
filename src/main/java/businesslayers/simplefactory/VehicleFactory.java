package businesslayers.simplefactory;

import businesslayers.builder.Vehicle;
import businesslayers.builder.VehicleBuilder;

/**
 * Simple factory for creating {@link Vehicle} instances.
 */
public class VehicleFactory {

    /**
     * Creates a vehicle with the specified parameters.
     */
    public static Vehicle createVehicle(String number, String vehicleType,
                                        String fuelType, int maxCapacity,
                                        double consumptionRate, String route, int vehicleId) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle number cannot be null or empty");
        }
        if (vehicleType == null || vehicleType.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle type cannot be null or empty");
        }

        VehicleBuilder builder = Vehicle.builder()
                .vehicleNumber(number.trim())
                .vehicleType(vehicleType.trim())
                .maxCapacity(maxCapacity)
                .consumptionRate(consumptionRate)
                .route(route != null ? route.trim() : "")
                .vehicleId(vehicleId);

        if (fuelType == null || fuelType.trim().isEmpty()) {
            fuelType = getDefaultFuelType(vehicleType);
        }
        builder.fuelType(fuelType.trim());

        return builder.build();
    }

    /**
     * Creates a vehicle with the specified parameters.
     */
    public static Vehicle createVehicle(String number, String vehicleType,
                                        String fuelType, int maxCapacity,
                                        double consumptionRate, String route) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle number cannot be null or empty");
        }
        if (vehicleType == null || vehicleType.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle type cannot be null or empty");
        }

        VehicleBuilder builder = Vehicle.builder()
                .vehicleNumber(number.trim())
                .vehicleType(vehicleType.trim())
                .maxCapacity(maxCapacity)
                .consumptionRate(consumptionRate)
                .route(route != null ? route.trim() : "");

        if (fuelType == null || fuelType.trim().isEmpty()) {
            fuelType = getDefaultFuelType(vehicleType);
        }
        builder.fuelType(fuelType.trim());

        return builder.build();
    }

    /**
     * Creates a vehicle with default parameters for the given type.
     */
    public static Vehicle createVehicle(String vehicleType) {
        return createVehicle(
            generateDefaultNumber(vehicleType),
            vehicleType,
            getDefaultFuelType(vehicleType),
            getDefaultCapacity(vehicleType),
            getDefaultConsumptionRate(vehicleType),
            getDefaultRoute(vehicleType)
        );
    }

    /** Returns the default fuel type for the given vehicle type. */
    private static String getDefaultFuelType(String vehicleType) {
        switch (vehicleType.toLowerCase()) {
            case "bus": return "Diesel";
            case "train": return "Electric";
            case "car": return "Gasoline";
            case "truck": return "Diesel";
            default: return "Unknown";
        }
    }

    /** Returns the default capacity for the given vehicle type. */
    private static int getDefaultCapacity(String vehicleType) {
        switch (vehicleType.toLowerCase()) {
            case "bus": return 50;
            case "train": return 250;
            case "car": return 5;
            case "truck": return 2;
            default: return 1;
        }
    }

    /** Returns the default fuel consumption rate for the given vehicle type. */
    private static double getDefaultConsumptionRate(String vehicleType) {
        switch (vehicleType.toLowerCase()) {
            case "bus": return 8.5;
            case "train": return 15.0;
            case "car": return 7.2;
            case "truck": return 12.0;
            default: return 10.0;
        }
    }

    /** Returns the default route for the given vehicle type. */
    private static String getDefaultRoute(String vehicleType) {
        switch (vehicleType.toLowerCase()) {
            case "bus": return "88 Hurdman";
            case "train": return "88 Bayshore";
            case "car": return "68 Baseline";
            case "truck": return "61 Stitsville";
            default: return "Unassigned";
        }
    }

    /** Generates a default vehicle number based on the type. */
    private static String generateDefaultNumber(String vehicleType) {
        String prefix = vehicleType.substring(0, Math.min(3, vehicleType.length())).toUpperCase();
        long timestamp = System.currentTimeMillis() % 10000;
        return prefix + "-" + timestamp;
    }

    /** Checks if the given vehicle type is supported. */
    public static boolean isValidVehicleType(String vehicleType) {
        if (vehicleType == null) return false;
        String[] validTypes = {"bus", "train", "car", "truck"};
        String lowerType = vehicleType.toLowerCase();
        for (String validType : validTypes) {
            if (validType.equals(lowerType)) {
                return true;
            }
        }
        return false;
    }
}
