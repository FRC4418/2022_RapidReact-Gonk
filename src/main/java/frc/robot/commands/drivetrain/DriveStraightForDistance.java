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

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction, double maxMotorMPS) {
		super(drivetrain, direction, maxMotorMPS);

		m_distanceInMeters = distanceInMeters;

		SmartDashboard.putNumber("Distance in meters", distanceInMeters);
		SmartDashboard.putNumber("MaxMotor mps", maxMotorMPS);
	}

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction) {
		this(drivetrain, distanceInMeters, direction, Autonomous.getDrivingMaxMotorMPS());
	}

	public DriveStraightForDistance(Drivetrain drivetrain, DriveStraightDirection direction, double maxMotorMPS) {
		this(drivetrain, Autonomous.getTarmacLeavingMeters(), direction, maxMotorMPS);
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