package org.usfirst.frc2876.DeepSpace2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.usfirst.frc2876.DeepSpace2019.commands.XboxDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * 
 */
public class DriveTrain extends Subsystem {

    private WPI_TalonSRX talonSRX4;
    private WPI_TalonSRX talonSRX3;
    private WPI_TalonSRX talonSRX2;
    private WPI_TalonSRX talonSRX1;
    private DifferentialDrive differentialDrive;

    private WPI_TalonSRX rightMaster;
    private WPI_TalonSRX leftMaster;
    private WPI_TalonSRX rightFollower;
    private WPI_TalonSRX leftFollower;

    // Calculated this following instructions here:
    // https://phoenix-documentation.readthedocs.io/en/latest/ch14_MCSensor.html#confirm-sensor-resolution-velocity
    //
    // Right Forward 3300
    // Right Reverse 3200
    // Left Forward 3200
    // Left Reverse 3300
    // (100% X 1023) / 3200 = .3197
    //
    private final double kF = .3197f;
    private final double MAX_RPM = 3380.0f;
 
    // TODO Declare navx

    public DriveTrain() {
        talonSRX4 = new WPI_TalonSRX(4);
        talonSRX3 = new WPI_TalonSRX(3);
        talonSRX2 = new WPI_TalonSRX(2);
        talonSRX1 = new WPI_TalonSRX(1);

        rightMaster = talonSRX1;
        leftMaster = talonSRX3;
        rightFollower = talonSRX2;
        leftFollower = talonSRX4;

        // https://phoenix-documentation.readthedocs.io/en/latest/ch13_MC.html#follower
        rightFollower.follow(rightMaster);
        leftFollower.follow(leftMaster);

        // https://phoenix-documentation.readthedocs.io/en/latest/ch13_MC.html#inverts
        rightMaster.setInverted(true);
        rightFollower.setInverted(InvertType.FollowMaster);
        leftMaster.setInverted(true);
        leftFollower.setInverted(InvertType.FollowMaster);

        // https://phoenix-documentation.readthedocs.io/en/latest/ch14_MCSensor.html#sensor-phase
        leftMaster.setSensorPhase(false);
        rightMaster.setSensorPhase(true);

        differentialDrive = new DifferentialDrive(leftMaster, rightMaster);

        differentialDrive.setSafetyEnabled(false);
        // differentialDrive.setExpiration(0.1);
        differentialDrive.setMaxOutput(1.0);

        // TODO configure pid on talons and stuffs. Example how to use
        // configAllSettings:
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/Config%20All/src/main/java/frc/robot/Configs.java#L19
        TalonSRXConfiguration allConfigs = new TalonSRXConfiguration();
        // rightMaster.configAllSettings(allConfigs);

        // TODO initialize navx variable

    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new XboxDrive());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        // TODO Call udpate dashboard here

    }
    // TODO Add an update dashboard method

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    // TODO Add adjustSpeed method to control sensitivity of joystick -> drive
    // output.

    public void arcadeDrive(double xSpeed, double zRotation) {
        differentialDrive.arcadeDrive(xSpeed, zRotation);
    }

    public void tankDrive(double leftValue, double rightValue) {
        differentialDrive.tankDrive(leftValue, rightValue);
    }

    public void velocityTankDrive(double leftValue, double rightValue) {
        leftMaster.set(ControlMode.Velocity, leftValue*MAX_RPM);
        rightMaster.set(ControlMode.Velocity, rightValue*MAX_RPM);
    }
}
