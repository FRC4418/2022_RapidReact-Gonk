package frc.robot;


import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.commands.intake.RetractIntakeArmWithFeedbackInDisabled;


public class Robot extends TimedRobot {
	// ----------------------------------------------------------
	// Public resources

	
	public static RobotContainer robotContainer;


	// ----------------------------------------------------------
	// Robot-mode scheduler methods


	@Override
	public void robotInit() {
		LiveWindow.disableAllTelemetry();

		robotContainer = new RobotContainer();

		robotContainer.drivetrain.configureDrivetrain(RobotContainer.defaultRobot);
		// the robot should not be moving while the IMU is calibrating
		robotContainer.drivetrain.calibrateIMU();
		robotContainer.drivetrain.resetIMU();
		
		robotContainer.intake.resetRetractorEncoder();

		robotContainer.vision.startDefaultCameraStreams();

		if (RobotContainer.enableDeveloperMode) {
			robotContainer.initializeJoystickValues();
		}

		// String entryName = "Launcher-Firing Duration [s]";
		// var entry = NetworkTableInstance.getDefault().getTable("Shuffleboard/Autonomous/Autonomous/Column 2").getEntry(entryName);
		// if (entry.exists()) {
		// 	SmartDashboard.putString("Entry cleared:", entryName);
		// }
		// entry.clearPersistent();
		// entry.removeListener(0);
		// entry.delete();
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();

		robotContainer
			.listenForJoystickModes()
			.listenForJoystickDevices()
			
			.listenForPremadeAutoRoutine()

			.updatePrintoutDisplays();
		
		if (RobotContainer.enableDeveloperMode) {
			robotContainer
				.printJoystickValues();
		}
	}


	// ----------------------------------------------------------
	// Disabled-mode scheduler methods


	@Override
	public void disabledInit() {
		robotContainer.drivetrain.coastMotors();

		(new RetractIntakeArmWithFeedbackInDisabled(robotContainer.intake)).schedule();
		
		robotContainer.manipulator.stopLauncher();
		
		robotContainer.lights.setAllToSlowRGBCycle();
	}

	@Override
	public void disabledPeriodic() {
		
	}

	@Override
	public void disabledExit() {
		robotContainer.drivetrain.coastMotors();
		
		robotContainer.manipulator.idleLauncher();
	}


	// ----------------------------------------------------------
	// Autonomous-phase scheduler methods


	@Override
	public void autonomousInit() {
		robotContainer.drivetrain.disableOpenLoopRamp();

		robotContainer.intake.extendIntakeArm();

		robotContainer.getAutoCommand().schedule();
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void autonomousExit() {
		robotContainer.getAutoCommand().cancel();
	}


	// ----------------------------------------------------------
	// Teleop-phase scheduler methods


	@Override
	public void teleopInit() {
		robotContainer.climber.attachPin();

		robotContainer.drivetrain.useTeleopOpenLoopRamp();

		robotContainer.intake.retractIntakeArm();
	}


	// ----------------------------------------------------------
	// Test-phase scheduler methods


	@Override
	public void testInit() {
		// Cancels all running commands at the start of test mode.
		CommandScheduler.getInstance().cancelAll();
	}

	@Override
	public void testPeriodic() {
		
	}
}