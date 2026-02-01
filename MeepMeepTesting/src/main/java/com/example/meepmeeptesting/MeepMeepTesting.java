package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(80, 50, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        DriveShim drive = myBot.getDrive();
        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(90));

        // Adjust if balls are flying too far left/right
        int shootingRotationDeg = 140;
        // Ball line offset
        int xAdjustmentNum = 24;

        // Good for complete actions, not chaining!!!
        /*Action fullAuto = new SequentialAction(
                buildInitialMove(drive, startPose, shootingRotationDeg),
                buildBallCollect(drive, shootingRotationDeg, xAdjustmentNum, 0),
                buildBallCollect(drive, shootingRotationDeg, xAdjustmentNum, 1),
                buildBallCollect(drive, shootingRotationDeg, xAdjustmentNum, 2)
        );*/

        // Create the action chain with initial position
        TrajectoryActionBuilder driveChain = drive.actionBuilder(startPose);

        // Chain together the movements
        driveChain = buildInitialMove(driveChain, shootingRotationDeg);
        for (int i = 0; i < 3; i++) {
            driveChain = buildBallCollect(driveChain, shootingRotationDeg, xAdjustmentNum, i);
        }

        myBot.runAction(driveChain.build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

    // Abstracted methods
    private static TrajectoryActionBuilder buildInitialMove(TrajectoryActionBuilder builder, int rot) {
        return builder
                .strafeToSplineHeading(new Vector2d(-53, 48), Math.toRadians(310))
                .strafeToLinearHeading(new Vector2d(-20, 20), Math.toRadians(rot))
                .waitSeconds(1);
    }

    private static TrajectoryActionBuilder buildBallCollect(TrajectoryActionBuilder builder, int rot, double adj, int mult) {
        double targetX = -12.0 + (adj * mult);
        return builder
                .strafeToLinearHeading(new Vector2d(targetX, 20), Math.toRadians(90))
                .strafeTo(new Vector2d(targetX, 54.0))
                .strafeTo(new Vector2d(targetX, 40))
                .splineToSplineHeading(new Pose2d(-20, 20, Math.toRadians(rot)), Math.toRadians(200))
                .waitSeconds(1);
    }
}