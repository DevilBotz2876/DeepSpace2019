package org.usfirst.frc2876.DeepSpace2019.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.usfirst.frc2876.DeepSpace2019.commands.ArmDown;
import org.usfirst.frc2876.DeepSpace2019.commands.ArmIdle;
import org.usfirst.frc2876.DeepSpace2019.commands.ArmPID;
import org.usfirst.frc2876.DeepSpace2019.commands.ArmStop;
import org.usfirst.frc2876.DeepSpace2019.commands.ArmUp;
import org.usfirst.frc2876.DeepSpace2019.commands.ArmUpDrive;
import org.usfirst.frc2876.DeepSpace2019.utils.TalonSrxEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 *
 */
public class Arm extends Subsystem {

    private WPI_TalonSRX talonSRX5;
    private WPI_TalonSRX talonSRX6;

    private WPI_TalonSRX master;
    private WPI_TalonSRX follower;

    private ShuffleboardTab tab;
    private NetworkTableEntry nteLimit;
    private NetworkTableEntry nteMotorOutput;
    private NetworkTableEntry nteSetPosition;
    private NetworkTableEntry ntePIDSetpoint;
    private NetworkTableEntry nteCurrentPosition;
    private NetworkTableEntry nteArmPidOutput;
    private TalonSrxEncoder encoder;

    private DigitalInput limit;
    private int periodicLoopCounter;

    public Arm() {
        periodicLoopCounter=0;
        talonSRX5 = new WPI_TalonSRX(5);
        talonSRX6 = new WPI_TalonSRX(6);

        limit = new DigitalInput(1);

        master = talonSRX5;
        follower = talonSRX6;

        follower.follow(master);


        //
        // https://phoenix-documentation.readthedocs.io/en/latest/ch16_ClosedLoop.html#motion-magic-position-velocity-current-closed-loop-closed-loop
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/MotionMagic/src/main/java/frc/robot/Robot.java

        // Example how to use configAllSettings:
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/Config%20All/src/main/java/frc/robot/Configs.java#L19
        TalonSRXConfiguration allConfigs = new TalonSRXConfiguration();
        master.configAllSettings(allConfigs);
        follower.configAllSettings(allConfigs);

        setupPID();

        encoder = new TalonSrxEncoder(master);

        periodicLoopCounter = 0;
    }

