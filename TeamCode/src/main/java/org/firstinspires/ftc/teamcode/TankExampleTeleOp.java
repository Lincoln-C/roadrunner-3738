package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.subsystems.TankDriveTrain;

@TeleOp
public class TankExampleTeleOp extends LinearOpMode {
    // The purpose of this file is to control the robot systems with inputs.
    // NO hardware maps should be here, they are ALREADY SET UP.

    // Initializing systems
    private TankDriveTrain driveTrain;

    @Override
    public void runOpMode() {
        // Map drivetrain
        driveTrain = new TankDriveTrain(hardwareMap);

        // This code runs after pushing INIT on driver station
        telemetry.addData("Status", "Running");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double driveY = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

			// Reset imu direction on button press
            if (gamepad1.options) {
                driveTrain.imuResetYaw();
            }

			// Drive robot
            driveTrain.drive(driveY, turn);

            // Update telemetry
            updateDriveTelemetry();
        }
    }

    // Telemetry
    private void updateDriveTelemetry() {
        // Retrieve rotational angles and velocities
        YawPitchRollAngles orientation = driveTrain.getRobotYawPitchRollAngles();
        AngularVelocity angularVelocity = driveTrain.getRobotAngularVelocity();

        telemetry.addData("Left motor power", "%.2f", driveTrain.getLeftPower());
        telemetry.addData("Right motor power", "%.2f", driveTrain.getRightPower());
        telemetry.addLine();
        telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
		telemetry.update();
	}
}