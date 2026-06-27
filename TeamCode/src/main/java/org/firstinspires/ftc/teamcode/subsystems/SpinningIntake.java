package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Represents a rotary launcher on a robot.
 * <p>
 * This class handles hardware maps, initialization, and logic to control an intake on a robot.
 */
public class SpinningIntake {
    /*
     * TODO: Fix code skeleton
     *  - Add support for multiple intake motors - currently assuming one (in progress)
     */

    // CONSTANTS
    private static final double INTAKE_MAX_POWER = 1.0;
    // Driver station names
    private static final String MOTOR_INTAKE = "intake";
    private static final String MOTOR_INTAKE_2 = "intake 2";

    // Tracks the current intake speed
    private double motorSpeed = 0;
    private final double motorIncrement = 0.001;

    private final DcMotor intake;
    private final DcMotor intake2;
    private boolean ignore_intake2 = false;

    public SpinningIntake(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, MOTOR_INTAKE);
        try {
            intake2 = hardwareMap.get(DcMotor.class, MOTOR_INTAKE_2);
            intake2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            ignore_intake2 = true;
        }

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public double getMotorPower() {
        return motorSpeed;
    }

    public double incrementSpeed() {
        motorSpeed += motorIncrement;
        if (motorSpeed > INTAKE_MAX_POWER) {
            motorSpeed = INTAKE_MAX_POWER;
        }

        updateMotorPower();

        return motorSpeed;
    }

    public double decrementSpeed()
    {
        motorSpeed -= motorIncrement;

        if (motorSpeed < 0) {
            motorSpeed = 0;
        }

        updateMotorPower();

        return motorSpeed;
    }

    // INTERNAL METHODS
    private void updateMotorPower() {
        intake.setPower(motorSpeed);
        if (!ignore_intake2) {
            intake.setPower(-motorSpeed);
        }
    }
}
