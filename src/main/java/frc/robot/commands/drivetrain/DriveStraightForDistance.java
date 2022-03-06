package frc.robot.commands.drivetrain;


import frc.robot.subsystems.Drivetrain;


public class DriveStraightForDistance extends DriveStraight {
	// ----------------------------------------------------------
	// Resources

	private final double m_distanceInMeters;

	// ----------------------------------------------------------
	// Constructor

	// TODO: !!!P1!!! find a way to centralize all the motorMPS values passed by auto routine constructors
	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction, double motorMPS) {
		super(drivetrain, direction);
		m_motorMPS = motorMPS;

		m_distanceInMeters = distanceInMeters;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public boolean isFinished() {
		return m_drivetrain.getAverageDistance() >= m_distanceInMeters;
	}
}