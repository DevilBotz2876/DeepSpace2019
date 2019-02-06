package org.usfirst.frc2876.DeepSpace2019.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Arm extends Subsystem {

    private WPI_TalonSRX talonSRX5;
    private WPI_TalonSRX talonSRX6;

    private WPI_TalonSRX master;
    private WPI_TalonSRX follower;

    public Arm() {
        talonSRX5 = new WPI_TalonSRX(5);
        talonSRX6 = new WPI_TalonSRX(6);

        // TODO Init these to the correct talonSRXnum

        // TODO configure talons and stuffs. Let's try motion magic to control arm
        // position. If it doesn't work out we can just use position pid on talon.
        //
        // https://phoenix-documentation.readthedocs.io/en/latest/ch16_ClosedLoop.html#motion-magic-position-velocity-current-closed-loop-closed-loop
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/MotionMagic/src/main/java/frc/robot/Robot.java

        // Example how to use configAllSettings:
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/Config%20All/src/main/java/frc/robot/Configs.java#L19
        TalonSRXConfiguration allConfigs = new TalonSRXConfiguration();
        // master.configAllSettings(allConfigs);
        // follower.configAllSettings(allConfigs);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        // TODO Call udpate dashboard here

    }
    // TODO Add an update dashboard method

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}
