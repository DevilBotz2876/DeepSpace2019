package org.usfirst.frc2876.DeepSpace2019.subsystems;

import java.util.Map;

import org.usfirst.frc2876.DeepSpace2019.Robot;
import org.usfirst.frc2876.DeepSpace2019.commands.PixyDriveAssist;
import org.usfirst.frc2876.DeepSpace2019.commands.PixyLine;
import org.usfirst.frc2876.DeepSpace2019.utils.PixyLinePID;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 *
 */
public class Vision extends Subsystem {
    // public Pixy2 pixyHatch;
    public PixyLinePID pixyHatch;
    public PixyLinePID pixyScoop;

    private int periodicLoopCounter;
    // public PIDController lineController;
    // PixySource pixySource;
    // PixyOutput pixyOutput;

    private ShuffleboardTab tab;
    private NetworkTableEntry nteVectors;
    private NetworkTableEntry ntePidError;
    private NetworkTableEntry ntePidSetpoint;
    private NetworkTableEntry ntePidOutput;
    private NetworkTableEntry nteSpeed;
    private NetworkTableEntry nteRotate;
    private NetworkTableEntry nteIsPixyAlive;
    private NetworkTableEntry nteLinePID;
    private NetworkTableEntry nteIsVectorFound;

    public Vision() {
        pixyHatch = new PixyLinePID("Hatch", (byte) 0x54);

        // If you want to disable 2nd pixy facing the cargo/scoop side comment out line
        // that allocates new PixyLinePID and uncomment following line. This will make
        // pixyScoop same as pixyHatch w/o having to change any other code.
        //
        // pixyScoop = pixyHatch
        
        pixyScoop = new PixyLinePID("Scoop", (byte) 0x34);

    }

    @Override
    public void initDefaultCommand() {

    }

    public void setupShuffleboard() {
        // Shuffleboard stuff
        tab = Shuffleboard.getTab("Vision");

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021941-using-tabs
        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021942-sending-data

        nteIsPixyAlive = tab.add("IsPixyAlive", false).withSize(6, 6).withPosition(0, 0).getEntry();

        ShuffleboardLayout commands = tab.getLayout("Commands", BuiltInLayouts.kList).withSize(7, 6).withPosition(6, 0)
                .withProperties(Map.of("Label position", "HIDDEN")); // hide labels for commands
        commands.add(new PixyLine());
        commands.add(new PixyDriveAssist());

        nteIsVectorFound = tab.add("IsVectorFound", false).withSize(6, 6).withPosition(12, 0).getEntry();

        nteVectors = tab.add("Vectors", currentPixy().vectorStrings()).withSize(12, 6).withPosition(18, 0).getEntry();

        int row2Y = 7;
        int boxYSize = 3;
        int boxXSize = 6;
        int row3Y = row2Y + boxYSize;
        int row4Y = row3Y + boxYSize;
        ntePidError = tab.add("LinePIDError", currentPixy().lineController.getError()).withSize(boxXSize, boxYSize)
                .withPosition(0, row2Y).getEntry();
        ntePidSetpoint = tab.add("LinePIDSetpoint", currentPixy().lineController.getSetpoint())
                .withSize(boxXSize, boxYSize).withPosition(0, row3Y).getEntry();
        ntePidOutput = tab.add("LinePIDOutput", currentPixy().lineController.get()).withSize(boxXSize, boxYSize)
                .withPosition(0, row4Y).getEntry();

        nteSpeed = tab.add("DTSpeed", 0).withSize(boxXSize, boxYSize).withPosition(boxXSize, row2Y).getEntry();
        nteRotate = tab.add("DTRotate", 0).withSize(boxXSize, boxYSize).withPosition(boxXSize, row3Y).getEntry();

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021980-organizing-widgets

    }

    public void updateShuffleDrivetrainOutputs(double speed, double rotate) {
        nteSpeed.setDouble(speed);
        nteRotate.setDouble(rotate);
    }

    public PixyLinePID currentPixy() {
        if (Robot.driveTrain.isHatchForward()) {
            return pixyHatch;
        }
        return pixyScoop;
    }

    public boolean isPixyAlive() {
        return currentPixy().isPixyAlive();
        // return true;
    }

    public boolean isVectorPresent() {
        if (currentPixy().lineController.isEnabled()) {
            return currentPixy().isVectorPresent();
        }
        return false;
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        if (currentPixy().lineController.isEnabled()) {
            nteIsVectorFound.setBoolean(isVectorPresent());
            nteVectors.setString(currentPixy().vectorStrings());
            ntePidError.setDouble(currentPixy().lineController.getError());
            ntePidSetpoint.setDouble(currentPixy().lineController.getSetpoint());
            ntePidOutput.setDouble(currentPixy().lineController.get());
        } else {
            if (periodicLoopCounter % 500 == 0) {
                boolean b = isPixyAlive();
                nteIsPixyAlive.setBoolean(b);
                System.out.println(currentPixy().toString() + " isAlive " + b);
            }
        }
        periodicLoopCounter++;

    }

}
