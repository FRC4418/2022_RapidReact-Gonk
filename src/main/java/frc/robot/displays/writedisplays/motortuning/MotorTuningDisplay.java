package frc.robot.displays.writedisplays.motortuning;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.WriteDisplay;


public abstract class MotorTuningDisplay extends WriteDisplay {
    protected static final ShuffleboardTab tab = Shuffleboard.getTab("Motor Tuning");

    public MotorTuningDisplay(int width, int height) {
        super(width, height);
    }
}