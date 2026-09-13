package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
 * TODO:
 *  Update to use tank drive instead of mecanum
 *  Update tuning - entirely new config
 *  Test correct path
 *  Implement servo actions
 *  Implement limelight calibration??
 */

@Autonomous
public class AutoRRExperimental extends LinearOpMode {

    // Custom mechanism actions here (servo, non-drive motor, etc)


    @Override
    public void runOpMode() {
        // Set up robot initial position
        // Switch to limelight eventually
        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        // Hardware initialization

        // Action thing here

        waitForStart();

        // Run the action
        //Actions.runBlocking(action);

        while (opModeIsActive()) {
            telemetry.update();
        }
    }
}