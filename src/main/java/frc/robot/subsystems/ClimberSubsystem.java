/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

// import edu.wpi.first.wpilibj.Encoder;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class ClimberSubsystem extends SubsystemBase {
	private WPI_TalonSRX climbMotor;
	// private Encoder climbEncoder;

	//public double Distance(){return climbEncoder.getDistance();}
	// private final double climbMaxDist = 1.0;
	// private final double climbMinDist = 0.0;

	public ClimberSubsystem() {
		climbMotor = new WPI_TalonSRX(Constants.Climber.TALONSRX_ID);
		// SmartDashboard.putString("Climber State", "NEUTRAL");
	}

	public void setClimbMotor(double motorValue) {
		climbMotor.set(ControlMode.PercentOutput, motorValue);
	}

	// public void toggleElevatorState() {
		// if(state == true)
		// {
			//if(climbMinDist < Distance() && Distance() > climbMaxDist)
			//{
			//	climbMotor.set(ControlMode.PercentOutput,0.5);
			//} else {
			//	climbMotor.set(ControlMode.PercentOutput,0);
			//}
		//} else {
		//	if(climbMinDist < Distance() && Distance() > climbMaxDist)
		//	{
		//		climbMotor.set(ControlMode.PercentOutput,-0.5);
		//	} else {
		//		climbMotor.set(ControlMode.PercentOutput,0);
		//	}
		// }
	// }

	// public Encoder getClimbEncoder() { return climbEncoder; }

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}