package frc.robot.commands.drivetrain;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;


public class DriveStraightForDistance extends DriveStraightWhileHeld {
	// ----------------------------------------------------------
	// Resources

	private final double m_distanceInMeters;

	// ----------------------------------------------------------
	// Constructors

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction, double maxMotorPercent) {
		super(drivetrain, direction, maxMotorPercent);

		m_distanceInMeters = distanceInMeters;
	}

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction) {
		this(drivetrain, distanceInMeters, direction, Autonomous.getDrivingMaxMotorPercent());
	}

	public DriveStraightForDistance(Drivetrain drivetrain, DriveStraightDirection direction, double maxMotorPercent) {
		this(drivetrain, Autonomous.getTarmacLeavingMeters(), direction, maxMotorPercent);
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