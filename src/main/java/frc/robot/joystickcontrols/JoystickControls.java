package frc.robot.joystickcontrols;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;


public abstract class JoystickControls {
    // ----------------------------------------------------------
    // Joystick helpers

    protected double DEADBAND;
    protected abstract double deadband();

    public abstract boolean isActivelyDriving();

    // ----------------------------------------------------------
    // Drivetrain axes

    public abstract double getArcadeDriveForwardAxis();

    public abstract double getArcadeDriveTurnAxis();

    
    public abstract double getTankDriveLeftAxis();

    public abstract double getTankDriveRightAxis();

    // ----------------------------------------------------------
    // Drivetrain buttons

    protected JoystickButton reverseDrivetrainButton;
    protected abstract JoystickButton reverseDrivetrainButton(Joystick joystick);

    protected POVButton driveStraightPOVButton;
    protected abstract POVButton driveStraightPOVButton(Joystick joystick);

    protected JoystickButton driveStraightJoystickButton;
    protected abstract JoystickButton driveStraightJoystickButton(Joystick joystick);

    // ----------------------------------------------------------
    // Intake axes

    public abstract double getReverseFeederAxis();

    public abstract double getFeederAxis();

    // ----------------------------------------------------------
    // Intake buttons

    protected JoystickButton toggleFeederButton;
    protected abstract JoystickButton toggleFeederButton(Joystick joystick);

    protected JoystickButton runFeederDisposalButton;
    protected abstract JoystickButton runFeederDisposalButton(Joystick joystick);

    protected JoystickButton runFeederIntakebutton;
    protected abstract JoystickButton runFeederButton(Joystick joystick);

    // ----------------------------------------------------------
    // Manipulator buttons

    protected JoystickButton runIndexerButton;
    protected abstract JoystickButton runIndexerButton(Joystick joystick);

    protected JoystickButton runLauncherButton;
    protected abstract JoystickButton runLauncherButton(Joystick joystick);

    // ----------------------------------------------------------
    // Constructor

    public JoystickControls() {
        DEADBAND = deadband();
    }
}
