package frc.robot.displays.autonomousdisplays;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.Display;


public abstract class AutonomousDisplay extends Display {
    protected static final ShuffleboardTab tab = Shuffleboard.getTab("Autonomous");

    public AutonomousDisplay(int width, int height) {
        super(width, height);
    }
}