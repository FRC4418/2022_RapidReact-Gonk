package frc.robot.commands.drivetrain;


import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;


public class DriveStraightForDistance extends DriveStraightWhileHeld {
	// ----------------------------------------------------------
	// Resources

	private final double m_distanceInMeters;

	// ----------------------------------------------------------
	// Constructors

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction, double maxSpeed) {
		super(drivetrain, direction, maxSpeed);

		m_distanceInMeters = distanceInMeters;
	}

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction) {
		this(drivetrain, distanceInMeters, direction, Autonomous.getDrivingMaxSpeedPercentage());
	}

	public DriveStraightForDistance(Drivetrain drivetrain, DriveStraightDirection direction, double motorMPS) {
		this(drivetrain, Autonomous.getDrivingMaxSpeedPercentage(), direction, motorMPS);
	}

	public DriveStraightForDistance(Drivetrain drivetrain, DriveStraightDirection direction) {
		this(drivetrain, Autonomous.getTarmacLeavingMeters(), direction);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public boolean isFinished() {
		return m_drivetrain.getAverageDistanceMeters() >= m_distanceInMeters;
	}
}