package org.usfirst.frc2876.DeepSpace2019.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.usfirst.frc2876.DeepSpace2019.commands.ScoopStop;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Scoop extends Subsystem {

    private WPI_TalonSRX talonSRX7;
    private WPI_TalonSRX master;

    // TODO define limit switch if plugged into roborio. If not need to config talon
    // to use it? Or figure out how to read if switch is triggered.

    public Scoop() {
        talonSRX7 = new WPI_TalonSRX(7);
        master = talonSRX7;
        // TODO Configure talon
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/Config%20All/src/main/java/frc/robot/Configs.java#L19
        TalonSRXConfiguration allConfigs = new TalonSRXConfiguration();
        master.configAllSettings(allConfigs);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ScoopStop());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop


    }

    public void scoopIn() {
        master.set(0.5);
    }

    public void scoopOut() {
        master.set(-0.5);
    }

    public void scoopStop() {
        master.set(0);
    }

}
