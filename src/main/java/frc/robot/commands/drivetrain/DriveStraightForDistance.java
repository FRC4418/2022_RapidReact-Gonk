package frc.robot.commands.drivetrain;


import frc.robot.subsystems.Drivetrain;


public class DriveStraightForDistance extends DriveStraight {
	// ----------------------------------------------------------
	// Resources

	private final double m_distanceInMeters;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction) {
		super(drivetrain, direction);

		m_distanceInMeters = distanceInMeters;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public boolean isFinished() {
		return m_drivetrain.getAverageDistance() >= m_distanceInMeters;
	}
}