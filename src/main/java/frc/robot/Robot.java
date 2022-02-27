package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


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

		// robotContainer.vision.startCameraStreams();

		if (RobotContainer.enableDiagnostics) {
			robotContainer.initializeJoystickValues();
		}
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();

		robotContainer
			// .listenForRobotSelection()
			
			.listenForJoystickModes()
			.listenForJoystickDevices()
			
			.listenForAutoRoutine();
		
		if (RobotContainer.enableDiagnostics) {
			// robotContainer
			// 	.printJoystickValues();
		}
	}


	// ----------------------------------------------------------
	// Disabled-mode scheduler methods


	@Override
	public void disabledInit() {
		robotContainer.drivetrain.coastMotors();

		// robotContainer.intake.extendIntakeArm();


		robotContainer.intake.coastRetractor();
	}

	@Override
	public void disabledPeriodic() {
		
	}

	@Override
	public void disabledExit() {
		robotContainer.drivetrain.brakeMotors();

		robotContainer.intake
			.brakeRetractor()
			.retractIntakeArm();
	}


	// ----------------------------------------------------------
	// Autonomous-phase scheduler methods


	@Override
	public void autonomousInit() {
		robotContainer.intake.extendIntakeArm();

		robotContainer.getAutoCommand().schedule();
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void autonomousExit() {
		robotContainer.getAutoCommand().cancel();

		robotContainer.intake.retractIntakeArm();
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