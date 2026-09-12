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

        // Adjust if balls are flying too far left/right
        int shootingRotationDeg = 140;
        // Ball line offset
        int xAdjustmentNum = 24;

        // Create the action chain with initial position
        TrajectoryActionBuilder driveChain = drive.actionBuilder(startPose);

        // Chain together the movements
        driveChain = buildInitialMove(driveChain, shootingRotationDeg);
        for (int i = 0; i < 3; i++) {
            driveChain = buildBallCollect(driveChain, shootingRotationDeg, xAdjustmentNum, i);
        }

        waitForStart();

        // Run the action
        Actions.runBlocking(driveChain.build());

        while (opModeIsActive()) {
            telemetry.update();
        }
    }

    // Abstracted methods
    private static TrajectoryActionBuilder buildInitialMove(TrajectoryActionBuilder builder, int rot) {
        return builder
                .strafeToSplineHeading(new Vector2d(-53, 48), Math.toRadians(310))
                .strafeToLinearHeading(new Vector2d(-20, 20), Math.toRadians(rot))
                .waitSeconds(1);
    }

    private static TrajectoryActionBuilder buildBallCollect(TrajectoryActionBuilder builder, int rot, double adj, int mult) {
        double targetX = -12.0+(adj*mult);
        return builder
                .strafeToLinearHeading(new Vector2d(targetX, 20), Math.toRadians(90))
                .strafeTo(new Vector2d(targetX, 54.0))
                .strafeTo(new Vector2d(targetX, 40))
                .splineToSplineHeading(new Pose2d(-20, 20, Math.toRadians(rot)), Math.toRadians(200))
                .waitSeconds(1);
    }
}