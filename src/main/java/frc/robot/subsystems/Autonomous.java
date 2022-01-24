package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Autonomous extends SubsystemBase {
	// ----------------------------------------------------------
	// Constants

	// TODO: Config all autonomous strategy options we should have
	public enum AutonomousRoutine {
		DRIVE_STRAIGHT_BACKWARDS,
		DRIVE_STRAIGHT_TO_LOW_HUB
	}

	// ----------------------------------------------------------
	// Constructor and actions

	public Autonomous() {
		
	}

	// ----------------------------------------------------------
	// Scheduler functions

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}
