package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class RunFeederWithTrigger extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Intake m_intake;
	private final Manipulator m_manipulator;

	// ----------------------------------------------------------
	// Constructor

	public RunFeederWithTrigger(Intake intake, Manipulator manipulator) {
		m_intake = intake;
		m_manipulator = manipulator;

		addRequirements(m_intake);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {}

	@Override
	public void execute() {
		double driverFeederAxis = RobotContainer.driverJoystickControls.getFeederAxis();
		double driverReverseFeederAxis = RobotContainer.driverJoystickControls.getReverseFeederAxis();
		
		double spotterFeederAxis = RobotContainer.spotterJoystickControls.getFeederAxis();
		double spotterReverseFeederAxis = RobotContainer.spotterJoystickControls.getReverseFeederAxis();

		// driver's triggers take priority over the spotter's triggers
		if (driverFeederAxis == 0.d && driverReverseFeederAxis == 0.d) {
			if (spotterFeederAxis == 0.d) {
				m_intake.setFeederMotorPercent(-spotterReverseFeederAxis);
				m_manipulator.setIndexerToPercent(-spotterReverseFeederAxis);
			} else {
				m_intake.setFeederMotorPercent(spotterFeederAxis);
				m_manipulator.setIndexerToPercent(spotterFeederAxis);
			}
		} else {
			// feeder axis (meaning that feeder is spinning to take IN a ball) takes priority over reverse feeder axis
			if (driverFeederAxis == 0.d) {
				m_intake.setFeederMotorPercent(-driverReverseFeederAxis);
				m_manipulator.setIndexerToPercent(-driverReverseFeederAxis);
			} else {
				m_intake.setFeederMotorPercent(driverFeederAxis);
				m_manipulator.setIndexerToPercent(driverFeederAxis);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return false;
	}
}
