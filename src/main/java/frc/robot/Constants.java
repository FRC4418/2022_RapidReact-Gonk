package frc.robot;


public class Constants {
    private static final double kMetersToinches = 39.37;

    public static double inchesToMeters(double inches) {
        return inches / kMetersToinches;
    }

    public static double metersToInches(double meters) {
        return meters * kMetersToinches;
    }

    public static class Falcon500 {
        public static int ticksPerRevolution = 2048;

        // AKA the free RPM
        public static int maxRPM = 6380;
    }

    public static class Drivetrain {
        // ----------------------------------------------------------
        // General

        public static final double
            // kWheelDiameterMeters = Constants.inchesToMeters(6.),
            kWheelDiameterMetersV2 = Constants.inchesToMeters(4.);

        public static enum MotorGroup {
            kLeft,
            kRight
        }

        public static class CAN_ID {
            // These are how it's SUPPOSED to be on both V1 and V2
            public static final int
                kFrontLeft = 3,
                kBackLeft = 2,
                kFrontRight = 4,
                kBackRight = 5;
        }

        // ----------------------------------------------------------
        // Conversions

        public static final double
            // kTicksToMeters  = (kWheelDiameterMetersV1 * Math.PI) / ((double) Falcon500.ticksPerRevolution) / 7.33,
            kTicksToMetersV2  = (kWheelDiameterMetersV2 * Math.PI) / ((double) Falcon500.ticksPerRevolution) / 7.75,
            // kMPSToTicksPer100ms = ((double) Falcon500.ticksPerRevolution) / (10. * kWheelDiameterMeters * Math.PI),
            kMPSToTicksPer100msV2 = ((double) Falcon500.ticksPerRevolution) / (10. * kWheelDiameterMetersV2 * Math.PI);

        // ----------------------------------------------------------
        // Kinematics

        // horizontal distance between the left and right-side wheels
        // private static final double kTrackWidthMeters = Constants.inchesToMeters(24.6);
        public static final double kTrackWidthMetersV2 = Constants.inchesToMeters(24.);

        // ----------------------------------------------------------
        // Open-loop control

        public static final double
            // units in seconds
            kJoystickOpenLoopRampTime = 0.7;

        // ----------------------------------------------------------
        // Closed-loop control

        public static final double
            // ksVolts = 0.63599,
            ksVoltsV2 = 0.69552,
            // kvVoltSecondsPerMeter = 0.043021,
            kvVoltSecondsPerMeterV2 = 0.066546,
            // kaVoltSecondsSquaredPerMeter = 0.018985,
            kaVoltSecondsSquaredPerMeterV2 = 0.010455;

        public static final int
            kLeftPidIdx = 0,	// PID slots are basically different contorl-loop types (closed-loop, open-loop, etc)
            kLeftSlotIdx = 0,	// slots are basically different motor control types

            kRightPidIdx = 0,
            kRightSlotIdx = 0,
            // Set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails.
            kTimeoutMs = 30;

        // ID Gains may have to be adjusted based on the responsiveness of control loop. kF: 1023 represents output value to Talon at 100%, 20660 represents Velocity units at 100% output
        // public static final Gains
        //     kLeftVelocityGains = new Gains(0.89077, 0., 0., 1023./20660., 300, 1.00),
        //     kRightVelocityGains = new Gains(kLeftMotorVelocityGains);
        public static final Gains
            kLeftVelocityGainsV2 = new Gains(0.45563, 0., 0., 1023./20660., 300, 1.00),
            kRightVelocityGainsV2 = new Gains(kLeftVelocityGainsV2);

        // ----------------------------------------------------------
        // Max-output modes

        public static final double kMaxSlewRateAllowed = 3.;

        public static class NormalOutputMode {
            public static final double kDefaultMaxOutput = 1.;

            public static class SlewRates {
                public static final double
                    kDefaultArcadeForward = 1.58,
                    kDefaultArcadeTurn = 2.08,
        
                    kDefaultTankForward = 1.0;
            }
        }

        public static class KidsSafetyOutputMode {
            public static final double kDefaultMaxOutput = 0.1;

            public static class SlewRates {
                public static final double
                    kDefaultArcadeForward = 2.,
                    kDefaultArcadeTurn = 2.,

                    kDefaultTankForward = 2.;
            }
        }

        // ----------------------------------------------------------
        // Trajectory

        public static final double
            kMaxSpeedMetersPerSecond = 3.,
            kMaxAccelerationMetersPerSecondSquared = 3.;

        public static final double
            kRamseteB = 2,
            kRamseteZeta = 0.7;
    }

    public static class Intake {
        // ----------------------------------------------------------
        // General

        public static final double
            kDefaultReverseFeederPercent = -0.5,
            kDefaultFeederPercent = 0.5,

            kToOutputRetractorTicksRatio = 58.25;
            
        // retractor has tick range of -((double) Falcon500.ticksPerRevolution * 58.25) to ((double) Falcon500.ticksPerRevolution * 58.25)
        public static final int
            kMaxRetractorTicks = (int) (Falcon500.ticksPerRevolution * kToOutputRetractorTicksRatio),

            kRetractedIntakeRetractorTicks = -2_300,
            kExtendedIntakeRetractorTicks = 7_346,

            kRetractorOriginOffsetBufferMargin = 150,

            kRetractorDegreeTolerance = (int) (kMaxRetractorTicks * 0.05);

        public static class CAN_ID {
            public static final int
                kFeeder = 11,
                kRetractor = 12;
        }

        public static final int kWhiskerSensorDIOPort = 8;

        // ----------------------------------------------------------
        // Conversion

        public static final double
            kRetractorDegreesToTicks = ((double) Falcon500.ticksPerRevolution * 58.25) / 360.;

        // ----------------------------------------------------------
        // Open-loop controls

        public static final double kRetractorOpenLoopRampSeconds = 1.d;

        // ----------------------------------------------------------
        // Closed-loop control

        public static final double
            // in seconds
            kFeederRampTime = 0.25;

        // ----------------------------------------------------------
        // Closed-loop control
        
        public static final int
            kRetractorPidIdx = 0,
            kRetractorSlotIdx = 0,
            kTimeoutMs = 30;

        // TODO: P3 tune V1 retractor gains
        // private final Gains kRetractorPositionGains
        // 	= new Gains(0.1, 0., 0., 1023./20660., 300, 1.00);
        public static final Gains kRetractorPositionGainsV2
            = new Gains(0.03, 0., 0., 1023./20660., 300, 1.00);
    }

    public static class Manipulator {
        // ----------------------------------------------------------
        // General

        public static final double
            kDefaultIndexerPercent = 1.0;

        public static final int kDefaultLauncherRPM = Falcon500.maxRPM;
        public static final double kDefaultLauncherPercent = 1.;

        public static class CAN_ID {
            public static final int
                kIndexer = 21,
                kLauncher = 22;
        }

        // ----------------------------------------------------------
        // Conversion

        // Falcon 500s have a free speed of 6380 RPM, which means a maximum of 21,777 ticks per 100ms
        public static final double kRpmToTicksPer100ms = ((double) Falcon500.ticksPerRevolution * 4.) / 600.;

        // ----------------------------------------------------------
        // Closed-loop control

        public static final int
            kLauncherPidIdx = 0,
            kTimeoutMs = 30;

        // public static final Gains kLauncherRPMGains
        // 	= new Gains(0.083708, 0., 0., 1023./20660., 300, 1.00);
        public static final Gains kLauncherRPMGainsV2
            = new Gains(0.040753, 0., 0., 1023./20660., 300, 1.00);
    }
}