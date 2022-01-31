package frc.robot.displays;


import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.RobotContainer.JoystickMode;


public class JoysticksDisplay {
    // ----------------------------------------------------------
    // Resources

    public SendableChooser<JoystickMode> driverJoystickModeChooser = new SendableChooser<>();

    private SendableChooser<JoystickMode> spotterJoystickModeChooser = new SendableChooser<>();

    // ----------------------------------------------------------
    // Constructor

    // TODO: Make the drive mode choosers actually flip the IO controls class' joystick modes
    
    public JoysticksDisplay(ShuffleboardTab HUDTab, int column, int row) {        
        var joysticksLayout = HUDTab
			.getLayout("Joysticks", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(3, 2);

            var driverLayout = joysticksLayout
                .getLayout("Driver", BuiltInLayouts.kGrid)
                .withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"));

                driverJoystickModeChooser.setDefaultOption("Arcade", JoystickMode.ARCADE);
                driverJoystickModeChooser.addOption("Lone Tank", JoystickMode.LONE_TANK);
                driverJoystickModeChooser.addOption("Dual Tank", JoystickMode.DUAL_TANK);
                driverLayout
                    .add("Mode", driverJoystickModeChooser)
                    .withWidget(BuiltInWidgets.kComboBoxChooser);

            var spotterLayout = joysticksLayout
                .getLayout("Spotter", BuiltInLayouts.kGrid)
                .withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"));

                spotterJoystickModeChooser.setDefaultOption("Arcade", JoystickMode.ARCADE);
                spotterJoystickModeChooser.addOption("Lone Tank", JoystickMode.LONE_TANK);
                driverJoystickModeChooser.addOption("Dual Tank", JoystickMode.DUAL_TANK);
                spotterLayout
                    .add("Mode", spotterJoystickModeChooser)
                    .withWidget(BuiltInWidgets.kComboBoxChooser);
    }
}