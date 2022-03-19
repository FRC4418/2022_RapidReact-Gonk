package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class Climber extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private final WPI_TalonFX m_winchMotor = new WPI_TalonFX(Constants.Climber.CAN_ID.kWinch);

	private final Servo rachetPinServo = new Servo(Constants.Climber.kRatchetPinServoPWMChannel);


	// ----------------------------------------------------------
	// Constructor


	public Climber() {
		// ----------------------------------------------------------
		// Winch motor configuration

		m_winchMotor.configFactoryDefault();
		m_winchMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Climber.kWinchPidIdx, Constants.Climber.kTimeoutMs);

		// ----------------------------------------------------------
		// Final setup

		configurePIDs();
	}


	// ----------------------------------------------------------
	// Constants-reconfiguration methods


	public void configurePIDs() {
		m_winchMotor.config_kP(Constants.Climber.kWinchPidIdx, Constants.Climber.kWinchPositionGains.kP);
		m_winchMotor.config_kI(Constants.Climber.kWinchPidIdx, Constants.Climber.kWinchPositionGains.kI);
        m_winchMotor.config_kD(Constants.Climber.kWinchPidIdx, Constants.Climber.kWinchPositionGains.kD);
		// m_winchMotor.config_kF(Constants.Climber.kWinchPidIdx, Constants.Climber.kWinchPositionGains.kF);
	}


	// ----------------------------------------------------------
	// Winch-motor methods


	public void setWinchToExtendPercent() {
		m_winchMotor.set(ControlMode.PercentOutput, Constants.Climber.kWinchSpeedPercent);
	}
	public void setWinchToLowerPercent() {
		m_winchMotor.set(ControlMode.PercentOutput, -Constants.Climber.kWinchSpeedPercent);
	}
	public void stopWinchMotor() {
		m_winchMotor.set(ControlMode.PercentOutput, 0.);
	}

	public double getWinchTraveledInches() {
		return
			m_winchMotor.getSelectedSensorPosition(Constants.Climber.kWinchPidIdx) / Constants.Climber.kWinchOutputInchesToInputTicks;
	}

	private boolean withinWinchInchesRange(double positionInches) {
		return positionInches >= Constants.Climber.kWinchMinPositionInches && positionInches <= Constants.Climber.kWinchMaxPositionInches;
	}

	public void setWinchPositionInches(double inches) {
		if (withinWinchInchesRange(inches)) {
			m_winchMotor.set(ControlMode.Position, inches * Constants.Climber.kWinchOutputInchesToInputTicks);
		}
	}

	public void setWinchToExtendedPosition() {
		m_winchMotor.set(ControlMode.Position, Constants.Climber.kClimberExtendedHeightInches);
	}
	public void setWinchToLoweredPosition() {
		m_winchMotor.set(ControlMode.Position, Constants.Climber.kClimberLoweredHeightInches);
	}


	// ----------------------------------------------------------
	// Ratchet-pin servo methods


	public void releasePin() {
		rachetPinServo.setAngle(Constants.Climber.kReleasePinAngle);
	}

	public void attachPin() {
		rachetPinServo.setAngle(Constants.Climber.kAttachPinAngle);
	}

	public boolean pinIsReleased() {
		return getServoAngle() == Constants.Climber.kReleasePinAngle;
	}

	public boolean pinIsAttached() {
		return getServoAngle() == Constants.Climber.kAttachPinAngle;
	}

	public double getServoAngle() {
		return rachetPinServo.getAngle();
	}

	public void setServosAngle(double degree) {
		rachetPinServo.setAngle(degree);
	}
}
