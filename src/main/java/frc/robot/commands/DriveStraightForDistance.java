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
import frc.robot.subsystems.Drivetrain;


public class DriveStraightForDistance extends CommandBase {
	public enum DriveStraightDirection {
		FORWARDS,
		BACKWARDS
	}

	private final double motorPercentOutput = 0.5d;
	private final double kP = 0.1;

	private double distanceInInches;
	private DriveStraightDirection direction;
	private Drivetrain dt;

	public DriveStraightForDistance(double distanceInInches, DriveStraightDirection direction) {
		this.distanceInInches = distanceInInches;
		this.direction = direction;
		dt = RobotContainer.drivetrain;
		addRequirements(dt);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		dt.coastOrBrakeMotors(false, false);
		dt.resetEncoders();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		double error = dt.getLeftDistance() - dt.getRightDistance();

		if (direction == DriveStraightDirection.FORWARDS) {
			// dt.tankDrive(motorPercentOutput + kP * error, motorPercentOutput - kP * error);
			dt.tankDrive(motorPercentOutput, motorPercentOutput);
		} else {
			// dt.tankDrive(-(motorPercentOutput + kP * error), -(motorPercentOutput - kP * error));
			dt.tankDrive(-motorPercentOutput, -motorPercentOutput);
		}

		SmartDashboard.putNumber("Left Encoder", dt.getLeftDistance());
		SmartDashboard.putNumber("Right Encoder", dt.getRightDistance());
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		dt.stopDrive();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return dt.getAverageDistance() >= distanceInInches;
	}
}
