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
     *  - Add support for multiple intake motors - currently assuming one
     */

    // CONSTANTS
    private static final double INTAKE_MAX_POWER = 1.0;

    // Tracks the current intake speed
    private double motorSpeed = 0;
    private final double motorIncrement = 0.001;

    private final DcMotor intake;

    public SpinningIntake(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");

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
    }
}