    // Grabbed this from last year elevator which used same motor/encoder setup.
    // Test if this works better or at all than ctre absolute example
    public void setupPID() {
        int kPIDLoopIdx = 0;
        int kTimeoutMs = 30;
        /* choose the sensor and sensor direction */
        //master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
        master.setSelectedSensorPosition(0);
        /* choose to ensure sensor is positive when output is positive */
        master.setSensorPhase(true);

        /*
         * choose based on what direction you want forward/positive to be. This does not
         * affect sensor phase.
         */
        master.setInverted(true);
        follower.setInverted(true);

        
        /* set the peak and nominal outputs, 12V means full */
        //master.configNominalOutputForward(0, kTimeoutMs);
        //master.configNominalOutputReverse(0, kTimeoutMs);

        // 1 means full power, 12v. Perhaps make down smaller than up since gravity is
        // helping go down.
        // up
        master.configPeakOutputForward(.4, kTimeoutMs);
        // down
        master.configPeakOutputReverse(-.3, kTimeoutMs);
        /*
         * set the allowable closed-loop error, Closed-Loop output will be neutral
         * within this range. See Table in Section 17.2.1 for native units per rotation.
         */
        //master.configAllowableClosedloopError(kPIDLoopIdx, 10, kTimeoutMs);

        /* set closed loop gains in slot0, typically kF stays zero. */
        // master.config_kF(kPIDLoopIdx, 0.0, kTimeoutMs);
        // master.config_kP(kPIDLoopIdx, 0.4, kTimeoutMs);
        // master.config_kI(kPIDLoopIdx, 0.0, kTimeoutMs);
        // master.config_kD(kPIDLoopIdx, 0.0, kTimeoutMs);

        /*
         * lets grab the 360 degree position of the MagEncoder's absolute position, and
         * initially set the relative sensor to match.
         */
        // int absolutePosition = master.getSensorCollection().getPulseWidthPosition();
        // /* mask out overflows, keep bottom 12 bits */
        // absolutePosition &= 0xFFF;
        // if (kSensorPhase)
        // absolutePosition *= -1;
        // if (kMotorInvert)
        // absolutePosition *= -1;
        // /* set the quadrature (relative) sensor to match absolute */
        // master.setSelectedSensorPosition(absolutePosition, kPIDLoopIdx, kTimeoutMs);

    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ArmIdle());
    }

    public void setupShuffleboard() {
        // Shuffleboard stuff
        tab = Shuffleboard.getTab("Arm");

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021941-using-tabs
        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021942-sending-data
        // nteLimit = tab.add("HatchLimit", limit.get()).getEntry();
        nteMotorOutput = tab.add("ArmMotorOutput", master.get()).getEntry();
        ntePIDSetpoint = tab.add("ArmPIDSetpoint", 0).getEntry();
        nteCurrentPosition = tab.add("ArmCurrentPosition", 0).getEntry();
        nteLimit = tab.add("ArmBottomLimit", limit.get()).getEntry();
        nteArmPidOutput = tab.add("ArmPidOutput", 0).getEntry();

        nteSetPosition = tab.add("ArmSetPosition", 1)
                // .withWidget("Number Slider")
                .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", -1, "max", 1)).withSize(2, 1)
                .withPosition(10, 0).getEntry();
        // tab.add("ArmEncoder", encoder);

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021980-organizing-widgets
        ShuffleboardLayout commands = tab.getLayout("Commands", BuiltInLayouts.kList).withSize(2, 3)
                .withProperties(Map.of("Label position", "HIDDEN")); // hide labels for commands
        commands.add(new ArmStop());
        commands.add(new ArmDown());
        commands.add(new ArmUp());
        commands.add(new ArmUpDrive());
        commands.add(new ArmPID());

    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        // TODO Call udpate dashboard here
        // SmartDashboard.putNumber("Arm Motor Output", master.get());
        nteMotorOutput.setDouble(master.get());
        nteCurrentPosition.setDouble(getPosition());
        nteLimit.setBoolean(isArmBottom());
       
        if (master.getControlMode() == ControlMode.Position) {
            ntePIDSetpoint.setDouble(master.getClosedLoopTarget(0));
        }

        // if (periodicLoopCounter % 50 == 0) {
        //     int pos = master.getSensorCollection().getPulseWidthPosition();
        //     int posMask = pos & 0xFFF;
        //     int ssPos = master.getSelectedSensorPosition();
        //     System.out.println("arm limit: " + isArmBottom()
        //     + " pos: " + pos
        //     + " posMask: " + posMask
        //     + " ssPos: " + ssPos
        //     );
        // }
        periodicLoopCounter++;
    }

    public void updateDashboardPID(PIDController pid) {
        nteArmPidOutput.setDouble(pid.get());
    }

    public void armUp() {
        master.set(ControlMode.PercentOutput, 0.3);
    }

    public void armDown() {
        master.set(ControlMode.PercentOutput, -0.2);
    }

    public void armStop() {
        master.set(ControlMode.PercentOutput, 0);
    }

    public void set(double val) {
        master.set(ControlMode.PercentOutput, val);
    }

    public void resetPosition() {
        master.setSelectedSensorPosition(0, 0, 30);
    }

    public boolean isArmBottom() {
        return !limit.get();
    }

    public void setPosition(double pos) {
        // master.set(ControlMode.MotionMagic, pos);
        master.set(ControlMode.Position, pos);
        System.out.println("slider value: " + pos);
    }

    public double getPosition() {
        //return master.getSensorCollection().getPulseWidthPosition() & 0xFFF;
        return master.getSensorCollection().getPulseWidthPosition();
        // return master.getSelectedSensorPosition();
    }

    private final double TOP = -4000;
    private final double BOTTOM = -1800;

    public void dashboardUpdatePosition() {
        double dashValue = nteSetPosition.getNumber(0).doubleValue();

        // https://stats.stackexchange.com/questions/281162/scale-a-number-between-a-range
        double rmin = -1;
        double rmax = 1;
        double tmin = TOP;
        double tmax = BOTTOM;
        double scaled = (dashValue - rmin) / (rmax - rmin) * (tmax - tmin) + tmin;

        setPosition(scaled);
    }
}
