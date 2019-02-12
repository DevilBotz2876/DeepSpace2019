package org.usfirst.frc2876.DeepSpace2019.subsystems;

import java.util.Map;

import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2Exception;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2Vector;
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

    private ShuffleboardTab tab;
    private NetworkTableEntry nteVectors;

    public Vision() {
        pixyHatch = new Pixy2("pixyHatch", 0x54);
        pixySource = new PixySource();

        lineController = new PIDController(0, 0, 0, pixySource, new PIDOutput() {
            public void pidWrite(double output) {
                SmartDashboard.putNumber("LinePID Output", output);
                // Don't output any values to the talons to make robot move
                // here. Instead use the outputs in other places so we can
                // combine multiple PID controllers.
            }
        });

    }

    private class PixySource implements PIDSource {

        Pixy2Vector[] vectors;

        public PixySource() {
            vectors = null;
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
                System.out.println(e);
                e.printStackTrace();
            }
            // TODO:
            //
            // index can change as pixy moves around. To track the same line need to look
            // for same index N times in a row.
            //
            // x,y can change slightly, observed x changed by 1 in steady camera pic.
            //
            // always seemed to get just one vector. Try testing with more than one tape
            // line to see what happens.
            //
            if (vectors != null) {
                for (int i = 0; i < vectors.length; i++) {
                    System.out.println(i + "  " + vectors[i]);
                }
            }
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

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021980-organizing-widgets
        ShuffleboardLayout commands = tab.getLayout("Commands", BuiltInLayouts.kList).withSize(2, 3)
                .withProperties(Map.of("Label position", "HIDDEN")); // hide labels for commands
        commands.add(new PixyLine());

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
        }
        periodicLoopCounter++;

    }

}
