package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.constants.RobotConstants;

/**
 * Gets info from robot LimeLight camera and converts to Pose2d that can be used in auton
 * <p>
 * Can also be used in TeleOp at a later time??j
 */
public class VisionCalibration {
    /* TODO:
     *  Set up hardware
     *  Initialize limelight
     *  Config in browser? - Get Jack's help
     *  Convert limelight vector to Pose2d - pose2dOutput
     */

    // Initialize hardware variables
    private final IMU imu;

    /**
     * Sets up the limelight and things using the provided robot hardware info.
     * @param hardwareMap Just say hardwareMap in the parenthesis for it to work.
     */
    public VisionCalibration(HardwareMap hardwareMap) {
        imu = hardwareMap.get(IMU.class, RobotConstants.IMU_NAME);

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RobotConstants.IMU_LOGO_DIRECTION,
                RobotConstants.IMU_USB_DIRECTION));
        imu.initialize(parameters);
    }
}
