package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Climber extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private final Servo m_leftPinServo = new Servo(Constants.Climber.kLeftServoPWMChannel);
	private final Servo m_rightPinServo = new Servo(Constants.Climber.kRightServoPWMChannel);


	// ----------------------------------------------------------
	// Constructor


	public Climber() {

	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Extension methods


	public Climber extend() {
		m_leftPinServo.setAngle(Constants.Climber.kPinOutAngle);
		m_rightPinServo.setAngle(Constants.Climber.kPinOutAngle);
		return this;
	}
}
