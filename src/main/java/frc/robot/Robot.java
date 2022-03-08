package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.subsystems.Lights.Pattern;


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
		
		robotContainer.drivetrain
			.configureDrivetrain(RobotContainer.defaultRobot)
			// the robot should not be moving while the IMU is calibrating
			.calibrateIMU()
			.resetIMU();

		robotContainer.vision.startDefaultCameraStreams();

		if (RobotContainer.enableDeveloperMode) {
			robotContainer.initializeJoystickValues();
		}
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();

		robotContainer
			.listenForRobotSelection()
			
			.listenForJoystickModes()
			.listenForJoystickDevices()
			
			.listenForPremadeAutoRoutine();
		
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

		robotContainer.retractIntkeArm();

		robotContainer.lights.sendCommand(Pattern.UNDERGLOW_BLUE.value());
	}

	@Override
	public void disabledPeriodic() {
		
	}

	@Override
	public void disabledExit() {
		robotContainer.drivetrain.brakeMotors();
	}


	// ----------------------------------------------------------
	// Autonomous-phase scheduler methods


	@Override
	public void autonomousInit() {
		robotContainer
			.retractIntkeArm()
			.getAutoCommand().schedule();
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
		
	}

	@Override
	public void teleopPeriodic() {
		
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