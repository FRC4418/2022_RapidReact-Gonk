package frc.robot.joystickcontrols;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public abstract class JoystickControls {
    // ----------------------------------------------------------
    // Drivetrain axes

    public abstract double getArcadeDriveForwardAxis();

    public abstract double getArcadeDriveAngleAxis();

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

    public abstract double reverseFeederAxis();

    public abstract double feederAxis();

    // ----------------------------------------------------------
    // Intake buttons

    protected JoystickButton toggleIntakeButton;
    public abstract JoystickButton toggleIntakeButton(Joystick joystick);

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

    // ----------------------------------------------------------
    // Constructor

    public JoystickControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        
    }
}
