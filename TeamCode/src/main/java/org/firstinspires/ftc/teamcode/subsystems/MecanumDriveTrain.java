package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class MecanumDriveTrain {
    // Enum is a nice way to select a robot drive mode
    enum DriveMode {
        FIELD_CENTRIC,
        ROBOT_CENTRIC;

        public DriveMode switchMode() {
            return this == FIELD_CENTRIC ? ROBOT_CENTRIC : FIELD_CENTRIC;
        }
    }

    //// CONSTANTS ////
    private static final double STRAFE_MULTIPLIER = 1.1;
	private static final double CONTROLLER_DEADZONE = 0.07;
    //// END CONSTANTS ////

    // Set initial drive mode here
    DriveMode driveMode = DriveMode.FIELD_CENTRIC;

    // Initialize hardware variables
    private final DcMotor leftBack;
    private final DcMotor rightBack;
    private final DcMotor rightFront;
    private final DcMotor leftFront;
    private final IMU imu;

    // This is a constructor, it gets the stuff from outside so it can do stuff inside this file.
    // Yes I know that is a bad explanation.
    public MecanumDriveTrain(HardwareMap hardwareMap) {
        // Map motors to driver hub names
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

        // If the encoders are present, uncomment this
		/*leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/

		//// Reverse direction as needed ////
		leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
		leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
		//rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
		//rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    // Now that the constructor is set up. we code the methods.
    // These blocks of code serve as custom "shortcuts" to make it easier for people to code.
    /* YOU CAN:
     * Reset the IMU position to set the front of the robot
     * Switch the drive mode
     * Get the current drive mode
     * Make the robot drive
     * AND NOTHING ELSE.
     * How simple is that???
     */
    public void imuResetYaw() {
        imu.resetYaw();
    }

    public void toggleDriveMode() {
        driveMode = driveMode.switchMode();
    }

    public String getDriveMode() {
        if (this.driveMode == DriveMode.FIELD_CENTRIC) {
            return "Field-Centric";
        } else {
            return "Robot-Centric";
        }
    }

    // Drive method
    public void drive(double driveX, double driveY, double turn) {
        if (driveMode == DriveMode.FIELD_CENTRIC) {
            driveFieldCentric(driveX, driveY, turn);
        } else {
            driveRobotCentric(driveX, driveY, turn);
        }
    }

    // IMU telemetry
    public YawPitchRollAngles getRobotYawPitchRollAngles() {
        return imu.getRobotYawPitchRollAngles();
    }

    public AngularVelocity getRobotAngularVelocity() {
        return imu.getRobotAngularVelocity(AngleUnit.DEGREES);
    }

    //// INTERNAL METHODS ////
    // These are inaccessible, you cannot call them manually from the TeleOp or Auton.
    // These are for organizational purposes.
    private void driveFieldCentric(double driveX, double driveY, double turn) {
        driveX = applyDeadzone(driveX);
        driveY = applyDeadzone(driveY);
        turn = applyDeadzone(turn);

        driveX *= STRAFE_MULTIPLIER;

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Do math or something, I don't know
        double rotX = driveX * Math.cos(-botHeading) - driveY * Math.sin(-botHeading);
        double rotY = driveX * Math.sin(-botHeading) + driveY * Math.cos(-botHeading);

        setMotorOutputs(rotX, rotY, turn);
    }

    private void driveRobotCentric(double driveX, double driveY, double turn) {
        driveX = applyDeadzone(driveX);
        driveY = applyDeadzone(driveY);
        turn = applyDeadzone(turn);

        driveX *= STRAFE_MULTIPLIER;

        setMotorOutputs(driveX, driveY, turn);
    }

    private void setMotorOutputs(double driveX, double driveY, double turn) {
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

    private double applyDeadzone(double input) {
        if (Math.abs(input) < CONTROLLER_DEADZONE) {
            return 0;
        } else {
            return input;
        }
    }
}