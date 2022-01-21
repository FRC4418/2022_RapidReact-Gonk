/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
//import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.Robot;


public class Manipulator extends SubsystemBase {
	public boolean inTuningMode;
	public NetworkTableEntry tuningModeBooleanBox;

	// private WPI_TalonSRX outerIntakeMotor;
	// private WPI_TalonSRX innerIntakeMotor;
	public NetworkTableEntry intakePercentOutputTextField;

	// public WPI_TalonSRX lowShooterMotor;
	// public NetworkTableEntry lowShooterRPMTextField;
	public WPI_TalonSRX highGoalShooterMotor;

	public NetworkTableEntry highShooterPercentageTextField;

	/* Encoder.getRate() returns distance per second
	distance per second * distance per pulse = pulse per second
	pulse per second * decoding factor = degrees per second
	degrees per second / 360 degrees = revolutions per second
	revolutions per second * 60 seconds = revolutions per minute (RPM) */
	// private static double distPerSecToRPM = 
	//   Constants.DRIVE_ENCODER_DISTANCE_PER_PULSE
	//   * (double) Constants.DRIVE_ENCODER_DECODING_SCALE_FACTOR 
	//   / 60.0;

	private AnalogPotentiometer pivotPotentiometer;

	private boolean pivotUp = true;

	public Manipulator() {
		highGoalShooterMotor = new WPI_TalonSRX(Constants.Manipulator.HIGH_GOAL_SHOOTER_775_ID);

		// wristFireMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        highGoalShooterMotor.setSensorPhase(false);
        /* set closed loop gains in slot0 */
        highGoalShooterMotor.config_kF(0, 0.1097);
        highGoalShooterMotor.config_kP(0, 0.22);
        highGoalShooterMotor.config_kI(0, 0);
        highGoalShooterMotor.config_kD(0, 0);
	}

	public void putManipulatorDisplays() {
		// tuningModeBooleanBox = Robot.statusDisplayTab
		// 	.add("Motor Tuning Mode", false)
		// 	.withWidget(BuiltInWidgets.kBooleanBox)
		// 	.withPosition(0, 0)
		// 	.withSize(2, 1)
		// 	.getEntry();

		highShooterPercentageTextField = Robot.statusDisplayTab
			.add("High Goal Shooter Percentage", Constants.Manipulator.HIGH_GOAL_SHOOTER_TARGET_PERCENTAGE)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(0, 1)
			.withSize(2, 1)
			.getEntry();
		
		// lowShooterRPMTextField = Robot.statusDisplayTab
		// 	.add("Low Goal Shooter RPM", Constants.Manipulator.ELBOW_TARGET_RPM)
		// 	.withWidget(BuiltInWidgets.kTextView)
		// 	.withPosition(0, 2)
		// 	.withSize(2, 1)
		// 	.getEntry();

		// intakePercentOutputTextField = Robot.statusDisplayTab
		// 	.add("Intake % Output ", 0.4)
		// 	.withWidget(BuiltInWidgets.kTextView)
		// 	.withPosition(0, 3)
		// 	.withSize(2, 1)
		// 	.getEntry();
	}

	// set motors by velocity
	// public void setElbowFireMotor(double velocity) { lowShooterMotor.set(ControlMode.Velocity, velocity); }
	// public void setWristFireMotor(double velocity) { highGoalShooterMotor.set(ControlMode.Velocity, velocity); }
	
	// set motors by percent output
	// public void setShoulderFireMotor(double percentOutput) { innerIntakeMotor.set(ControlMode.PercentOutput, percentOutput); }
	// public void setIntakeMotor(double percentOutput) { outerIntakeMotor.set(ControlMode.PercentOutput, percentOutput); }
	public void setHighGoalShooterMotor(double percentOutput) { highGoalShooterMotor.set(ControlMode.PercentOutput, percentOutput); }

	// read potentiometer
	public double getPivotPotentiometer() { return pivotPotentiometer.get(); }

	// (if confused about distPerSecToRPM static constant, check comment in definition)
	// public double getLeftEncoderRPM() { return -leftDriveEncoder.getRate() * distPerSecToRPM; }

	// public double getRightEncoderRPM() { return -rightDriveEncoder.getRate() * distPerSecToRPM; }

	// get whether the pivot is up
	public boolean pivotIsUp(){
		if (getPivotPotentiometer() > 13) {
			return pivotUp;
		} else {
			return !pivotUp;
		}
	}

	@Override
	public void periodic() {
		// inTuningMode = tuningModeBooleanBox.getBoolean(false); // TODO: Make this an interactable boolean in Shuffleboard
		inTuningMode = true;

		// highGoalShooterMotor.set(ControlMode.PercentOutput, highShooterPercentageTextField.getDouble(Constants.Manipulator.HIGH_GOAL_SHOOTER_TARGET_PERCENTAGE));
	}
}