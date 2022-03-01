package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class RunFeederAndIndexerWithTrigger extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Intake m_intake;
	private final Manipulator m_manipulator;

	// ----------------------------------------------------------
	// Constructor

	public RunFeederAndIndexerWithTrigger(Intake intake, Manipulator manipulator) {
		m_intake = intake;
		m_manipulator = manipulator;

		addRequirements(m_intake);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public boolean runsWhenDisabled() {
		return false;
	}

	@Override
	public void execute() {
		double driverFeederAxis = RobotContainer.driverJoystickControls.getFeederAxis();
		double driverReverseFeederAxis = RobotContainer.driverJoystickControls.getReverseFeederAxis();
		
		double spotterFeederAxis = RobotContainer.spotterJoystickControls.getFeederAxis();
		double spotterReverseFeederAxis = RobotContainer.spotterJoystickControls.getReverseFeederAxis();

		// driver's triggers take priority over the spotter's triggers
		if (driverFeederAxis == 0. && driverReverseFeederAxis == 0.) {
			if (spotterFeederAxis == 0.) {
				m_intake.setFeederPercent(-spotterReverseFeederAxis);
				m_manipulator.setIndexerPercent(-spotterReverseFeederAxis);
			} else {
				m_intake.setFeederPercent(spotterFeederAxis);
				m_manipulator.setIndexerPercent(spotterFeederAxis);
			}
		} else {
			// feeder axis (meaning that feeder is spinning to take IN a ball) takes priority over reverse feeder axis
			if (driverFeederAxis == 0.) {
				m_intake.setFeederPercent(-driverReverseFeederAxis);
				m_manipulator.setIndexerPercent(-driverReverseFeederAxis);
			} else {
				m_intake.setFeederPercent(driverFeederAxis);
				m_manipulator.setIndexerPercent(driverFeederAxis);
			}
		}
	}
	
	@Override
	public boolean isFinished() {
		return false;
	}
}
