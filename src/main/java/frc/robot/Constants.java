package frc.robot;


public class Constants {
    public static boolean kDefaultUsingTuningMode = false;
    
    public static void useV1Constants() {
        Drivetrain.kWheelDiameterMeters = Drivetrain.kWheelDiameterMetersV1;
        Drivetrain.kDrivetrainMPSReductionRatio = Drivetrain.kDrivetrainMPSReductionRatioV1;
        Drivetrain.kTicksToMeters = Drivetrain.kTicksToMetersV1;
        Drivetrain.kMPSToTicksPer100ms = Drivetrain.kMPSToTicksPer100msV1;
        Drivetrain.kTrackWidthMeters = Drivetrain.kTrackWidthMetersV1;
        Drivetrain.ksVolts = Drivetrain.ksVoltsV1;
        Drivetrain.kvVoltSecondsPerMeter = Drivetrain.kvVoltSecondsPerMeterV1;
        Drivetrain.kaVoltSecondsSquaredPerMeter = Drivetrain.kaVoltSecondsSquaredPerMeterV1;
        Drivetrain.kLeftVelocityGains = Drivetrain.kLeftVelocityGainsV1;
        Drivetrain.kRightVelocityGains = Drivetrain.kRightVelocityGainsV1;
        
        Intake.kRetractorPositionGains = Intake.kRetractorPositionGainsV1;
        
        Manipulator.kLauncherRPMGains = Manipulator.kLauncherRPMGainsV1;
        Manipulator.kIndexerRPMGains = Manipulator.kIndexerRPMGainsV1;
    }

    public static void useV2Constants() {
        Drivetrain.kWheelDiameterMeters = Drivetrain.kWheelDiameterMetersV2;
        Drivetrain.kDrivetrainMPSReductionRatio = Drivetrain.kDrivetrainMPSReductionRatioV2;
        Drivetrain.kTicksToMeters = Drivetrain.kTicksToMetersV2;
        Drivetrain.kMPSToTicksPer100ms = Drivetrain.kMPSToTicksPer100msV2;
        Drivetrain.kTrackWidthMeters = Drivetrain.kTrackWidthMetersV2;
        Drivetrain.ksVolts = Drivetrain.ksVoltsV2;
        Drivetrain.kvVoltSecondsPerMeter = Drivetrain.kvVoltSecondsPerMeterV2;
        Drivetrain.kaVoltSecondsSquaredPerMeter = Drivetrain.kaVoltSecondsSquaredPerMeterV2;
        Drivetrain.kLeftVelocityGains = Drivetrain.kLeftVelocityGainsV2;
        Drivetrain.kRightVelocityGains = Drivetrain.kRightVelocityGainsV2;
        
        Intake.kRetractorPositionGains = Intake.kRetractorPositionGainsV2;
        
        Manipulator.kLauncherRPMGains = Manipulator.kLauncherRPMGainsV2;
        Manipulator.kIndexerRPMGains = Manipulator.kIndexerRPMGainsV2;
    }

    private static final double kMetersToinches = 39.37;

    public static double inchesToMeters(double inches) {
        return inches / kMetersToinches;
    }

    public static double metersToInches(double meters) {
        return meters * kMetersToinches;
    }

    public static class Falcon500 {
        public static final int
            ticksPerRevolution = 2048,
            // AKA the free RPM
            kMaxRPM = 6380;

        public static final double
            kRpmToTicksPer100ms = ((double) Falcon500.ticksPerRevolution) / 600.,
            // 720 instead of 360 because our degree range is -180 to 180
            kDegreesToTicks = ((double) Falcon500.ticksPerRevolution) / 720.;
    }

    public static class Drivetrain {
        // ----------------------------------------------------------
        // General

        public static double
            kWheelDiameterMeters,

            kDrivetrainMPSReductionRatio;
        private static final double
            kWheelDiameterMetersV1 = Constants.inchesToMeters(6.),
            kWheelDiameterMetersV2 = Constants.inchesToMeters(4.),

            kDrivetrainMPSReductionRatioV1 = 7.33,
            kDrivetrainMPSReductionRatioV2 = 7.75;

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

        public static double
            kTicksToMeters,

            kMPSToTicksPer100ms;
        private static final double
            kTicksToMetersV1  = (kWheelDiameterMetersV1 * Math.PI) / ((double) Falcon500.ticksPerRevolution),
            kTicksToMetersV2  = (kWheelDiameterMetersV2 * Math.PI) / ((double) Falcon500.ticksPerRevolution),
            
            kMPSToTicksPer100msV1 = ((double) Falcon500.ticksPerRevolution) / (10. * kWheelDiameterMetersV1 * Math.PI),
            kMPSToTicksPer100msV2 = ((double) Falcon500.ticksPerRevolution) / (10. * kWheelDiameterMetersV2 * Math.PI);

        // ----------------------------------------------------------
        // Kinematics

        // horizontal distance between the left and right-side wheels
        public static double
            kTrackWidthMeters;
        private static final double
            kTrackWidthMetersV1 = Constants.inchesToMeters(24.6),
            kTrackWidthMetersV2 = Constants.inchesToMeters(24.);

        // ----------------------------------------------------------
        // Open-loop control

        public static double
            // units in seconds
            kDefaultOpenLoopRampTime = 0.7;

        public static class ArcadePolynomial {
            public static double
                kDefaultForwardMultiplier = 1.0,
                kDefaultForwardExponential = 1.0,

                kDefaultTurnMultiplier = 0.8,
                kDefaultTurnExponential = 1.0;
        }

