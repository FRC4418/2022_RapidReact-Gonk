package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class Climber extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private boolean pinsReleased = false;

	private final Servo
		m_leftPinServo = new Servo(Constants.Climber.kLeftServoPWMChannel),
		m_rightPinServo = new Servo(Constants.Climber.kRightServoPWMChannel);


	// ----------------------------------------------------------
	// Extension methods


	public Climber toggleClimberPins() {
		if (!pinsReleased) {
			m_leftPinServo.setAngle(Constants.Climber.kPinOutAngle);
			m_rightPinServo.setAngle(Constants.Climber.kPinOutAngle);
		} else {
			m_leftPinServo.setAngle(Constants.Climber.kPinInAngle);
			m_rightPinServo.setAngle(Constants.Climber.kPinInAngle);
		}
		pinsReleased = !pinsReleased;
		return this;
	}

	public double getLeftServoAngle() {
		return m_leftPinServo.getAngle();
	}
	public void setLeftServoAngle(double angle) {
		m_leftPinServo.setAngle(angle);
	}

	public double getRightServoAngle() {
		return m_rightPinServo.getAngle();
	}
	public void setRightServoAngle(double angle) {
		m_rightPinServo.setAngle(angle);
	}
}
