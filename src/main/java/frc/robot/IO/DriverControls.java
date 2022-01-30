package frc.robot.IO;


import edu.wpi.first.wpilibj.buttons.JoystickButton;

import frc.robot.Constants.X3D;
import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.RunLauncher;
import frc.robot.commands.ToggleIntake;


// TODO: Different set of driver controls for tank mode
public class DriverControls {
    // ----------------------------------------------------------
    // Resources

    private JoystickButton
        driveStraightButton = new JoystickButton(X3D_LEFT, X3D.GRIP_BUTTON_ID),
        
        toggleIntakeButton = new JoystickButton(X3D_LEFT, X3D.BUTTON_3_ID),
        runLaunchButton = new JoystickButton(X3D_LEFT, X3D.TRIGGER_BUTTON_ID);

    // ----------------------------------------------------------
    // Methods

    public DriverControls configButtonBindings() {
        driveStraightButton.whenHeld(new DriveStraightWhileHeld(drivetrain));
        
        toggleIntakeButton.toggleWhenPressed(new ToggleIntake(intake));
        runLaunchButton.whenHeld(new RunLauncher(manipulator));
        
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


