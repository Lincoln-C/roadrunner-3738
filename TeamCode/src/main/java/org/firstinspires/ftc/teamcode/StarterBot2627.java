package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.subsystems.PollenManipulator;
import org.firstinspires.ftc.teamcode.subsystems.TankDriveTrain;

@TeleOp
public class StarterBot2627 extends LinearOpMode {
    // Starter bot OpMode for 2026-2027 season

    // Devices and systems
    private TankDriveTrain driveTrain;
    private PollenManipulator pollenManipulator;

    @Override
    public void runOpMode() {
        // Create instances of systems
        driveTrain = new TankDriveTrain(hardwareMap);
        pollenManipulator = new PollenManipulator(hardwareMap);

        telemetry.addData("Status", "Running");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Set drive inputs
            double driveY = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            // Switch pollen manipulator modes
            if (gamepad1.a) { pollenManipulator.setState(PollenManipulator.State.INTAKING); }
            else if (gamepad1.b) { pollenManipulator.setState(PollenManipulator.State.EJECTING); }
            else if (driveY < -0.2) { pollenManipulator.setState(PollenManipulator.State.HOLD_REVERSE); }
            else { pollenManipulator.setState(PollenManipulator.State.HOLD_STATIC); }

            // Reset imu direction on button press
            if (gamepad1.options) {
                driveTrain.imuResetYaw();
            }

            // Start system loops
            driveTrain.drive(driveY, turn);
            pollenManipulator.run();

            // Telemetry
            updateDriveTelemetry();
        }
    }

    // Telemetry
    private void updateDriveTelemetry() {
        // Retrieve rotational angles and velocities
        YawPitchRollAngles orientation = driveTrain.getRobotYawPitchRollAngles();
        AngularVelocity angularVelocity = driveTrain.getRobotAngularVelocity();

        telemetry.addLine("a for intake, b for outtake, release for containment");
        telemetry.addData("Current intake state", pollenManipulator.getCurrentState());
        telemetry.addLine();
        telemetry.addData("Left motor power", "%.2f", driveTrain.getLeftPower());
        telemetry.addData("Right motor power", "%.2f", driveTrain.getRightPower());
        telemetry.addLine();
        telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
        telemetry.update();
    }
}
