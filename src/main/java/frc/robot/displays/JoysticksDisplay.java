package frc.robot.displays;


import java.util.Map;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.RobotContainer.JoystickModes;


public class JoysticksDisplay {
    // ----------------------------------------------------------
    // Resources

    private SendableChooser<JoystickModes> sendableDriverJoystickModeChooser = new SendableChooser<>();
    private SendableChooser<Joystick> sendableDriverJoystickDeviceChooser = new SendableChooser<>();

    private SendableChooser<JoystickModes> sendableSpotterJoystickModeChooser = new SendableChooser<>();
    private SendableChooser<Joystick> sendableSpotterJoystickDeviceChooser = new SendableChooser<>();

    // ----------------------------------------------------------
    // Constructor

    // TODO: Make the drive mode choosers actually flip the IO controls class' joystick modes
    
    // TODO: Make the joystick device choosers for the JoysticksDisplay

    public JoysticksDisplay(ShuffleboardTab HUDTab, int column, int row) {
        var joysticksLayout = HUDTab
			.getLayout("Joysticks", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(3, 2);

            var driverLayout = joysticksLayout
                .getLayout("Driver", BuiltInLayouts.kGrid)
                .withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"));

                sendableDriverJoystickModeChooser.setDefaultOption("Arcade", JoystickModes.ARCADE);
                sendableDriverJoystickModeChooser.addOption("Tank", JoystickModes.LONE_TANK);
                driverLayout
                    .add("Mode", sendableDriverJoystickModeChooser)
                    .withWidget(BuiltInWidgets.kComboBoxChooser);
                // sendableDriverJoystickDeviceChooser.setDefaultOption("ex. Xbox1", SOME_JOYSTICK);
                // sendableDriverJoystickDeviceChooser.addOption("Tank", SOME_JOYSTICK);
                // driverLayout
                // 	.add("Devices",sendableDriverJoystickDeviceChooser)
                // 	.withWidget(BuiltInWidgets.kComboBoxChooser);

            var spotterLayout = joysticksLayout
                .getLayout("Spotter", BuiltInLayouts.kGrid)
                .withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"));

                sendableSpotterJoystickModeChooser.setDefaultOption("Arcade", JoystickModes.ARCADE);
                sendableSpotterJoystickModeChooser.addOption("Tank", JoystickModes.LONE_TANK);
                spotterLayout
                    .add("Mode", sendableSpotterJoystickModeChooser)
                    .withWidget(BuiltInWidgets.kComboBoxChooser);
                // sendableSpotterJoystickDeviceChooser.setDefaultOption("ex. Xbox1", SOME_JOYSTICK);
                // sendableSpotterJoystickDeviceChooser.addOption("Tank", SOME_JOYSTICK);
                // spotterLayout
                // 	.add("Devices",sendableSpotterJoystickDeviceChooser)
                // 	.withWidget(BuiltInWidgets.kComboBoxChooser);
    }
}
