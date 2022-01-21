/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;


public class DriveStraightForDistance extends CommandBase {
	private final double kP = 0.1;

	private double distanceInMeters;
	private Drivetrain dt;

	public DriveStraightForDistance(double distanceInMeters) {
		this.distanceInMeters = distanceInMeters;
		dt = RobotContainer.drivetrain;
		addRequirements(dt);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		dt.coastOrBrakeMotors(false, false);
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		double error = dt.getLeftDistance() - dt.getRightDistance();
		dt.tankDrive(0.5d + kP * error, 0.5d - kP * error);
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		dt.stopDrive();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return dt.getAverageDistance() >= distanceInMeters;
	}
}