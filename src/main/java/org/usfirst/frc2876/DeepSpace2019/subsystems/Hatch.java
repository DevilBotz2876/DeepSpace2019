package org.usfirst.frc2876.DeepSpace2019.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.usfirst.frc2876.DeepSpace2019.commands.HatchStop;
import org.usfirst.frc2876.DeepSpace2019.commands.HatchUp;
import org.usfirst.frc2876.DeepSpace2019.commands.HatchDown;

import org.usfirst.frc2876.DeepSpace2019.utils.TalonSrxEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 *
 */
public class Hatch extends Subsystem {
    private WPI_TalonSRX talonSRX8;
    private WPI_TalonSRX master;
    private TalonSrxEncoder encoder;
    private DigitalInput limit;
    private ShuffleboardTab tab;
    private NetworkTableEntry nteLimit;
    private NetworkTableEntry nteMotorOutput;

    // Use this to limit how fast we print messages to riolog/console.
    int periodicLoopCounter;

    public Hatch() {
        talonSRX8 = new WPI_TalonSRX(8);
        master = talonSRX8;
        encoder = new TalonSrxEncoder(master);
        limit = new DigitalInput(0);

        periodicLoopCounter = 0;

        // TODO Configure hatch talon
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/Config%20All/src/main/java/frc/robot/Configs.java#L19
        TalonSRXConfiguration allConfigs = new TalonSRXConfiguration();
        master.configAllSettings(allConfigs);

        
    }

    @Override
    public void initDefaultCommand() {
        // TODO Set the default command for a subsystem here.
        setDefaultCommand(new HatchStop());
    }

    public void setupShuffleboard() {
        // Shuffleboard stuff
        tab = Shuffleboard.getTab("Hatch");

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021941-using-tabs
        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021942-sending-data
        nteLimit = tab.add("HatchLimit", limit.get()).getEntry();
        nteMotorOutput = tab.add("HatchMotorOutput", master.get()).getEntry();
        // TODO not sure this will work, does it need to get called in periodic?
        tab.add("HatchEncoder", encoder);

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021980-organizing-widgets
        ShuffleboardLayout hatchCommands = tab.getLayout("Commands", BuiltInLayouts.kList).withSize(2, 2)
                .withProperties(Map.of("Label position", "HIDDEN")); // hide labels for commands
        hatchCommands.add(new HatchStop());
        hatchCommands.add(new HatchDown());
        hatchCommands.add(new HatchUp());
    }

    public void hatchUp() {
        master.set(0.5);
    }

    public void hatchDown() {
        master.set(-0.5);
    }

    public void hatchStop() {
        master.set(0);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        nteLimit.setBoolean(limit.get());
        nteMotorOutput.setDouble(master.get());

        // TODO Call udpate dashboard here

        if (periodicLoopCounter % 100 == 0) {
            System.out.println("hatch limit: " + limit.get());
        }
        if (periodicLoopCounter % 1000 == 0) {
            System.out.println("BOOM");
        }
        periodicLoopCounter++;

    }
    // TODO Add an update dashboard method

}
