/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;


public class DriveStraightCommand extends CommandBase {
	private int counter;
	private final int counterMax = 150;	// TODO: Make drive-straight timer use seconds

	public DriveStraightCommand() {
		addRequirements(RobotContainer.driveSubsystem);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		counter = 0;
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		RobotContainer.driveSubsystem.brakeOrCoastMotors(false, false);
		RobotContainer.driveSubsystem.setLeftMotors(0.5);
		RobotContainer.driveSubsystem.setRightMotors(-0.5);
		counter++;
		SmartDashboard.putNumber("AUTO PRINT COUNTER", counter);
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		RobotContainer.driveSubsystem.stopDrive();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return counter >= counterMax;
	}
}
