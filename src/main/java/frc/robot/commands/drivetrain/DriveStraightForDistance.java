package frc.robot.commands.drivetrain;


import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;


public class DriveStraightForDistance extends DriveStraight {
	// ----------------------------------------------------------
	// Resources

	private final double m_distanceInMeters;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction, double motorMPS) {
		super(drivetrain, direction);
		m_motorMPS = motorMPS;

		m_distanceInMeters = distanceInMeters;
	}

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction) {
		this(drivetrain, distanceInMeters, direction, Constants.Autonomous.kDefaultDriveStraightMPS);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public boolean isFinished() {
		return m_drivetrain.getAverageDistanceMeters() >= m_distanceInMeters;
	}
}