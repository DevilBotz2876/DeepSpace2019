package org.usfirst.frc2876.DeepSpace2019.utils;

import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2Exception;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2Vector;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2Version;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;

public class PixyLinePID {
    public Pixy2 pixyCamera;
    public PIDController lineController;
    PixySource pixySource;
    PixyOutput pixyOutput;

    private Preferences prefs;

    public PixyLinePID(String name, byte i2cAddress) {
        pixyCamera = new Pixy2(name, i2cAddress);
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
        // lineController.setOutputRange(-Robot.driveTrain.MAX_RPM * .5,
        // Robot.driveTrain.MAX_RPM * .5);
        lineController.setOutputRange(-.4, .4);

        lineController.setContinuous(false);

        prefs = Preferences.getInstance();

        // Test using Preferences to get/set values. Only use this for tuning, stop
        // using once good values found
        //
        // if (!prefs.containsKey("VisionPID_P")) {
        // prefs.putDouble("VisionPID_P", .05);
        // }
        // if (!prefs.containsKey("VisionPID_D")) {
        // prefs.putDouble("VisionPID_D", .0);
        // }
        // lineController.setP(prefs.getDouble("VisionPID_P", .05));
        // lineController.setD(prefs.getDouble("VisionPID_D", .0));

        lineController.setP(.1);
        lineController.setD(.001);
    }

    public String toString() {
        return pixyCamera.toString();
    }

    public void updatePreferences() {
        // lineController.setP(prefs.getDouble("VisionPID_P", .1));
        // lineController.setD(prefs.getDouble("VisionPID_D", .0));
    }

    private class PixyOutput implements PIDOutput {
        public void pidWrite(double output) {
            // TODO output to shuffleboard
            // ntePidOutput.setDouble(output);

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
        int vectorsFound;
        int pixyDelay;

        public PixySource() {
            vectors = null;
            lastVectorId = -1;
            lastVectorPos = -1.0;
            errors = 0;
            noVectorsFound = 0;
            vectorsFound = 0;
            pixyDelay = 0;
        }

        public String vectorStrings() {
            if (vectors != null) {
                return "0: " + vectors[0].toString();
            }
            return "None";
        }

        public boolean isVectorPresent() {
            return vectors != null;
        }

        public void setPIDSourceType(PIDSourceType pidSource) {
        }

        public PIDSourceType getPIDSourceType() {
            return PIDSourceType.kDisplacement;
        }

        public double pidGet() {
            pixyDelay++;
            if (pixyDelay % 5 != 0) {
                return lastVectorPos;
            }
            // System.out.println("pixyDelay=" + pixyDelay);
            try {
                vectors = pixyCamera.getVectors();
            } catch (Pixy2Exception e) {
                // System.out.println(e);
                // e.printStackTrace();
                errors++;
                if (vectorsFound != 0) {
                    System.out.println("Vision: vectorsFound=" + vectorsFound);
                    vectorsFound = 0;
                }
                // TODO should we return last vector found?
                return lastVectorPos;
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
                        vectorsFound++;
                        // x1 is the head/arrow end of the vector found by pixy2.
                        lastVectorPos = vectors[i].m_x0;
                        return lastVectorPos;
                    }
                    lastVectorId = vectors[i].m_index;
                }
            }
            noVectorsFound++;
            // System.out.println("Vision: noVectorsFound=" + noVectorsFound);

            // TODO should we return last vector found?
            return 0;
        }
    }

    // public void updateShuffleDrivetrainOutputs(double speed, double rotate) {
    // nteSpeed.setDouble(speed);
    // nteRotate.setDouble(rotate);
    // }

    public boolean isPixyAlive() {
        Pixy2Version v = pixyCamera.version();
        return v.get();
        // return true;
    }

    public boolean isVectorPresent() {
        if (lineController.isEnabled()) {
            return pixySource.isVectorPresent();
        }
        return false;
    }

    public String vectorStrings() {
        return pixySource.vectorStrings();
    }

}