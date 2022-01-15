/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ManipulatorSubsystem;


public class IntakeCommand extends CommandBase {
	private boolean isReverseIntake;

	// shorthand reference to RobotContainer's ManipulatorSubsystem singleton
	private ManipulatorSubsystem ms;

	public IntakeCommand(boolean isReverseIntake) {
		this.isReverseIntake = isReverseIntake;
		ms = RobotContainer.manipulatorsubsystem;
	}

	public int getIntakeDirection(boolean isReverseIntake) {
		return isReverseIntake ? -1: 1;
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		// TODO: Convert intake motor to velocity and put it on display
		// SmartDashboard.putNumber("Intake Motor", RobotContainer.manipulatorsubsystem.getIntakeMotor());

		if (ms.inTuningMode) {
			ms.setIntakeMotor(getIntakeDirection(isReverseIntake) * ms.intakePercentOutputTextField.getDouble(Constants.Manipulator.INTAKE_PERCENT_OUTPUT));
		} else {
			ms.setIntakeMotor(getIntakeDirection(isReverseIntake) * Constants.Manipulator.INTAKE_PERCENT_OUTPUT);
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		RobotContainer.manipulatorsubsystem.setIntakeMotor(0.0);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
