package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;

@TeleOp

public class FieldRobotCentricMech extends LinearOpMode {
    
    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotor rightFront;
    private DcMotor leftFront;
    private IMU imu;

	//// Global constants ////

	// Fixes imperfect strafing, adjust as needed
	private static final double STRAFE_MULTIPLIER = 1.1;
	private static final double CONTROLLER_DEADZONE = 0.1;

	// DRIVE MODE: 0 for field-centric, 1 for robot-centric - WHEN IN DOUBT set to 1
	private static final int DRIVE_MODE = 0;

	//// END GLOBAL VARS ////

    @Override
    public void runOpMode() {
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);
        
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		//// Reverse direction as needed ////
		//leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
		//leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
		rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
		rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Loop until game end (driver presses STOP)
        while (opModeIsActive()) {
			telemetry.addData("Status", "Running");
            
			// Get inputs
			double driveX = gamepad1.left_stick_x * STRAFE_MULTIPLIER;
            double driveY = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
			
			// Reset imu direction on button press
            if (gamepad1.options) {
                imu.resetYaw();
            }

			// Drive robot
			if (DRIVE_MODE == 0) {
				driveFieldCentric(driveX, driveY, turn);
			} else {
				driveRobotCentric(driveX, driveY, turn);
			}

			// Update telemetry
			updateDriveTelemetry();
        }
    }

	private void driveFieldCentric(double driveX, double driveY, double turn) {
		if (Math.abs(driveX) < CONTROLLER_DEADZONE) driveX = 0;
		if (Math.abs(driveY) < CONTROLLER_DEADZONE) driveY = 0;
		if (Math.abs(turn) < CONTROLLER_DEADZONE) turn = 0;
		
		double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

		// Do math or something, I don't know
		double rotX = driveX * Math.cos(-botHeading) - driveY * Math.sin(-botHeading);
		double rotY = driveX * Math.sin(-botHeading) + driveY * Math.cos(-botHeading);
		
		double leftFrontPower = rotY + rotX + turn;
		double rightFrontPower = rotY - rotX - turn;
		double leftBackPower = rotY - rotX + turn;
		double rightBackPower = rotY + rotX - turn;
		
		double maxPower = Math.max(Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower)),
		Math.max(Math.abs(leftBackPower), Math.abs(rightBackPower)));
		
		if (maxPower > 1.0) {
			leftFrontPower /= maxPower;
			rightFrontPower /= maxPower;
			leftBackPower /= maxPower;
			rightBackPower /= maxPower;
		}
		
		leftFront.setPower(leftFrontPower);
		rightFront.setPower(rightFrontPower);
		leftBack.setPower(leftBackPower);
		rightBack.setPower(rightBackPower);
	}

	private void driveRobotCentric(double driveX, double driveY, double turn) {
		if (Math.abs(driveX) < CONTROLLER_DEADZONE) driveX = 0;
		if (Math.abs(driveY) < CONTROLLER_DEADZONE) driveY = 0;
		if (Math.abs(turn) < CONTROLLER_DEADZONE) turn = 0;

		double leftFrontPower = driveY + driveX + turn;
		double rightFrontPower = driveY - driveX - turn;
		double leftBackPower = driveY - driveX + turn;
		double rightBackPower = driveY + driveX - turn;
		
		double maxPower = Math.max(Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower)),
		Math.max(Math.abs(leftBackPower), Math.abs(rightBackPower)));
		
		if (maxPower > 1.0) {
			leftFrontPower /= maxPower;
			rightFrontPower /= maxPower;
			leftBackPower /= maxPower;
			rightBackPower /= maxPower;
		}
		
		leftFront.setPower(leftFrontPower);
		rightFront.setPower(rightFrontPower);
		leftBack.setPower(leftBackPower);
		rightBack.setPower(rightBackPower);
	}

	private void updateDriveTelemetry() {
		// Retrieve Rotational Angles and Velocities
		YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
		AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

		telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
		telemetry.addData("Pitch (X)", "%.2f Deg.", orientation.getPitch(AngleUnit.DEGREES));
		telemetry.addData("Roll (Y)", "%.2f Deg.\n", orientation.getRoll(AngleUnit.DEGREES));
		telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", angularVelocity.zRotationRate);
		telemetry.addData("Pitch (X) velocity", "%.2f Deg/Sec", angularVelocity.xRotationRate);
		telemetry.addData("Roll (Y) velocity", "%.2f Deg/Sec", angularVelocity.yRotationRate);
		telemetry.update();
	}
}