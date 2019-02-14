package org.usfirst.frc2876.DeepSpace2019.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.*;

import org.usfirst.frc2876.DeepSpace2019.commands.HatchDown;
import org.usfirst.frc2876.DeepSpace2019.commands.HatchStop;
import org.usfirst.frc2876.DeepSpace2019.commands.HatchUp;
import org.usfirst.frc2876.DeepSpace2019.utils.TalonSrxEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
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
   
    // private DigitalInput limit;
    private ShuffleboardTab tab;
    private NetworkTableEntry nteLimit;
    private NetworkTableEntry nteMotorOutput;
    private NetworkTableEntry ntePosition;

    private final double maxVelocity = 120.;
    private final double cruiseVelocity = maxVelocity / 2.0;
    private final double acceleration = cruiseVelocity;

    // F-gain = (100% X 1023) / 120 F-gain = 0.1097

    private final double kF = 8.525;

    // Use this to limit how fast we print messages to riolog/console.
    //private int periodicLoopCounter;

    public Hatch() {
        talonSRX8 = new WPI_TalonSRX(8);
        master = talonSRX8;
        encoder = new TalonSrxEncoder(master);
       
        // limit = new DigitalInput(0);

        //periodicLoopCounter = 0;

        // TODO Configure hatch talon
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/Config%20All/src/main/java/frc/robot/Configs.java#L19

        // Set to factory defaults.
        TalonSRXConfiguration allConfigs = new TalonSRXConfiguration();
        master.configAllSettings(allConfigs);
        

        // Setup motion magic

        // zero encoder sensor
        master.setSelectedSensorPosition(0, 0, 30);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new HatchStop());
        // setDefaultCommand(new HatchPosition(30));
    }

    public void setupShuffleboard() {
        // Shuffleboard stuff
        tab = Shuffleboard.getTab("Hatch");

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021941-using-tabs
        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021942-sending-data
        // nteLimit = tab.add("HatchLimit", limit.get()).getEntry();
        nteMotorOutput = tab.add("HatchMotorOutput", master.get()).withSize(5, 3).withPosition(0, 7).getEntry();
        // TODO not sure this will work, does it need to get called in periodic?
        tab.add("HatchEncoder", encoder);
       

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021980-organizing-widgets
        ShuffleboardLayout hatchCommands = tab.getLayout("Commands", BuiltInLayouts.kList)
                .withSize(4, 6)
                .withProperties(Map.of("Label position", "HIDDEN")); // hide labels for commands
        hatchCommands.add(new HatchStop()).withSize(4,1);
        hatchCommands.add(new HatchDown());
        hatchCommands.add(new HatchUp());

        //tab.add(new HatchStop()).withSize(4,3).withProperties(Map.of("Label position", "HIDDEN"));

        //ntePosition = tab.add("Set Position", 1).withWidget("Number Slider").withPosition(1, 1).withSize(2, 1).getEntry();
        ntePosition = tab.add("Set position", 1)
                          //.withWidget("Number Slider")
                          .withWidget(BuiltInWidgets.kNumberSlider)
                          //.withProperties(Map.of(String.valueOf("min"), Double.valueOf(0.), String.valueOf("max"), Double.valueOf(200.)))
                          .withProperties(Map.of("min", 0, "max", 1))
                          //.withSize(2, 1)
                          .getEntry();
                          

    }

    public void hatchUp() {
        master.set(ControlMode.PercentOutput, 0.3);
    }

    public void hatchDown() {
        master.set(ControlMode.PercentOutput, -0.3);
    }

    public void hatchStop() {
        master.set(ControlMode.PercentOutput, 0);
    }

    public void setPosition(double pos) {
        //master.set(ControlMode.MotionMagic, pos);
        System.out.println("slider value: " + pos);
    }

    public double getPosition() {
        return master.getSelectedSensorPosition();
    }

    public void positionIncrement() {
        double cur = getPosition();
        double next = cur + 50;
        setPosition(next);
    }

    public void positionDecrement() {
        double cur = getPosition();
        double next = cur - 50;
        setPosition(next);
    }

    public void dashboardUpdatePosition() {
        double dashValue = ntePosition.getNumber(20).doubleValue();

        double maxVal = 400;
        double maxHalfVal = maxVal / 2;
        double scaledPos = (dashValue * maxHalfVal) + maxHalfVal;

        setPosition(scaledPos);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

      

        nteMotorOutput.setDouble(master.get());
        //dashboardUpdatePosition();
        //setPosition(ntePosition.getNumber(20).doubleValue());

        // TODO Call udpate dashboard here

        // if (periodicLoopCounter % 100 == 0) {
        // System.out.println("hatch limit: " + limit.get());
        // }
        // if (periodicLoopCounter % 1000 == 0) {
        // System.out.println("BOOM");
        // }
        //periodicLoopCounter++;

    }
    // TODO Add an update dashboard method

}
