package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Represents a two-motor tank drivetrain on a robot.
 * <p>
 * This class handles hardware maps, initialization, and movement math required to drive a robot.
 */
public class TankDrive {
    // CONSTANTS
    private static final double CONTROLLER_DEADZONE = 0.07;

    // Driver station names
    public static final String MOTOR_LEFT = "leftDrive";
    public static final String MOTOR_RIGHT = "rightDrive";

    // Initialize hardware variables
    private final DcMotor leftDrive;
    private final DcMotor rightDrive;

    /**
     * Sets up the motors and stuff using the provided robot hardware info.
     * @param hardwareMap Just say hardwareMap in the parenthesis for it to work.
     */
    public TankDrive(HardwareMap hardwareMap) {
        // Map motors to driver hub names
        leftDrive = hardwareMap.get(DcMotor.class, MOTOR_LEFT);
        rightDrive = hardwareMap.get(DcMotor.class, MOTOR_RIGHT);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // If using encoders, uncomment this for better power input accuracy
        /*leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/

        // Reverse direction as needed
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /**
     * Tells the robot "drive using these stick inputs".
     *
     * @param driveY Forward/back power input
     * @param turn Rotational power input
     */
    public void drive(double driveY, double turn) {
        driveY = applyDeadzone(driveY);
        turn = applyDeadzone(turn);

        double leftPower = driveY + turn;
        double rightPower = driveY - turn;

        double powerClip = Math.max(Math.abs(leftPower), Math.abs(rightPower));

        if (powerClip > 1.0) {
            leftPower /= powerClip;
            rightPower /= powerClip;
        }

        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    private double applyDeadzone(double input) {
        if (Math.abs(input) < CONTROLLER_DEADZONE) {
            return 0;
        } else {
            return input;
        }
    }
}
