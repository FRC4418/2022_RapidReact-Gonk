package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class RunRoller extends CommandBase {
	// ----------------------------------------------------------
	// Constants
	
	private final double INTAKE_ROLLER_OUTPUT_PERCENT = 0.6d;

	// ----------------------------------------------------------
	// Resources

	private Intake it;

	// ----------------------------------------------------------
	// Constructor

	public RunRoller(Intake intake) {
		it = intake;
		// addRequirements(it);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		
	}

	int counter = 0;

	@Override
	public void execute() {
		it.setRollerMotorPercent(INTAKE_ROLLER_OUTPUT_PERCENT);
		SmartDashboard.putNumber("intake", counter++);
	}

	@Override
	public void end(boolean interrupted) {
		it.setRollerMotorPercent(0.d);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
