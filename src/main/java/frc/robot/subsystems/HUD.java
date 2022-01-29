package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class HUD extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources

	public ShuffleboardTab HUDTab;

	public ShuffleboardTab tuningToolsTab;

	// ----------------------------------------------------------
	// Motor tuning resources

	public NetworkTableEntry tuningModeToggleSwitch;

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
	// Constructor and actions

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
	}

	public void initializeTuningTools() {
		tuningToolsTab = Shuffleboard.getTab("Tuning Tools");

		tuningModeToggleSwitch = tuningToolsTab
			.add("Motor Tuning Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(0, 0)
			.withSize(2, 1)
			.getEntry();

		retractIntakeMotorToggleSwitch = tuningToolsTab
			.add("Retract Intake Motor Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(1, 1)
			.withSize(2, 1)
			.getEntry();
		retractIntakeMotorPositionTextView = tuningToolsTab
			.add("Retract Intake Motor Position", Intake.RETRACT_MOTOR_DEFAULT_POSITION)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 1)
			.withSize(2, 1)
			.getEntry();
		
		rollerIntakeMotorToggleSwitch = tuningToolsTab
			.add("Roller Intake Motor Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(1, 2)
			.withSize(2, 1)
			.getEntry();
		rollerIntakeMotorPercentTextView = tuningToolsTab
			.add("Roller Intake Motor Percent", Intake.ROLLER_MOTOR_DEFAULT_PERCENT_OUTPUT)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 2)
			.withSize(2, 1)
			.getEntry();

		lowerConveyorMotorToggleSwitch = tuningToolsTab
			.add("Lower Manipulator Motor Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(1, 3)
			.withSize(2, 1)
			.getEntry();
		lowerConveyorMotorPercentTextView = tuningToolsTab
			.add("Lower Manipulator Motor Percent", Manipulator.LOWER_MOTOR_DEFAULT_PERCENT_OUTPUT)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 3)
			.withSize(2, 1)
			.getEntry();
		
		higherConveyorMotorToggleSwitch = tuningToolsTab
			.add("Higher Manipulator Motor Enabled", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(1, 4)
			.withSize(2, 1)
			.getEntry();
		higherConveyorMotorPercentTextView = tuningToolsTab
			.add("Higher Manipulator Motor Percent", Manipulator.HIGHER_MOTOR_DEFAULT_PERCENT_OUTPUT)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 4)
			.withSize(2, 1)
			.getEntry();
	}

	// ----------------------------------------------------------
	// Scheduler functions

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}