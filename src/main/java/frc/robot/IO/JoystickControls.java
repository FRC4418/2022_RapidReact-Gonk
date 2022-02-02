package frc.robot.IO;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.RunIndexer;
import frc.robot.commands.RunLauncher;
import frc.robot.commands.RunRollerDisposal;
import frc.robot.commands.RunRollerIntake;
import frc.robot.commands.ToggleIntake;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public abstract class JoystickControls {
    // ----------------------------------------------------------
    // Drivetrain axes

    private int arcadeDriveForwardAxis;
    public abstract int arcadeDriveForwardAxis();

    private int arcadeDriveAngleAxis;
    public abstract int arcadeDriveAngleAxis();

    private int tankDriveLeftAxis;
    public abstract int tankDriveLeftAxis();

    private int tankDriveRightAxis;
    public abstract int tankDriveRightAxis();

    // ----------------------------------------------------------
    // Drivetrain buttons

    private POVButton driveStraightPOVButton;
    public abstract POVButton driveStraightPOVButton(Joystick joystick);

    private JoystickButton driveStraightJoystickButton;
    public abstract JoystickButton driveStraightJoystickButton(Joystick joystick);

    // ----------------------------------------------------------
    // Intake axes

    private int rollerDisposalAxis;
    public abstract int rollerDisposalAxis();

    private int rollerIntakeAxis;
    public abstract int rollerIntakeAxis();

    // ----------------------------------------------------------
    // Intake buttons

    private JoystickButton toggleIntakeButton;
    public abstract JoystickButton toggleIntakeButton(Joystick joystick);

    private JoystickButton runRollerDisposalButton;
    public abstract JoystickButton runRollerDisposalButton(Joystick joystick);

    private JoystickButton runRollerIntakebutton;
    public abstract JoystickButton runRollerIntakebutton(Joystick joystick);

    // ----------------------------------------------------------
    // Manipulator buttons

    private JoystickButton runIndexerButton;
    public abstract JoystickButton runIndexerButton(Joystick joystick);

    private JoystickButton runLauncherButton;
    public abstract JoystickButton runLauncherButton(Joystick joystick);

    // ----------------------------------------------------------
    // Constructor

    public JoystickControls(Joystick primaryJoystick, Joystick secondaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        arcadeDriveForwardAxis = arcadeDriveForwardAxis();
        arcadeDriveAngleAxis = arcadeDriveAngleAxis();
        tankDriveLeftAxis = tankDriveLeftAxis();
        tankDriveRightAxis = tankDriveRightAxis();

        driveStraightPOVButton = driveStraightPOVButton(primaryJoystick);
        if (driveStraightPOVButton != null) driveStraightPOVButton.whenHeld(new DriveStraightWhileHeld(drivetrain));
        driveStraightJoystickButton = driveStraightJoystickButton(primaryJoystick);
        if (driveStraightJoystickButton != null) driveStraightJoystickButton.whenHeld(new DriveStraightWhileHeld(drivetrain));

        runRollerDisposalButton = runRollerDisposalButton(primaryJoystick);
        if (runRollerDisposalButton != null) runRollerDisposalButton.whenHeld(new RunRollerDisposal(intake));
        runRollerIntakebutton = runRollerIntakebutton(primaryJoystick);
        if (runRollerIntakebutton != null) runRollerIntakebutton.whenHeld(new RunRollerIntake(intake));
        toggleIntakeButton = toggleIntakeButton(primaryJoystick);
        if (toggleIntakeButton != null) toggleIntakeButton.toggleWhenPressed(new ToggleIntake(intake));

        runIndexerButton = runIndexerButton(primaryJoystick);
        if (runIndexerButton != null) runIndexerButton.whenHeld(new RunIndexer(manipulator));
        runLauncherButton = runLauncherButton(primaryJoystick);
        if (runLauncherButton != null) runLauncherButton.whenHeld(new RunLauncher(manipulator));
    }
}
