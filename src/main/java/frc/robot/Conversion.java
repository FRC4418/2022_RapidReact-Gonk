package frc.robot;


public class Conversion {
    private static final double kMetersToinches = 39.37d;

    public static double inchesToMeters(double inches) {
        return inches / kMetersToinches;
    }

    public static double metersToInches(double meters) {
        return meters * kMetersToinches;
    }
}