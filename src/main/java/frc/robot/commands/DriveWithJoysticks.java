package frc.robot.commands;


import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.subsystems.Drivetrain;


public class DriveWithJoysticks extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

	private final JoystickControls m_joystickControls;

	private final SlewRateLimiter arcadeDriveForwardLimiter = new SlewRateLimiter(0.5d);
	private final SlewRateLimiter arcadeDriveTurnLimiter = new SlewRateLimiter(0.5d);

	private final SlewRateLimiter tankDriveForwardLimiter = new SlewRateLimiter(0.d);

	// ----------------------------------------------------------
	// Constructor

	public DriveWithJoysticks(Drivetrain drivetrain, JoystickControls joystickControls) {
		this.m_joystickControls = joystickControls;
		this.m_drivetrain = drivetrain;

		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		
	}

	@Override
	public void execute() {
		switch (RobotContainer.driverJoystickMode) {
			case ARCADE:
				m_drivetrain.arcadeDrive(
					arcadeDriveForwardLimiter.calculate(m_joystickControls.getArcadeDriveForwardAxis()),
					arcadeDriveTurnLimiter.calculate(m_joystickControls.getArcadeDriveAngleAxis()));
				break;
			case LONE_TANK:
			case DUAL_TANK:
				m_drivetrain.tankDrive(
					tankDriveForwardLimiter.calculate(m_joystickControls.getTankDriveLeftAxis()),
					tankDriveForwardLimiter.calculate(m_joystickControls.getTankDriveRightAxis()));
				break;
		}
	}

	@Override
	public void end(boolean interrupted) {
		
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
