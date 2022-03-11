package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class Climber extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private boolean pinsReleased = false;

	private final Servo
		m_leftServo = new Servo(Constants.Climber.kLeftServoPWMChannel),
		m_rightServo = new Servo(Constants.Climber.kRightServoPWMChannel);
	
	private double servoSetAngle = Constants.Climber.kPinInAngle;


	// ----------------------------------------------------------
	// Extension methods


	public Climber toggleClimberPins() {
		if (!pinsReleased) {
			releasePins();
		} else {
			attachPins();
		}
		pinsReleased = !pinsReleased;
		return this;
	}

	public void releasePins() {
		setServosAngle(Constants.Climber.kPinOutAngle);
	}

	public void attachPins() {
		setServosAngle(Constants.Climber.kPinInAngle);
	}

	public boolean pinsAreReleased() {
		return servoSetAngle == Constants.Climber.kPinInAngle;
	}

	public boolean pinsAreAttached() {
		return servoSetAngle == Constants.Climber.kPinOutAngle;
	}

	public double getServosAngle() {
		double leftServoAngle = m_leftServo.getAngle();
		double rightServoAngle = m_rightServo.getAngle();
		assert leftServoAngle == rightServoAngle;
		return leftServoAngle;
	}

	public void setServosAngle(double degree) {
		m_leftServo.setAngle(degree);
		m_rightServo.setAngle(degree);
	}
}
