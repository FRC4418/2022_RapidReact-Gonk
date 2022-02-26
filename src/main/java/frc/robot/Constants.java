package frc.robot;


public class Constants {
    public static class Falcon500 {
        public static int ticksPerRevolution = 2048;

        // AKA the free RPM
        public static int maxRPM = 6380;
    }

    public static class Drivetrain {
        // ----------------------------------------------------------
        // General

        public static final double
            // kWheelDiameterMeters = Conversion.inchesToMeters(6.d),
            kWheelDiameterMetersV2 = Conversion.inchesToMeters(4.d);

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
            // kTicksToMeters  = (kWheelDiameterMetersV1 * Math.PI) / ((double) Falcon500.ticksPerRevolution) / 7.33d,
            kTicksToMetersV2  = (kWheelDiameterMetersV2 * Math.PI) / ((double) Falcon500.ticksPerRevolution) / 7.75d,
            // kMPSToTicksPer100ms = ((double) Falcon500.ticksPerRevolution) / (10.d * kWheelDiameterMeters * Math.PI),
            kMPSToTicksPer100msV2 = ((double) Falcon500.ticksPerRevolution) / (10.d * kWheelDiameterMetersV2 * Math.PI);

        // ----------------------------------------------------------
        // Kinematics

        // horizontal distance between the left and right-side wheels
        // private static final double kTrackWidthMeters = Conversion.inchesToMeters(24.6d);
        public static final double kTrackWidthMetersV2 = Conversion.inchesToMeters(24.d);

        // ----------------------------------------------------------
        // Open-loop control

        public static final double
            // units in seconds
            kJoystickOpenLoopRampTime = 0.7d;

        // ----------------------------------------------------------
        // Closed-loop control

        public static final double
            // ksVolts = 0.63599d,
            ksVoltsV2 = 0.69552d,
            // kvVoltSecondsPerMeter = 0.043021d,
            kvVoltSecondsPerMeterV2 = 0.066546d,
            // kaVoltSecondsSquaredPerMeter = 0.018985d,
            kaVoltSecondsSquaredPerMeterV2 = 0.010455d;

        public static final int
            kLeftPidIdx = 0,	// PID slots are basically different contorl-loop types (closed-loop, open-loop, etc)
            kLeftSlotIdx = 0,	// slots are basically different motor control types

            kRightPidIdx = 0,
            kRightSlotIdx = 0,
            // Set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails.
            kTimeoutMs = 30;

        // ID Gains may have to be adjusted based on the responsiveness of control loop. kF: 1023 represents output value to Talon at 100%, 20660 represents Velocity units at 100% output
        // public static final Gains
        //     kLeftVelocityGains = new Gains(0.89077d, 0.d, 0.d, 1023.d/20660.d, 300, 1.00d),
        //     kRightVelocityGains = new Gains(kLeftMotorVelocityGains);
        public static final Gains
            kLeftVelocityGainsV2 = new Gains(0.45563d, 0.d, 0.d, 1023.d/20660.d, 300, 1.00d),
            kRightVelocityGainsV2 = new Gains(kLeftVelocityGainsV2);

        // ----------------------------------------------------------
        // Max-output modes

        public static final double kMaxSlewRateAllowed = 3.d;

        public static class NormalOutputMode {
            public static final double kDefaultMaxOutput = 1.d;

            public static class SlewRates {
                public static final double
                    kDefaultArcadeForward = 1.58d,
                    kDefaultArcadeTurn = 2.08d,
        
                    kDefaultTankForward = 1.0d;
            }
        }

        public static class KidsSafetyOutputMode {
            public static final double kDefaultMaxOutput = 0.1d;

            public static class SlewRates {
                public static final double
                    kDefaultArcadeForward = 2.d,
                    kDefaultArcadeTurn = 2.d,

                    kDefaultTankForward = 2.d;
            }
        }

        // ----------------------------------------------------------
        // Trajectory

        public static final double
            kMaxSpeedMetersPerSecond = 3.d,
            kMaxAccelerationMetersPerSecondSquared = 3.d;

        public static final double
            kRamseteB = 2,
            kRamseteZeta = 0.7;
    }
}