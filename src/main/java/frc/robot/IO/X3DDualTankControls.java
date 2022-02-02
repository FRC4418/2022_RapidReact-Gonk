package frc.robot.IO;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.IO.Constants.X3D;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class X3DDualTankControls extends DualJoystickControls {
    // ----------------------------------------------------------
    // Drivetrain axes

    @Override
    public int arcadeDriveForwardAxis() {
        return -1;
    }

    @Override
    public int arcadeDriveAngleAxis() {
        return -1;
    }

    @Override
    public int tankDriveLeftAxis() {
        return X3D.PITCH_AXIS;
    }

    @Override
    public int tankDriveRightAxis() {
        return X3D.PITCH_AXIS;
    }

    // ----------------------------------------------------------
    // Drivetrain buttons

    @Override
    public JoystickButton driveStraightJoystickButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.GRIP_BUTTON_ID);
    }

    @Override
    public POVButton driveStraightPOVButton(Joystick joystick) {
        return null;
    }

    // ----------------------------------------------------------
    // Intake axes

    @Override
    public int rollerDisposalAxis() {
        return -1;
    }

    @Override
    public int rollerIntakeAxis() {
        return -1;
    }

    // ----------------------------------------------------------
    // Intake buttons

    @Override
    public JoystickButton runRollerDisposalButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_11_ID);
    }

    @Override
    public JoystickButton runRollerIntakebutton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_12_ID);
    }

    @Override
    public JoystickButton toggleIntakeButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_4_ID);
    }

    // ----------------------------------------------------------
    // Manipulator buttons

    @Override
    public JoystickButton runIndexerButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_3_ID);
    }

    @Override
    public JoystickButton runLauncherButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.TRIGGER_BUTTON_ID);
    }

    // ----------------------------------------------------------
    // Constructor

    public X3DDualTankControls(Joystick primaryJoystick, Joystick secondaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        super(primaryJoystick, secondaryJoystick, drivetrain, intake, manipulator);
    }
}
