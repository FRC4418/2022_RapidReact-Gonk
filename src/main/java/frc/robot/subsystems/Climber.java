package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Climber extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private final Servo m_leftPinServo = new Servo(0);
	private final Servo m_rightPinServo = new Servo(2);

	private final double m_pinOutAngle = 30.;


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
		m_leftPinServo.setAngle(m_pinOutAngle);
		m_rightPinServo.setAngle(m_pinOutAngle);
		return this;
	}
}
