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
// import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
//import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.Manipulator.ConveyorShooter;
// import frc.robot.Robot;
import frc.robot.Constants.Manipulator.Intake;


public class Manipulator extends SubsystemBase {
	public boolean inTuningMode;

	public NetworkTableEntry intakePercentOutputTextField;

	public WPI_TalonSRX intake0;
	public WPI_TalonSRX intake1;
	public WPI_TalonSRX intake2;
	
	public WPI_TalonSRX lowConveyorMotor;
	public WPI_TalonSRX highConveyorMotor;
	

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

	public Manipulator() { 
		// TODO: Change intake motor names after learning of functionality
		intake0 = new WPI_TalonSRX(Intake.INTAKE_0_ID);
		intake1 = new WPI_TalonSRX(Intake.INTAKE_1_ID);
		intake2 = new WPI_TalonSRX(Intake.INTAKE_2_ID);

		lowConveyorMotor = new WPI_TalonSRX(ConveyorShooter.LOW_CONVEYOR_MOTOR_ID);
		highConveyorMotor = new WPI_TalonSRX(ConveyorShooter.HIGH_CONVEYOR_MOTOR_ID);

		//highGoalShooterMotor = new WPI_TalonSRX(Constants.Manipulator.HIGH_GOAL_SHOOTER_775_ID);

		// wristFireMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        //highGoalShooterMotor.setSensorPhase(false);
        /* set closed loop gains in slot0 */
        //highGoalShooterMotor.config_kF(0, 0.1097);
        //highGoalShooterMotor.config_kP(0, 0.22);
        //highGoalShooterMotor.config_kI(0, 0);
        //highGoalShooterMotor.config_kD(0, 0);
	}

	// set motors by velocity
	// public void setElbowFireMotor(double velocity) { lowShooterMotor.set(ControlMode.Velocity, velocity); }
	// public void setWristFireMotor(double velocity) { highGoalShooterMotor.set(ControlMode.Velocity, velocity); }
	
	// set motors by percent output
	public void setLowConveyerMotor(double percentOutput) { lowConveyorMotor.set(ControlMode.PercentOutput, percentOutput); }
	public void setHighConveyerMotor(double percentOutput) { highConveyorMotor.set(ControlMode.PercentOutput, percentOutput); }
	public void setIntake(double percentOutput) { intake0.set(ControlMode.PercentOutput, percentOutput); }
	public void setIntake2(double percentOutput) { intake1.set(ControlMode.PercentOutput, percentOutput); }
	public void setIntake3(double percentOutput) { intake2.set(ControlMode.PercentOutput, percentOutput); }

	public double getLowConveyerMotor() { return lowConveyorMotor.get(); }
	public double getHighConveryerMotor() { return highConveyorMotor.get(); }
	public double getIntake() { return intake0.get(); }
	public double getIntake2() { return intake1.get(); }
	public double getIntake3() { return intake2.get(); }

	// (if confused about distPerSecToRPM static constant, check comment in definition)
	// public double getLeftEncoderRPM() { return -leftDriveEncoder.getRate() * distPerSecToRPM; }

	// public double getRightEncoderRPM() { return -rightDriveEncoder.getRate() * distPerSecToRPM; }

	@Override
	public void periodic() {
		inTuningMode = true;

		// highGoalShooterMotor.set(ControlMode.PercentOutput, highShooterPercentageTextField.getDouble(Constants.Manipulator.HIGH_GOAL_SHOOTER_TARGET_PERCENTAGE));
	}
}