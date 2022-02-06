package frc.robot.joystickcontrols;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;


public abstract class JoystickControls {
    // ----------------------------------------------------------
    // Drivetrain axes

    public abstract double getArcadeDriveForwardAxis();

    public abstract double getArcadeDriveTurnAxis();

    
    public abstract double getTankDriveLeftAxis();

    public abstract double getTankDriveRightAxis();

    // ----------------------------------------------------------
    // Drivetrain buttons

    protected POVButton driveStraightPOVButton;
    public abstract POVButton driveStraightPOVButton(Joystick joystick);

    protected JoystickButton driveStraightJoystickButton;
    public abstract JoystickButton driveStraightJoystickButton(Joystick joystick);

    // ----------------------------------------------------------
    // Intake axes

    public abstract double getReverseFeederAxis();

    public abstract double getFeederAxis();

    // ----------------------------------------------------------
    // Intake buttons

    protected JoystickButton toggleFeederButton;
    public abstract JoystickButton toggleFeederButton(Joystick joystick);

    protected JoystickButton runFeederDisposalButton;
    public abstract JoystickButton runFeederDisposalButton(Joystick joystick);

    protected JoystickButton runFeederIntakebutton;
    public abstract JoystickButton runFeederButton(Joystick joystick);

    // ----------------------------------------------------------
    // Manipulator buttons

    protected JoystickButton runIndexerButton;
    public abstract JoystickButton runIndexerButton(Joystick joystick);

    protected JoystickButton runLauncherButton;
    public abstract JoystickButton runLauncherButton(Joystick joystick);
}
