package frc.robot;


// import edu.wpi.first.cscore.UsbCamera;
// import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.commands.ConveyerDemo;
import frc.robot.commands.AutoDriveStraightForDistance;
import frc.robot.commands.AutoDriveStraightForDistance.DriveStraightDirection;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
	// private UsbCamera m_frontShooterCamera;
	// private UsbCamera m_rightPanelCamera;

	public static Command m_autonomousCommand;

	// run when robot is started, put initialization code here
	@Override
	public void robotInit() {
		// Configure the button bindings
		RobotContainer.DriverControls.configureButtonBindings();
		RobotContainer.SpotterControls.configureButtonBindings();
		
		// autonomous, drive straight and backwards for 30 inches
		m_autonomousCommand = new AutoDriveStraightForDistance(60.0d, DriveStraightDirection.BACKWARDS);

		// m_frontShooterCamera = CameraServer.startAutomaticCapture(0);
		// m_rightPanelCamera = CameraServer.startAutomaticCapture(1);

		RobotContainer.telemetry.initializeTelemetry();
	}

	// called every robot packet (good for diagnostics), after mode-specific periodics
	// runs before LiveWindow & SmartDashboard updates
	@Override
	public void robotPeriodic() {
		// runs base periodic functions. Do not delete/comment out
		CommandScheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {

	}

	// Runs autonomous command selected by {@link RobotContainer} class
	@Override
	public void autonomousInit() {
		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.schedule();
		}
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopInit() {
		// stops auto before teleop starts running
		// comment out to continue auto as another command starts
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}

		var manipulatorDemo = new ConveyerDemo();
		manipulatorDemo.schedule();
	}

	@Override
	public void teleopPeriodic() {
		
	}

	@Override
	public void testInit() {
		// Cancels all running commands at the start of test mode.
		CommandScheduler.getInstance().cancelAll();
	}

	@Override
	public void testPeriodic() {

	}
}
