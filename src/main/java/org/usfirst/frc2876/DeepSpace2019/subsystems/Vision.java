package org.usfirst.frc2876.DeepSpace2019.subsystems;

import java.util.Map;

import org.usfirst.frc2876.DeepSpace2019.Robot;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2Exception;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2Vector;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2Version;
import org.usfirst.frc2876.DeepSpace2019.commands.PixyLine;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Vision extends Subsystem {
    public Pixy2 pixyHatch;
    private int periodicLoopCounter;
    public PIDController lineController;
    PixySource pixySource;
    PixyOutput pixyOutput;

    private ShuffleboardTab tab;
    private NetworkTableEntry nteVectors;
    private NetworkTableEntry ntePidError;
    private NetworkTableEntry ntePidSetpoint;
    private NetworkTableEntry ntePidOutput;
    private NetworkTableEntry nteDriveTrainRight;
    private NetworkTableEntry nteDriveTrainLeft;
    private NetworkTableEntry nteIsPixyAlive;
    private NetworkTableEntry nteLinePID;

    public Vision() {
        pixyHatch = new Pixy2("Hatch", 0x54);
        pixySource = new PixySource();
        pixyOutput = new PixyOutput();

        // The line PID will try to center the line/vector from pixy. Similar to this
        // example taken from pixy2 zumo code
        // https://github.com/charmedlabs/pixy2/blob/cc82d64b415fbcb3913166416bec679b37f516d9/src/host/arduino/libraries/Pixy2/examples/line_zumo_demo/line_zumo_demo.ino#L74
        lineController = new PIDController(0, 0, 0, pixySource, pixyOutput);
        // https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:line_api
        // Image on that page says that pixy2 line tracking uses x coords from 0-78.
        lineController.setInputRange(0, 78);
        // Set output less than max rpm so we don't try to turn too fast/hard to find
        // the line. Is this valid? Or is tuning PID better/right way?
        //lineController.setOutputRange(-Robot.driveTrain.MAX_RPM * .5, Robot.driveTrain.MAX_RPM * .5);

        // .5 is just a starting guess
        lineController.setP(1.0);
    }

    private class PixyOutput implements PIDOutput {
        public void pidWrite(double output) {
            // SmartDashboard.putNumber("LinePID Output", output);
            ntePidOutput.setDouble(output);
            // Don't output/control motors here. Use the pid output elsewhere to control
            // drive train.
        }
    }

    private class PixySource implements PIDSource {

        Pixy2Vector[] vectors;
        int lastVectorId;
        double lastVectorPos;
        int errors;
        int noVectorsFound;

        public PixySource() {
            vectors = null;
            lastVectorId = -1;
            lastVectorPos = 0.0;
            errors = 0;
            noVectorsFound = 0;
        }

        public String vectorStrings() {
            if (vectors != null) {
                return "0: " + vectors[0].toString();
            }
            return "None";
        }

        public void setPIDSourceType(PIDSourceType pidSource) {
        }

        public PIDSourceType getPIDSourceType() {
            return PIDSourceType.kDisplacement;
        }

        public double pidGet() {

            try {
                vectors = pixyHatch.getVectors();
            } catch (Pixy2Exception e) {
                // System.out.println(e);
                // e.printStackTrace();
                errors++;
                // TODO should we return last vector found?
                return 0;
            }
            if (errors != 0) {
                System.out.println("Vision: errors=" + errors);
                errors = 0;
            }
            // TODO:
            //
            // index can change as pixy moves around. To track the same line need to look
            // for same index N times in a row.
            //
            // x,y can change slightly, observed x changed by 1 in steady camera pic. Maybe
            // that's fine and PID tuning can account for that.
            //
            // always seemed to get just one vector. Try testing with more than one tape
            // line to see what happens.
            //
            if (vectors != null) {
                for (int i = 0; i < vectors.length; i++) {
                    // System.out.println(i + " " + vectors[i]);
                    if (lastVectorId == vectors[i].m_index) {
                        if (noVectorsFound != 0) {
                            System.out.println("Vision: noVectorsFound=" + noVectorsFound);
                            noVectorsFound = 0;
                        }
                        // x1 is the head/arrow end of the vector found by pixy2.
                        lastVectorPos = vectors[i].m_x1;
                        return lastVectorPos;
                    }
                    lastVectorId = vectors[i].m_index;
                }
            }
            noVectorsFound++;
            // TODO should we return last vector found?
            return 0;
        }
    }

    @Override
    public void initDefaultCommand() {
        // setDefaultCommand(new ScoopStop());
    }

    public void setupShuffleboard() {
        // Shuffleboard stuff
        tab = Shuffleboard.getTab("Vision");

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021941-using-tabs
        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021942-sending-data
        // nteLimit = tab.add("HatchLimit", limit.get()).getEntry();
        nteVectors = tab.add("Vectors", pixySource.vectorStrings()).getEntry();

        ntePidError = tab.add("LinePIDError", lineController.getError()).getEntry();
        ntePidSetpoint = tab.add("LinePIDSetpoint", lineController.getSetpoint()).getEntry();
        ntePidOutput = tab.add("LinePIDOutput", lineController.get()).getEntry();
        nteDriveTrainLeft = tab.add("DTLeft", 0).getEntry();
        nteDriveTrainRight = tab.add("DTRight", 0).getEntry();
        nteIsPixyAlive = tab.add("IsPixyAlive", false).getEntry();
        // nteLinePID = tab.add("LinePID", lineController).getEntry();

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021980-organizing-widgets
        ShuffleboardLayout commands = tab.getLayout("Commands", BuiltInLayouts.kList).withSize(2, 3)
                .withProperties(Map.of("Label position", "HIDDEN")); // hide labels for commands
        commands.add(new PixyLine());

    }

    public void updateShuffleDrivetrainOutputs(double left, double right) {
        nteDriveTrainLeft.setDouble(left);
        nteDriveTrainRight.setDouble(right);
    }

    public boolean isPixyAlive() {
        Pixy2Version v = pixyHatch.version();
        return v.get();
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        // if (periodicLoopCounter % 1000 == 0) {
        // Pixy2Vector[] vectors = null;
        // try {
        // vectors = pixyHatch.getVectors();

        // } catch (Pixy2Exception e) {
        // System.out.println(e);
        // e.printStackTrace();
        // }
        // if (vectors != null) {
        // for (int i = 0; i < vectors.length; i++) {
        // System.out.println(i + " " + vectors[i]);
        // }
        // }
        // }
        if (lineController.isEnabled()) {
            nteVectors.setString(pixySource.vectorStrings());
            ntePidError.setDouble(lineController.getError());
            ntePidSetpoint.setDouble(lineController.getSetpoint());
            // ntePidOutput.setDouble(lineController.get());
        } else {
            if (periodicLoopCounter % 1000 == 0) {
                nteIsPixyAlive.setBoolean(isPixyAlive());
            }
        }
        periodicLoopCounter++;

    }

}
