package frc.robot.IO;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import frc.robot.Constants.X3D;
import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.RunLauncher;
import frc.robot.commands.ToggleIntake;


// TODO: Different set of driver controls for tank mode
public class DriverControls {
    // ----------------------------------------------------------
    // Resources

    private Joystick joystick1;
    private Joystick joystick2;

    private JoystickButton
        driveStraightButton,
        
        toggleIntakeButton,
        runLaunchButton;

    // ----------------------------------------------------------
    // Methods

    // if controller2 is null, then configure button bindings for the driver's single controller
    public DriverControls configureButtonBindings(Joystick joystick1, Joystick joystick2) {
        this.joystick1 = joystick1;
        this.joystick2 = joystick2;

        if (joystick1 == null || joystick2 == null) {
            configureLoneControlsFor(joystick1 == null ? joystick2: joystick1);
        } else {
            configureDualControlsFor();
        }
        
        return this;
    }

    private DriverControls configureLoneControlsFor(Joystick joystick) {
        driveStraightButton = new JoystickButton(joystick, X3D.GRIP_BUTTON_ID);
        driveStraightButton.whenHeld(new DriveStraightWhileHeld(drivetrain));
        

        toggleIntakeButton = new JoystickButton(joystick, X3D.BUTTON_3_ID);
        toggleIntakeButton.toggleWhenPressed(new ToggleIntake(intake));
        
        runLaunchButton = new JoystickButton(joystick, X3D.TRIGGER_BUTTON_ID);
        runLaunchButton.whenHeld(new RunLauncher(manipulator));
    }

    private DriverControls configureDualControlsFor() {
        throw new Exception("Dual controller driver controls not configured");

        return this;
    }

    // Tank drive axes

    @SuppressWarnings("unused")
    private double getLeftTankDriveAxis() {
        return X3D_LEFT.getRawAxis(X3D.PITCH_AXIS);
    }

    @SuppressWarnings("unused")
    private double getRightTankDriveAxis() {
        return X3D_RIGHT.getRawAxis(X3D.PITCH_AXIS);
    }

    // Arcade drive axes

    @SuppressWarnings("unused")
    private double getForwardArcadeDriveAxis() {
        return X3D_LEFT.getRawAxis(X3D.PITCH_AXIS);
    }

    @SuppressWarnings("unused")
    private double getAngleArcadeDriveAxis() {
        return X3D_LEFT.getRawAxis(X3D.ROLL_AXIS);
    }
}


