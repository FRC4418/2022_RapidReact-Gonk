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


    public abstract int getPrimaryJoystickPort();
    
    public abstract int getSecondaryJoystickPort();

    // ----------------------------------------------------------
    // Drivetrain axes

    public abstract double getCurvatureForwardAxis();

    public abstract double getCurvatureRotationAxis();
    

    public abstract double getArcadeForwardAxis();

    public abstract double getArcadeTurnAxis();

    
    public abstract double getTankLeftAxis();

    public abstract double getTankRightAxis();

    // ----------------------------------------------------------
    // Drivetrain buttons

    protected JoystickButton reverseDrivetrainButton;
    protected abstract JoystickButton reverseDrivetrainButton(Joystick joystick);

    protected JoystickButton driveStraightButton;
    protected abstract JoystickButton driveStraightButton(Joystick joystick);

    // ----------------------------------------------------------
    // Intake axes

    public abstract double getReverseFeederAxis();

    public abstract double getFeederAxis();

    // ----------------------------------------------------------
    // Intake buttons

    protected JoystickButton toggleFeederButton;
    protected abstract JoystickButton toggleFeederButton(Joystick joystick);

    protected JoystickButton runFeederDisposalButton;
    protected abstract JoystickButton runReverseFeederButton(Joystick joystick);

    protected JoystickButton runFeederIntakebutton;
    protected abstract JoystickButton runFeederButton(Joystick joystick);

    protected JoystickButton extendIntakeArmButton;
    protected abstract JoystickButton extendIntakeArmButton(Joystick joystick);

    // ----------------------------------------------------------
    // Manipulator buttons

    protected JoystickButton runIndexerButton;
    protected abstract JoystickButton runIndexerButton(Joystick joystick);

    protected JoystickButton runLauncherButton;
    protected abstract JoystickButton runLauncherButton(Joystick joystick);

    // ----------------------------------------------------------
    // Climber buttons

    protected JoystickButton releaseClimberPinButton;
    protected abstract JoystickButton releaseClimberPinButton(Joystick joystick);

    protected POVButton extendClimberButton;
    protected abstract POVButton extendClimberButton(Joystick joystick);

    protected POVButton lowerClimberButton;
    protected abstract POVButton lowerClimberButton(Joystick joystick);

    // ----------------------------------------------------------
    // Constructor

    public JoystickControls() {
        DEADBAND = deadband();
    }
}
