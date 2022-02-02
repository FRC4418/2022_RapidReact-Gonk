package frc.robot.joystickcontrols;


import edu.wpi.first.wpilibj.Joystick;

import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.RunIndexer;
import frc.robot.commands.RunLauncher;
import frc.robot.commands.RunReverseFeeder;
import frc.robot.commands.RunFeeder;
import frc.robot.commands.ToggleFeeder;
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

        runFeederDisposalButton = runFeederDisposalButton(primaryJoystick);
        if (runFeederDisposalButton != null) runFeederDisposalButton.whenHeld(new RunReverseFeeder(intake));
        runFeederIntakebutton = runFeederButton(primaryJoystick);
        if (runFeederIntakebutton != null) runFeederIntakebutton.whenHeld(new RunFeeder(intake));
        toggleIntakeButton = toggleIntakeButton(primaryJoystick);
        if (toggleIntakeButton != null) toggleIntakeButton.toggleWhenPressed(new ToggleFeeder(intake));

        runIndexerButton = runIndexerButton(primaryJoystick);
        if (runIndexerButton != null) runIndexerButton.whenHeld(new RunIndexer(manipulator));
        runLauncherButton = runLauncherButton(primaryJoystick);
        if (runLauncherButton != null) runLauncherButton.whenHeld(new RunLauncher(manipulator));
    }
}
