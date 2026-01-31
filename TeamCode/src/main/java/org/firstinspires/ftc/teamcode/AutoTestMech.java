package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class AutoTestMech extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Set up robot initial position
        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        int shootingRotationDeg = 140;

        Action initialMove = drive.actionBuilder(startPose)
                .strafeToSplineHeading(new Vector2d(-53, 48), Math.toRadians(310))
                .strafeToLinearHeading(new Vector2d(-20, 20), Math.toRadians(shootingRotationDeg))
                .waitSeconds(1)
                .build();

        int xAdjustmentNum = 24;
        int xAdjustMult = 0;

        Action ballCollect1 = drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(shootingRotationDeg)))
                .strafeToLinearHeading(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 20), Math.toRadians(90))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 54.0))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 40))
                .splineToSplineHeading(new Pose2d(-20, 20, Math.toRadians(shootingRotationDeg)), Math.toRadians(200))
                .waitSeconds(1)
                .build();

        xAdjustMult = 1;

        Action ballCollect2 = drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(shootingRotationDeg)))
                .strafeToLinearHeading(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 20), Math.toRadians(90))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 54.0))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 40))
                .splineToSplineHeading(new Pose2d(-20, 20, Math.toRadians(shootingRotationDeg)), Math.toRadians(200))
                .waitSeconds(1)
                .build();

        xAdjustMult = 2;

        Action ballCollect3 = drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(shootingRotationDeg)))
                .strafeToLinearHeading(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 20), Math.toRadians(90))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 54.0))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 40))
                .splineToSplineHeading(new Pose2d(-20, 20, Math.toRadians(shootingRotationDeg)), Math.toRadians(200))
                .waitSeconds(1)
                .build();

        Action fullAuto = new SequentialAction(
                initialMove,
                ballCollect1,
                ballCollect2,
                ballCollect3
        );

        waitForStart();

        // Run the action
        Actions.runBlocking(fullAuto);

        while (opModeIsActive()) {
            telemetry.update();
        }
    }
}