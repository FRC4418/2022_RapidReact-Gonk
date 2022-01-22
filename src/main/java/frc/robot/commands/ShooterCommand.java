/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;


// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Manipulator;


public class ShooterCommand extends CommandBase {
	// shorthand reference to RobotContainer's ManipulatorSubsystem singleton
	private Manipulator ms;

	// private final double rpmToVelocity = -Constants.Manipulator.UNITS_PER_REVOLUTION/600;

	public ShooterCommand() {
		ms = RobotContainer.manipulator;
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {

	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		// ms.setHighGoalShooterMotor(1.0);

		// if (ms.inTuningMode) {
		// 	ms.setHighGoalShooterMotor(
		// 		ms.highShooterOnOffTextField.getDouble(Constants.Manipulator.HIGH_GOAL_SHOOTER_TARGET_PERCENTAGE)
		// 	);
		// } else {
		// 	ms.setHighGoalShooterMotor(Constants.Manipulator.HIGH_GOAL_SHOOTER_TARGET_PERCENTAGE);
		// }
		
		// SmartDashboard.putNumber("Wrist Motor Velocity", Math.abs(RobotContainer.manipulatorsubsystem.highShooterMotor.getSelectedSensorVelocity(0)));

		// SmartDashboard.putNumber("Elbow Motor Velocity", Math.abs(RobotContainer.manipulatorsubsystem.lowShooterMotor.getSelectedSensorVelocity(0)));
		//need to wait until motors are up to speed
		// double currentSpeed = Math.abs(RobotContainer.manipulatorsubsystem.wristFireMotor.getSelectedSensorVelocity(0)) * (600/unitsPerRev);
		// if ((wristTargetSpeed - currentSpeed) <= toleranceSlider.getDouble(0))  {
		// 	RobotContainer.manipulatorsubsystem.setShoulderFireMotor(-0.5);	
		// }
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		// ms.setHighGoalShooterMotor(0.0);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
