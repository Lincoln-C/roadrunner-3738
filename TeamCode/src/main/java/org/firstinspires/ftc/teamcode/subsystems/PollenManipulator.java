package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.RobotConstants;

/**
 * Represents a pollen management system of one intake motor.
 * <p>
 * This includes manual control and auto intake which collects the pollen easier
 * <p>
 * This class handles hardware maps, initialization, and math required to manipulate POLLEN.
 * <blockquote>
 *     IT IS POLLEN NOT "BALLS"!
 * </blockquote>
 */
public class PollenManipulator {
    // Initialize hardware variables
    private final DcMotor pollenIntake;

    /**
     * HOLD_STATIC for forward driving (no motion)
     * HOLD_REVERSE for backward driving (slow intake)
     * INTAKING for intaking (fast intake)
     * EJECTING for ejecting (fast outtake)
     */
    public enum State {
        HOLD_STATIC,
        HOLD_REVERSE,
        INTAKING,
        EJECTING
    }

    private State currentState = State.HOLD_STATIC;

    public PollenManipulator(HardwareMap hardwareMap) {
        pollenIntake = hardwareMap.get(DcMotor.class, RobotConstants.MOTOR_INTAKE);
        pollenIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // State setter and getter
    public void setState(State state) {
        this.currentState = state;
    }

    public State getCurrentState() {
        return this.currentState;
    }

    // Set power based on currentState
    public void run() {
        switch (currentState) {
            case INTAKING: pollenIntake.setPower(1.0); break;
            case EJECTING: pollenIntake.setPower(-1.0); break;
            case HOLD_STATIC: pollenIntake.setPower(0.0); break;
            // Hold in pollen while reversing
            case HOLD_REVERSE: pollenIntake.setPower(0.4); break;
        }
    }
}
