package frc.robot.displays.writedisplays.autonomous;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.WriteDisplay;


public abstract class AutonomousDisplay extends WriteDisplay {
    protected static final ShuffleboardTab tab = Shuffleboard.getTab("Autonomous");

    public AutonomousDisplay(int width, int height) {
        super(width, height);
    }
}