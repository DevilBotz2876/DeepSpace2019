package org.usfirst.frc2876.DeepSpace2019.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hatch extends Subsystem {
    private WPI_TalonSRX talonSRX8;
    private final WPI_TalonSRX master;

    // TODO define limit switch if plugged into roborio. If plugged into talon
    // figure out how to configure talon to use it. Ask electrical for help.
    // If plugged into roborio ask yourself what kind of sensor this is.. analog or
    // digital. Then ask yourself are we getting data or sending data. That will
    // help you figure out what type of class to use as type. Choices are Input or
    // Output, prefixed by Digital or Analog depending where sensor is connected.
    //
    // X limit;

    // Use this to limit how fast we print messages to riolog/console.
    int periodicLoopCounter;

    public Hatch() {
        talonSRX8 = new WPI_TalonSRX(8);
        master = talonSRX8;
        
        // limit = new X

        periodicLoopCounter = 0;

        // TODO Configure hatch talon
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/Config%20All/src/main/java/frc/robot/Configs.java#L19
        TalonSRXConfiguration allConfigs = new TalonSRXConfiguration();
        master.configAllSettings(allConfigs);

    }

    @Override
    public void initDefaultCommand() {
        // TODO Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        // TODO Call udpate dashboard here

        // if (periodicLoopCounter % 10 == 0) {
        // System.out.println("hatch limit: " + limit.get());
        // }
        // if (periodicLoopCounter % 100 == 0) {
        // System.out.println("BOOM");
        // }
        periodicLoopCounter++;

    }
    // TODO Add an update dashboard method

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}
