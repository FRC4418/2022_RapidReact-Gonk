package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;


public class DriveWithJoysticks extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

	// ----------------------------------------------------------
	// Constructor

	public DriveWithJoysticks(Drivetrain drivetrain) {
		m_drivetrain = drivetrain;

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
					m_drivetrain.filterArcadeDriveForward(RobotContainer.driverJoystickControls.getArcadeDriveForwardAxis()),
					m_drivetrain.filterArcadeDriveTurn(RobotContainer.driverJoystickControls.getArcadeDriveTurnAxis()));
				break;
			case LONE_TANK:
				SmartDashboard.putNumber("LEFT AXIS", RobotContainer.driverJoystickControls.getTankDriveLeftAxis());
				SmartDashboard.putNumber("RIGHT AXIS", RobotContainer.driverJoystickControls.getTankDriveRightAxis());

				m_drivetrain.tankDrive(
					RobotContainer.driverJoystickControls.getTankDriveLeftAxis(),
					RobotContainer.driverJoystickControls.getTankDriveRightAxis());
					// m_drivetrain.filterTankDriveForward(RobotContainer.driverJoystickControls.getTankDriveLeftAxis()),
					// m_drivetrain.filterTankDriveForward(RobotContainer.driverJoystickControls.getTankDriveRightAxis()));
				break;
			case DUAL_TANK:
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
