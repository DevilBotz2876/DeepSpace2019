package org.usfirst.frc2876.DeepSpace2019.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.usfirst.frc2876.DeepSpace2019.commands.ArmDown;
import org.usfirst.frc2876.DeepSpace2019.commands.ArmIdle;
import org.usfirst.frc2876.DeepSpace2019.commands.ArmPosition;
import org.usfirst.frc2876.DeepSpace2019.commands.ArmStop;
import org.usfirst.frc2876.DeepSpace2019.commands.ArmUp;
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
    private TalonSrxEncoder encoder;



    public Arm() {
        talonSRX5 = new WPI_TalonSRX(5);
        talonSRX6 = new WPI_TalonSRX(6);

        master = talonSRX5;
        follower = talonSRX6;

        follower.follow(master);

        // TODO configure talons and stuffs. 
        //master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        master.configNominalOutputForward(.2);
        master.configNominalOutputReverse(-.2);
        //
        // https://phoenix-documentation.readthedocs.io/en/latest/ch16_ClosedLoop.html#motion-magic-position-velocity-current-closed-loop-closed-loop
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/MotionMagic/src/main/java/frc/robot/Robot.java

        // Example how to use configAllSettings:
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/Config%20All/src/main/java/frc/robot/Configs.java#L19
        TalonSRXConfiguration allConfigs = new TalonSRXConfiguration();
        master.configAllSettings(allConfigs);
        follower.configAllSettings(allConfigs);

        encoder = new TalonSrxEncoder(master);
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

        nteSetPosition = tab.add("ArmSetPosition", 1)
                // .withWidget("Number Slider")
                .withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", -1, "max", 1))
                .withSize(2, 1)
                .withPosition(10, 0)
                .getEntry();
        //tab.add("ArmEncoder", encoder);

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021980-organizing-widgets
        ShuffleboardLayout commands = tab.getLayout("Commands", BuiltInLayouts.kList).withSize(2, 3)
                .withProperties(Map.of("Label position", "HIDDEN")); // hide labels for commands
        commands.add(new ArmStop());
        commands.add(new ArmDown());
        commands.add(new ArmUp());
        commands.add(new ArmPosition(TOP/2));

    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        // TODO Call udpate dashboard here
        //SmartDashboard.putNumber("Arm Motor Output", master.get());
        nteMotorOutput.setDouble(master.get());
        nteCurrentPosition.setDouble(getPosition());

        if (master.getControlMode() == ControlMode.Position) {
            ntePIDSetpoint.setDouble(master.getClosedLoopTarget(0));
            
        }
    }
    // TODO Add an update dashboard method

    public void armUp() {
        master.set(ControlMode.PercentOutput, -0.2);
    }

    public void armDown() {
        master.set(ControlMode.PercentOutput, 0.05);
    }

    public void armStop() {
        master.set(ControlMode.PercentOutput, 0);
    }

    public void setPosition(double pos) {
        // master.set(ControlMode.MotionMagic, pos);
        //master.set(ControlMode.Position, pos);
        System.out.println("slider value: " + pos);
    }

    public double getPosition() {
        return master.getSensorCollection().getPulseWidthPosition();
        //return master.getSelectedSensorPosition();
    }

    private final double TOP = -8000;
    private final double BOTTOM = -1800;
    public void dashboardUpdatePosition() {
        double dashValue = nteSetPosition.getNumber(0).doubleValue();

        // https://stats.stackexchange.com/questions/281162/scale-a-number-between-a-range
        double rmin = -1;
        double rmax = 1;
        double tmin = TOP;
        double tmax = BOTTOM;
        double scaled = (dashValue - rmin)/(rmax - rmin) * (tmax - tmin) + tmin;

        setPosition(scaled);
    }
}
