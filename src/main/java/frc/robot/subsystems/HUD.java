package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class HUD extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private ShuffleboardTab HUDTab;

	private ShuffleboardTab diagnosticsTab;


	// ----------------------------------------------------------
	// Motor Output Testing resources


	public NetworkTableEntry motorTestingModeToggleSwitch;

	public NetworkTableEntry lowerConveyorMotorToggleSwitch;
	public NetworkTableEntry lowerConveyorMotorPercentTextView;

	public NetworkTableEntry higherConveyorMotorToggleSwitch;
	public NetworkTableEntry higherConveyorMotorPercentTextView;

	public NetworkTableEntry rollerIntakeMotorToggleSwitch;
	public NetworkTableEntry rollerIntakeMotorPercentTextView;

	public NetworkTableEntry retractIntakeMotorToggleSwitch;
	public NetworkTableEntry retractIntakeMotorPositionTextView;
	
	public SendableChooser<AutonomousRoutine> sendableAutoRoutineChooser = new SendableChooser<>();


	// ----------------------------------------------------------
	// Constructor and methods

	
	public HUD() {

	}

	public void initializeHUD() {
		HUDTab = Shuffleboard.getTab("HUD");

		sendableAutoRoutineChooser.setDefaultOption("Drive Straight Backwards", AutonomousRoutine.DRIVE_STRAIGHT_BACKWARDS);
		sendableAutoRoutineChooser.addOption("Drive Striaght to Low Hub", AutonomousRoutine.DRIVE_STRAIGHT_TO_LOW_HUB);
		HUDTab
			.add("Autonomous Routine", sendableAutoRoutineChooser)
			.withWidget(BuiltInWidgets.kComboBoxChooser)
			.withPosition(1, 1)
			.withSize(2, 1);

		
		// sendableDriverModeChooser
		// sendableDriverDeviceChooser

		// sendableSpotterModeChooser
		// sendableSpotterDeviceChooser
	}

	public void initializeDiagnostics() {
		diagnosticsTab = Shuffleboard.getTab("Diagnostics");

		motorTestingModeToggleSwitch = diagnosticsTab
			.add("Motor Testing Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(0, 0)
			.withSize(2, 1)
			.getEntry();

		retractIntakeMotorToggleSwitch = diagnosticsTab
			.add("Retract Intake Motor Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(1, 1)
			.withSize(2, 1)
			.getEntry();
		retractIntakeMotorPositionTextView = diagnosticsTab
			.add("Retract Intake Motor Position", Intake.RETRACT_MOTOR_DEFAULT_POSITION)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 1)
			.withSize(2, 1)
			.getEntry();
		
		rollerIntakeMotorToggleSwitch = diagnosticsTab
			.add("Roller Intake Motor Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(1, 2)
			.withSize(2, 1)
			.getEntry();
		rollerIntakeMotorPercentTextView = diagnosticsTab
			.add("Roller Intake Motor Percent", Intake.ROLLER_MOTOR_DEFAULT_PERCENT_OUTPUT)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 2)
			.withSize(2, 1)
			.getEntry();

		lowerConveyorMotorToggleSwitch = diagnosticsTab
			.add("Lower Manipulator Motor Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(1, 3)
			.withSize(2, 1)
			.getEntry();
		lowerConveyorMotorPercentTextView = diagnosticsTab
			.add("Lower Manipulator Motor Percent", Manipulator.LOWER_MOTOR_DEFAULT_PERCENT_OUTPUT)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 3)
			.withSize(2, 1)
			.getEntry();
		
		higherConveyorMotorToggleSwitch = diagnosticsTab
			.add("Higher Manipulator Motor Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(1, 4)
			.withSize(2, 1)
			.getEntry();
		higherConveyorMotorPercentTextView = diagnosticsTab
			.add("Higher Manipulator Motor Percent", Manipulator.HIGHER_MOTOR_DEFAULT_PERCENT_OUTPUT)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 4)
			.withSize(2, 1)
			.getEntry();
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}