package frc.robot.subsystems;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.displays.AutonomousDisplay;
import frc.robot.displays.JoysticksDisplay;
import frc.robot.displays.MotorTestingDisplay;


public class HUD extends SubsystemBase {
	// ----------------------------------------------------------
	// Public resources


	public MotorTestingDisplay motorTestingDisplay;

	public JoysticksDisplay joysticksDisplay;
	public AutonomousDisplay autonomousDisplay;
	

	// ----------------------------------------------------------
	// Private resources


	private ShuffleboardTab HUDTab;
	private ShuffleboardTab diagnosticsTab;


	// ----------------------------------------------------------
	// Constructor and methods

	
	public HUD() {

	}

	public void initializeHUD() {
		HUDTab = Shuffleboard.getTab("HUD");

		joysticksDisplay = new JoysticksDisplay(HUDTab, 2, 0);
		autonomousDisplay = new AutonomousDisplay(HUDTab, 0, 0);
	}

	public void initializeDiagnostics() {
		diagnosticsTab = Shuffleboard.getTab("Diagnostics");
		
		motorTestingDisplay = new MotorTestingDisplay(diagnosticsTab, 0, 0);
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}