package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
	// Constructor


	public Climber() {

	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		SmartDashboard.putNumber("Servo angle", m_leftPinServo.getAngle());
	}


	// ----------------------------------------------------------
	// Extension methods


	public Climber extend() {
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
}
