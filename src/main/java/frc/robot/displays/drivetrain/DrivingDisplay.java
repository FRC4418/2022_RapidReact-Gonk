package frc.robot.displays.drivetrain;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.Display;


public abstract class DrivingDisplay extends Display {
    protected static final ShuffleboardTab tab = Shuffleboard.getTab("Driving");

    public DrivingDisplay(int width, int height) {
        super(width, height);
    }
}