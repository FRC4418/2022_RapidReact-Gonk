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
	
	public WPI_TalonSRX lowerConveyorMotor;
	public WPI_TalonSRX higherConveyorMotor;
	

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
		intake0 = new WPI_TalonSRX(Intake.MOTOR_0_ID);
		intake1 = new WPI_TalonSRX(Intake.MOTOR_1_ID);
		intake2 = new WPI_TalonSRX(Intake.MOTOR_2_ID);

		lowerConveyorMotor = new WPI_TalonSRX(ConveyorShooter.LOWER_MOTOR_ID);
		higherConveyorMotor = new WPI_TalonSRX(ConveyorShooter.HIGHER_MOTOR_ID);

		higherConveyorMotor.setInverted(true);
	}
	
	public void setIntake0(double percentOutput) { intake0.set(ControlMode.PercentOutput, percentOutput); }
	public void setIntake1(double percentOutput) { intake1.set(ControlMode.PercentOutput, percentOutput); }
	public void setIntake2(double percentOutput) { intake2.set(ControlMode.PercentOutput, percentOutput); }
	
	public void setLowConveyerMotor(double percentOutput) { lowerConveyorMotor.set(ControlMode.PercentOutput, percentOutput); }
	public void setHighConveyerMotor(double percentOutput) { higherConveyorMotor.set(ControlMode.PercentOutput, percentOutput); }

	public double getLowConveyerMotor() { return lowerConveyorMotor.get(); }
	public double getHighConveryerMotor() { return higherConveyorMotor.get(); }
	public double getIntake() { return intake0.get(); }
	public double getIntake2() { return intake1.get(); }
	public double getIntake3() { return intake2.get(); }

	@Override
	public void periodic() {
		inTuningMode = true;
	}
}