package frc.robot;


public class Gains {
    public double
        kP,
        kI,
        kD,
        kF;

    public int
        Iz;

    public double
        peakout;

    public Gains(double kP, double kI, double kD, double kF, int Iz, double peakout) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.Iz = Iz;
        this.peakout = peakout;
    }
}