        public static class TankPolynomial {
            public static double
                kDefaultForwardMultiplier = 1.0,
                kDefaultForwardExponential = 1.0;
        }

        // ----------------------------------------------------------
        // Closed-loop control

        public static double
            ksVolts,
            kvVoltSecondsPerMeter,
            kaVoltSecondsSquaredPerMeter;
        private static final double
            ksVoltsV1 = 0.63599,
            ksVoltsV2 = 0.69552,
            kvVoltSecondsPerMeterV1 = 0.043021,
            kvVoltSecondsPerMeterV2 = 0.066546,
            kaVoltSecondsSquaredPerMeterV1 = 0.018985,
            kaVoltSecondsSquaredPerMeterV2 = 0.010455;

        public static final int
            kLeftPidIdx = 0,	// PID slots are basically different contorl-loop types (closed-loop, open-loop, etc)
            kLeftSlotIdx = 0,	// slots are basically different motor control types

            kRightPidIdx = 0,
            kRightSlotIdx = 0,
            // Set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails.
            kTimeoutMs = 30;

        // ID Gains may have to be adjusted based on the responsiveness of control loop. kF: 1023 represents output value to Talon at 100%, 20660 represents Velocity units at 100% output
        public static Gains
            kLeftVelocityGains,
            kRightVelocityGains;
        private static final Gains
            kLeftVelocityGainsV1 = new Gains(0.89077, 0., 0., 1023./20660., 300, 1.00),
            kRightVelocityGainsV1 = new Gains(kLeftVelocityGainsV1),

            kLeftVelocityGainsV2 = new Gains(0.45563, 0., 0., 1023./20660., 300, 1.00),
            kRightVelocityGainsV2 = new Gains(kLeftVelocityGainsV2);

        // ----------------------------------------------------------
        // Max-output modes

        public static double
            kDefaultMaxOutput = 1.;

        public static boolean
            kDefaultUseSlewRateLimiters = false;

        public static class SlewRates {
            public static final double
                kDefaultArcadeForward = 1.58,
                kDefaultArcadeTurn = 2.08,
    
                kDefaultTankForward = 1.0;
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

            kRetractedIntakeRetractorDegree = 0.,
            kExtendedIntakeRetractorDegree = 82.,

            kRetractorDegreeTolerance = 1.5,

            // means that for every 58.25 input ticks, the mechanism outputs 1 tick
            kRetractorTicksReductionRatio = 58.25,

            kRetractorMinDegree = -180.,
            kRetractorMaxDegree = 180.;

        public static class CAN_ID {
            public static final int
                kFeeder = 11,
                kRetractor = 12;
        }

        public static final int kWhiskerSensorDIOPort = 8;

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

        public static Gains
            kRetractorPositionGains;
        private static final Gains
            // TODO: P3 tune V1 retractor gains
            kRetractorPositionGainsV1 = new Gains(0.1, 0., 0., 1023./20660., 300, 1.00),
            kRetractorPositionGainsV2 = new Gains(0.03, 0., 0., 1023./20660., 300, 1.00);
    }

    public static class Manipulator {
        // ----------------------------------------------------------
        // General

        public static final double
            kLauncherTicksReductionRatio = 3.,
            kIndexerTicksReductionRatio = 9.,

            kLauncherMinRPM = -Constants.Falcon500.kMaxRPM,
            kLauncherMaxRPM = Constants.Falcon500.kMaxRPM,

            kIndexerMinRPM = -Constants.Falcon500.kMaxRPM,
            kIndexerMaxRPM = Constants.Falcon500.kMaxRPM;

        public static final int
            kDefaultIndexerRPM = Falcon500.kMaxRPM,
            kDefaultLauncherRPM = Falcon500.kMaxRPM;

        public static class CAN_ID {
            public static final int
                kIndexer = 21,
                kLauncher = 22;
        }

        // ----------------------------------------------------------
        // Closed-loop control

        public static final int
            kIndexerPidIdx = 0,
            kLauncherPidIdx = 0,
            kTimeoutMs = 30;

        public static Gains
            kLauncherRPMGains;
        private static final Gains
            kLauncherRPMGainsV1 = new Gains(0.083708, 0., 0., 1023./20660., 300, 1.00),
            kLauncherRPMGainsV2 = new Gains(0.040753, 0., 0., 1023./20660., 300, 1.00);

        public static Gains
            kIndexerRPMGains;
        private static final Gains
            kIndexerRPMGainsV1 = new Gains(0., 0., 0., 1023./20660., 300, 1.00),
            // TODO: !!!P1!!! Tune V2 indexer RPM gains
            kIndexerRPMGainsV2 = new Gains(0., 0., 0., 1023./20660., 300, 1.00);
    }

    public static class Climber {
        // ----------------------------------------------------------
        // General

        public static final int
            kLeftServoPWMChannel = 0,
            kRightServoPWMChannel = 2;

        public static final double
            kPinOutAngle = 0.,
            kPinInAngle = 50.;
    }


    public static class Autonomous {
        // ----------------------------------------------------------
        // General

        public static boolean
            kDefaultUsePremadeRoutine = true;

        public static double
            kDefaultStartDelaySeconds = 0.,
            kDefaultTarmacLeavingMeters = Constants.inchesToMeters(72.);
    }

    public static class Vision {
        // ----------------------------------------------------------
        // General

        public static boolean
            kDefaultEnableFrontCenterCamera = false,
            kDefaultEnableBackCenterCamera = false;
    }
}