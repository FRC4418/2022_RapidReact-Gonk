package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class Telemetry extends SubsystemBase {
	// ----------------------------------------------------------
	// Subsystem resources

	private Manipulator ms;

	public ShuffleboardTab telemetryTab;
	
	public NetworkTableEntry tuningModeBooleanBox;

	public NetworkTableEntry lowerConveyorMotorPercentTextView;
	public NetworkTableEntry higherConveyorMotorPercentTextView;
	
	public SendableChooser<AutonomousRoutine> sendableAutoRoutineChooser = new SendableChooser<>();
	
	public boolean inTuningMode;

	// ----------------------------------------------------------
	// Constructor and actions

	public Telemetry() {
		ms = RobotContainer.manipulator;
	}

	public void initializeTelemetry() {
		telemetryTab = Shuffleboard.getTab("4418 Telemetry");
		
		tuningModeBooleanBox = telemetryTab
			.add("Motor Tuning", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(0, 0)
			.withSize(2, 1)
			.getEntry();

		sendableAutoRoutineChooser.setDefaultOption("Drive Straight Backwards", AutonomousRoutine.DRIVE_STRAIGHT_BACKWARDS);
		sendableAutoRoutineChooser.addOption("Drive Striaght to Low Hub", AutonomousRoutine.DRIVE_STRAIGHT_TO_LOW_HUB);
		telemetryTab
			.add("Autonomous Routine", sendableAutoRoutineChooser)
			.withWidget(BuiltInWidgets.kComboBoxChooser)
			.withPosition(0, 1)
			.withSize(2, 1);

		lowerConveyorMotorPercentTextView = telemetryTab
			.add("Lower Conveyor Motor Percent", ms.DEFAULT_LOWER_MOTOR_PERCENT)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 0)
			.withSize(2, 1)
			.getEntry();
		higherConveyorMotorPercentTextView = telemetryTab
			.add("Higher Conveyor Motor Percent", ms.DEFAULT_HIGHER_MOTOR_PERCENT)
			.withWidget(BuiltInWidgets.kTextView)
			.withPosition(3, 1)
			.withSize(2, 1)
			.getEntry();
	}

	// ----------------------------------------------------------
	// Cycle functions

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
		inTuningMode = tuningModeBooleanBox.getBoolean(false);
		SmartDashboard.putBoolean("Motor Tuning Mode Is On", inTuningMode);
	}
}