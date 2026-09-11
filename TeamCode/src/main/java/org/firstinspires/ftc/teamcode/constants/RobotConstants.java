package org.firstinspires.ftc.teamcode.constants;

/**
 * All robot constant values go here
 * <p>
 * Examples include robot drive speed multipliers,
 * motor names, or anything else that doesn't change
 */
public class RobotConstants {
    // Private empty constructor because this class doesn't need multiple instances
    private RobotConstants() {}

    // ---------- TANK DRIVE ----------
    public static final String MOTOR_TANK_LEFT = "leftDrive";
    public static final String MOTOR_TANK_RIGHT = "rightDrive";

    // ---------- MECANUM DRIVE ----------
    public static final String MOTOR_LEFT_BACK = "leftBack";
    public static final String MOTOR_RIGHT_BACK = "rightBack";
    public static final String MOTOR_RIGHT_FRONT = "rightFront";
    public static final String MOTOR_LEFT_FRONT = "leftFront";

    // ---------- ROBOT MISC ELECTRONICS ----------
    public static final String IMU_NAME = "imu";
    public static final String MOTOR_INTAKE = "intake";

        // limelight?

    // ---------- GAMEPAD ----------
    public static final double CONTROLLER_DEADZONE = 0.07;


    // ---------- ROBOT SPEEDS ----------
    public static final double STRAFE_MULTIPLIER = 1.1;
    public static final double TURN_MULTIPLIER = 0.7;

}
