package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
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

        Action initialMove = drive.actionBuilder(startPose)
                .strafeToSplineHeading(new Vector2d(-53, 48), Math.toRadians(310))
                .strafeToLinearHeading(new Vector2d(-20, 20), Math.toRadians(135))
                .waitSeconds(1)
                .build();

        int xAdjustmentNum = 24;
        int xAdjustMult = 0;

        Action ballCollect1 = drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(135)))
                .strafeToLinearHeading(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 20), Math.toRadians(90))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 54.0))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 40))
                .splineToSplineHeading(new Pose2d(-20, 20, Math.toRadians(140)), Math.toRadians(200))
                .waitSeconds(1)
                .build();

        xAdjustMult = 1;

        Action ballCollect2 = drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(135)))
                .strafeToLinearHeading(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 20), Math.toRadians(90))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 54.0))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 40))
                .splineToSplineHeading(new Pose2d(-20, 20, Math.toRadians(140)), Math.toRadians(200))
                .waitSeconds(1)
                .build();

        xAdjustMult = 2;

        Action ballCollect3 = drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(135)))
                .strafeToLinearHeading(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 20), Math.toRadians(90))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 54.0))
                .strafeTo(new Vector2d(-12.0 + xAdjustmentNum*xAdjustMult, 40))
                .splineToSplineHeading(new Pose2d(-20, 20, Math.toRadians(140)), Math.toRadians(200))
                .waitSeconds(1)
                .build();

        Action fullAuto = new SequentialAction(
                initialMove,
                ballCollect1,
                ballCollect2,
                ballCollect3
        );

        myBot.runAction(fullAuto);

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}