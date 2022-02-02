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

    protected int arcadeDriveForwardAxis;
    public abstract int arcadeDriveForwardAxis();

    protected int arcadeDriveAngleAxis;
    public abstract int arcadeDriveAngleAxis();

    protected int tankDriveLeftAxis;
    public abstract int tankDriveLeftAxis();

    protected int tankDriveRightAxis;
    public abstract int tankDriveRightAxis();

    // ----------------------------------------------------------
    // Drivetrain buttons

    protected POVButton driveStraightPOVButton;
    public abstract POVButton driveStraightPOVButton(Joystick joystick);

    protected JoystickButton driveStraightJoystickButton;
    public abstract JoystickButton driveStraightJoystickButton(Joystick joystick);

    // ----------------------------------------------------------
    // Intake axes

    protected int rollerDisposalAxis;
    public abstract int rollerDisposalAxis();

    protected int rollerIntakeAxis;
    public abstract int rollerIntakeAxis();

    // ----------------------------------------------------------
    // Intake buttons

    protected JoystickButton toggleIntakeButton;
    public abstract JoystickButton toggleIntakeButton(Joystick joystick);

    protected JoystickButton runRollerDisposalButton;
    public abstract JoystickButton runRollerDisposalButton(Joystick joystick);

    protected JoystickButton runRollerIntakebutton;
    public abstract JoystickButton runRollerIntakebutton(Joystick joystick);

    // ----------------------------------------------------------
    // Manipulator buttons

    protected JoystickButton runIndexerButton;
    public abstract JoystickButton runIndexerButton(Joystick joystick);

    protected JoystickButton runLauncherButton;
    public abstract JoystickButton runLauncherButton(Joystick joystick);

    // ----------------------------------------------------------
    // Constructor

    public JoystickControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        arcadeDriveForwardAxis = arcadeDriveForwardAxis();
        arcadeDriveAngleAxis = arcadeDriveAngleAxis();
        tankDriveLeftAxis = tankDriveLeftAxis();
        tankDriveRightAxis = tankDriveRightAxis();
    }
}
