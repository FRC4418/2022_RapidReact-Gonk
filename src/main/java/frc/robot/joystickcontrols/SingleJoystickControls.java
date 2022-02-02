package frc.robot.joystickcontrols;


import edu.wpi.first.wpilibj.Joystick;

import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.RunIndexer;
import frc.robot.commands.RunLauncher;
import frc.robot.commands.RunRollerDisposal;
import frc.robot.commands.RunRollerIntake;
import frc.robot.commands.ToggleIntake;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public abstract class SingleJoystickControls extends JoystickControls {
    // ----------------------------------------------------------
    // Constructor

    public SingleJoystickControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        super(primaryJoystick, drivetrain, intake, manipulator);

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
