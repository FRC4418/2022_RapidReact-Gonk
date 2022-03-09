package frc.robot.displays.writedisplays.drivetrain;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.WriteDisplay;


public abstract class DrivingDisplay extends WriteDisplay {
    protected static final ShuffleboardTab tab = Shuffleboard.getTab("Driving");

    public DrivingDisplay(int width, int height) {
        super(width, height);
    }
}