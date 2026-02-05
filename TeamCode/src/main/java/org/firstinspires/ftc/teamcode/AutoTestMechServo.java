package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class AutoTestMechServo extends LinearOpMode {

    // Custom mechanism actions here (servo, non-drive motor, etc)
    public class Servo1 {
        // Pretend the servo is a claw mechanism
        private Servo servo1;
        public Servo1(HardwareMap hardwareMap) {
            servo1 = hardwareMap.get(Servo.class, "servo1");
        }

        public class MoveServo implements Action {
            private double targetPos;
            public MoveServo(double inputPos) {
                // Sets targetPos to input from action call
                this.targetPos = inputPos;
            }
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                // Tell the servo to move
                servo1.setPosition(targetPos);
                return false;
            }
        }
        public Action moveServo(double pos) {
            return new MoveServo(pos);
        }
    }

    @Override
    public void runOpMode() {
        // Set up robot initial position
        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        // Hardware initialization
        Servo1 servo1 = new Servo1(hardwareMap);

        // Adjust if balls are flying too far left/right
        int shootingRotationDeg = 140;
        // Ball line offset
        int xAdjustmentNum = 24;

        // Create the action chain with initial position
        TrajectoryActionBuilder driveChain = drive.actionBuilder(startPose);

        // Chain together the movements
        driveChain = buildInitialMove(driveChain, shootingRotationDeg);
        for (int i = 0; i < 3; i++) {
            driveChain = buildBallCollect(driveChain, servo1, shootingRotationDeg, xAdjustmentNum, i);
        }

        Action mainAuto = driveChain.build();

        waitForStart();

        // Run the action
        Actions.runBlocking(
                mainAuto
        );

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

    private static TrajectoryActionBuilder buildBallCollect(TrajectoryActionBuilder builder, Servo1 servo1, int rot, double adj, int mult) {
        double targetX = -12.0+(adj*mult);
        return builder
                // Move to start of line
                .strafeToLinearHeading(new Vector2d(targetX, 20), Math.toRadians(90))
                // "Open" servo
                .stopAndAdd(servo1.moveServo(1.0))
                // Drives forward to collect balls
                .strafeTo(new Vector2d(targetX, 54.0))
                // "Close" servo
                .stopAndAdd(servo1.moveServo(0.0))
                // Return to shooting position
                .strafeTo(new Vector2d(targetX, 40))
                .splineToSplineHeading(new Pose2d(-20, 20, Math.toRadians(rot)), Math.toRadians(200))
                // Pretend its shooting
                .waitSeconds(1);
    }
}