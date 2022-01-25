package frc.robot;


import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Telemetry;
import frc.robot.subsystems.TeleopInput;


public class RobotContainer {
    public static TeleopInput teleopInput = new TeleopInput();

	public static Drivetrain drivetrain = new Drivetrain();
	public static Intake intake = new Intake();
	public static Manipulator manipulator = new Manipulator();
	public static Climber climber = new Climber();

	// public static Sensory sensory = new Sensory();
	public static Autonomous autonomous = new Autonomous();
	public static Telemetry telemetry = new Telemetry();
}
