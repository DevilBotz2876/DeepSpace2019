package org.usfirst.frc2876.DeepSpace2019.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import org.usfirst.frc2876.DeepSpace2019.Robot;
import org.usfirst.frc2876.DeepSpace2019.commands.CGDriveOffPlatform;
import org.usfirst.frc2876.DeepSpace2019.commands.DriveForward;
import org.usfirst.frc2876.DeepSpace2019.commands.DriveReverse;
import org.usfirst.frc2876.DeepSpace2019.commands.DriveRotate;
import org.usfirst.frc2876.DeepSpace2019.commands.DriveStop;
import org.usfirst.frc2876.DeepSpace2019.commands.ToggleInverseDrive;
import org.usfirst.frc2876.DeepSpace2019.commands.XboxDrive;
import org.usfirst.frc2876.DeepSpace2019.utils.Ramp;
import org.usfirst.frc2876.DeepSpace2019.utils.TalonSrxEncoder;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.SendableCameraWrapper;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * 
 */
public class DriveTrain extends Subsystem {

    private WPI_TalonSRX talonSRX4;
    private WPI_TalonSRX talonSRX3;
    private WPI_TalonSRX talonSRX2;
    private WPI_TalonSRX talonSRX1;
    private DifferentialDrive differentialDrive;

    public PIDController turnController;
    private int turnOnTargets;
    public AHRS navx;

    public UsbCamera scoopCamera;
    public UsbCamera hatchCamera;
    public VideoSink server;

    private WPI_TalonSRX rightMaster;
    private WPI_TalonSRX leftMaster;
    private WPI_TalonSRX rightFollower;
    private WPI_TalonSRX leftFollower;

    private TalonSrxEncoder leftEncoder;
    private TalonSrxEncoder rightEncoder;

    private boolean toggleInverseDrive = false;
    private boolean toggleHelp;

    // Calculated this following instructions here:
    // https://phoenix-documentation.readthedocs.io/en/latest/ch14_MCSensor.html#confirm-sensor-resolution-velocity
    //
    // Right Forward 3300
    // Right Reverse 3200
    // Left Forward 3200
    // Left Reverse 3300
    // (100% X 1023) / 3200 = .3197
    //
    private final double kF = .3197f;

    // Don't allow drivetrain to run at max speed. If we did then there is no room
    // for PID to adjust velocity if we have already maxed out. Specify a number
    // between 0-1 to limit rpm to percentage of max.
    private double RPM_LIMIT = .9;
    // Set this in constructor
    private double MAX_RPM;

    public final double MAX_MEASURED_RPM = 3200.0;

    // Use this to limit how fast velocity can be adjusted.
    private Ramp rampArcadeSpeed;
    private Ramp rampArcadeRotate;
    private Ramp rampTankLeft;
    private Ramp rampTankRight;
    private double defaultRamp = 1;

    private ShuffleboardTab tab;
    private NetworkTableEntry nteRamp;
    private NetworkTableEntry nteMotorOutput;
    private NetworkTableEntry nteInverseDriveToggle;

    private ShuffleboardTab driveTeamTab;

    // TODO Declare navx

    public DriveTrain() {

        navx = new AHRS(SPI.Port.kMXP);

        talonSRX4 = new WPI_TalonSRX(4);
        talonSRX3 = new WPI_TalonSRX(3);
        talonSRX2 = new WPI_TalonSRX(2);
        talonSRX1 = new WPI_TalonSRX(1);

        rightMaster = talonSRX1;
        leftMaster = talonSRX3;
        rightFollower = talonSRX2;
        leftFollower = talonSRX4;

        // https://phoenix-documentation.readthedocs.io/en/latest/ch13_MC.html#follower
        rightFollower.follow(rightMaster);
        leftFollower.follow(leftMaster);

        // https://phoenix-documentation.readthedocs.io/en/latest/ch13_MC.html#inverts
        rightMaster.setInverted(true);
        rightFollower.setInverted(InvertType.FollowMaster);
        leftMaster.setInverted(true);
        leftFollower.setInverted(InvertType.FollowMaster);

        // https://phoenix-documentation.readthedocs.io/en/latest/ch14_MCSensor.html#sensor-phase
        leftMaster.setSensorPhase(false);
        rightMaster.setSensorPhase(true);

        leftMaster.setNeutralMode(NeutralMode.Coast);
        leftFollower.setNeutralMode(NeutralMode.Coast);
        rightMaster.setNeutralMode(NeutralMode.Coast);
        rightFollower.setNeutralMode(NeutralMode.Coast);

        differentialDrive = new DifferentialDrive(leftMaster, rightMaster);
        // LiveWindow.addActuator("DriveTrain", "DifferentialDrive", differentialDrive);

        differentialDrive.setSafetyEnabled(false);
        // differentialDrive.setExpiration(0.1);
        differentialDrive.setMaxOutput(1.0);

        if (Robot.robotSettings.isCompBot()) {
            MAX_RPM = MAX_MEASURED_RPM * RPM_LIMIT;
        } else {
            // practice bot has a broken encoder on one side of drive train. So can't drive
            // in velocity closed loop mode. Set MAX_RPM to value between 0-1 so that any
            // settings/adjusts that would normally treat it as RPM leaves it as percent
            // output
            MAX_RPM = 1;
        }

        // TODO configure pid on talons and stuffs. Example how to use
        // configAllSettings:
        // https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/b71916c131f6b381ba26bb5ac46302180088614d/Java/Config%20All/src/main/java/frc/robot/Configs.java#L19
        TalonSRXConfiguration allConfigs = new TalonSRXConfiguration();

        allConfigs.primaryPID.selectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative;

        allConfigs.slot0.kF = kF;
        rightMaster.configAllSettings(allConfigs);
        leftMaster.configAllSettings(allConfigs);

        leftEncoder = new TalonSrxEncoder(leftMaster);
        rightEncoder = new TalonSrxEncoder(rightMaster);

        rampArcadeSpeed = new Ramp(MAX_RPM * defaultRamp);
        rampArcadeRotate = new Ramp(MAX_RPM * defaultRamp);
        rampTankLeft = new Ramp(MAX_RPM * defaultRamp);
        rampTankRight = new Ramp(MAX_RPM * defaultRamp);

        PIDOutput po = new PIDOutput() {
			public void pidWrite(double output) {
				output = output * MAX_RPM;
				 double minMove = 700.0f;
				 output = minRpm(output, minMove);

                leftMaster.set(ControlMode.Velocity, -output);
                rightMaster.set(ControlMode.Velocity, output);

            }
        };
        turnController = new PIDController(.006, 0.0, 0.0, 0.0, (PIDSource)navx, po);
        turnController.setPercentTolerance(10.0);
        turnController.setInputRange(0.0, 360.0);
        turnController.setOutputRange(-0.5, 0.5);

        initializeCameras();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new XboxDrive());
    }

    public void setupShuffleboard() {
        // Shuffleboard stuff
        tab = Shuffleboard.getTab("DriveTrain");

        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021941-using-tabs
        // https://wpilib.screenstepslive.com/s/currentCS/m/shuffleboard/l/1021942-sending-data
        // nteLimit = tab.add("HatchLimit", limit.get()).getEntry();
        // nteMotorOutput = tab.add("HatchMotorOutput", master.get()).getEntry();
        // TODO not sure this will work, does it need to get called in periodic?
        // tab.add("HatchEncoder", encoder);
        nteRamp = tab.add("RampRateTuner", 1).withWidget("Number Slider")
                .withProperties(
                        Map.of(String.valueOf("min"), Double.valueOf(0), String.valueOf("max"), Double.valueOf(1)))
                .withSize(2, 1).getEntry();

        nteMotorOutput = tab.add("RPM", 0).withSize(4, 4).getEntry();
        nteInverseDriveToggle = tab.add("Hatch-Side Forward", true)
        .withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

        ShuffleboardLayout commands = tab.getLayout("Commands", BuiltInLayouts.kList).withSize(7, 10)
                .withProperties(Map.of("Label position", "HIDDEN")).withPosition(0, 0); // hide labels for commands
        commands.add(new CGDriveOffPlatform());
        commands.add(new DriveStop());
        commands.add(new DriveForward());
        commands.add(new DriveReverse());

        commands.add(new DriveRotate(180.0));

        commands.add(new ToggleInverseDrive());

        tab.add("Camera", SendableCameraWrapper.wrap(server.getSource()));

        // DRIVE TEAM COMMANDS START
        driveTeamTab = Shuffleboard.getTab("DriveTeam");




    }

    // public void initializeCamera(int camNum) {
	// 	server = CameraServer.getInstance();
	// 	// server.setQuality(50);
    //     if (camNum == 0){
    //         UsbCamera serverUsbScoop = server.startAutomaticCapture("Scoop Camera", camNum);
    //         server.setConnectionSt
    //         serverUsbScoop.setFPS(15);
    //         serverUsbScoop.setResolution(160, 120);
    //     }else{
    //         UsbCamera serverUsbHatch = server.startAutomaticCapture("Hatch Camera", camNum);
    //         serverUsbHatch.setFPS(15);
    //         serverUsbHatch.setResolution(160, 120);
    //     }
    // }
    
    public void initializeCameras() {
        scoopCamera = CameraServer.getInstance().startAutomaticCapture("Scoop Camera", 0);
        hatchCamera = CameraServer.getInstance().startAutomaticCapture("Hatch Camera", 1);
        server = CameraServer.getInstance().addSwitchedCamera("Toggle Camera");
        scoopCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
        hatchCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

        scoopCamera.setFPS(15);
        scoopCamera.setResolution(160, 120);
        hatchCamera.setFPS(15);
        hatchCamera.setResolution(160, 120);
    }
    
    public void getCameraSource(){
        server.getSource();
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    // Use this method to tune ramp rate using a slider on shuffleboard
    private void updateRamps() {
        double r = nteRamp.getNumber(defaultRamp).doubleValue();
        rampArcadeRotate.setMaxChangePerSecond(MAX_RPM * r);
        rampArcadeSpeed.setMaxChangePerSecond(MAX_RPM * r);
        rampTankLeft.setMaxChangePerSecond(MAX_RPM * r);
        rampTankRight.setMaxChangePerSecond(MAX_RPM * r);
    }

    private void updateDirection(){
        toggleInverseDrive = nteInverseDriveToggle.getBoolean(toggleInverseDrive);
    }

    public void arcadeDrive(double xSpeed, double zRotation) {
        differentialDrive.arcadeDrive(xSpeed, zRotation);
    }

    public void tankDrive(double leftValue, double rightValue) {
        differentialDrive.tankDrive(leftValue, rightValue);
    }

    public void velocityTankDrive(double leftValue, double rightValue) {

        updateRamps();
        double leftRpm = leftValue * MAX_RPM;
        leftRpm = rampTankLeft.get(leftRpm);
        double rightRpm = rightValue * MAX_RPM;
        rightRpm = rampTankRight.get(rightRpm);

        leftMaster.set(ControlMode.Velocity, leftRpm);
        rightMaster.set(ControlMode.Velocity, rightRpm);
    }

    // Note only use this to control drive train from joysticks. DO NOT use this
    // method to control robot from PID loops or other non-joystick sources.
    public void setVelocityArcadeJoysticks(double speed, double rotate) {

        // This reads slider on dash and changes ramp rate. Should be removed once we
        // find a a good rate for driving.
        //updateRamps();
        updateDirection();

        double leftRPM, rightRPM;

        // Apply deadband
        if (Math.abs(speed) < .09) {
            speed = 0.0;
        }
        if (Math.abs(rotate) < .09) {
            rotate = 0.0;
        }

        double MAX_ROTATE = .8;
        if (rotate > MAX_ROTATE) {
            rotate = MAX_ROTATE;
        } else if (rotate < -MAX_ROTATE) {
            rotate = -MAX_ROTATE;
        }

        speed = adjustJoystickSensitivity(speed);
        rotate = adjustJoystickSensitivity(rotate);
        if (speed > 0.0) {
            if (rotate > 0.0) {
                // leftMaster.set(ControlMode.Velocity, (speed - rotate) * MAX_RPM);
                // rightMaster.set(ControlMode.Velocity, Math.max(speed, rotate) * MAX_RPM);
                leftRPM = (speed - rotate);
                rightRPM = Math.max(speed, rotate);
            } else {
                // leftRPM = rampTankLeft.get(Math.max(speed, -rotate) * MAX_RPM);
                // double l = Math.max(speed, -rotate) * MAX_RPM;
                // leftMaster.set(ControlMode.Velocity,l);
                // double r = rampTankRight.get((speed + rotate) * MAX_RPM);
                // double r = (speed + rotate) * MAX_RPM;
                // rightMaster.set(ControlMode.Velocity, r);
                leftRPM = Math.max(speed, -rotate);
                rightRPM = (speed + rotate);
            }
        } else {
            if (rotate > 0.0) {
                // leftMaster.set(ControlMode.Velocity, -Math.max(-speed, rotate) * MAX_RPM);
                // rightMaster.set(ControlMode.Velocity, (speed + rotate) * MAX_RPM);
                leftRPM = -Math.max(-speed, rotate);
                rightRPM = (speed + rotate);
            } else {
                // leftMaster.set(ControlMode.Velocity, (speed - rotate) * MAX_RPM);
                // rightMaster.set(ControlMode.Velocity, -Math.max(-speed, -rotate) * MAX_RPM);
                leftRPM = (speed - rotate);
                rightRPM = -Math.max(-speed, -rotate);
            }
        }

        // Turn -1..1 value into RPMs
        leftRPM *= MAX_RPM;
        rightRPM *= MAX_RPM;

        // TODO might be a good place to limit how fast robot can go if arm is high or
        // some other reason/condition. Should this limit be enforced before or after
        // ramping tho?

        // Ramp left and right RPM to avoid jerky motions, poppin wheelies
        leftRPM = rampTankLeft.get(leftRPM);
        rightRPM = rampTankRight.get(rightRPM);

        // Send velocity setpoint(RPMs) to talons
        if (Robot.robotSettings.isCompBot()) {
            leftMaster.set(ControlMode.Velocity, leftRPM);
            rightMaster.set(ControlMode.Velocity, rightRPM);
        } else {
            leftMaster.set(ControlMode.PercentOutput, leftRPM);
            rightMaster.set(ControlMode.PercentOutput, rightRPM);

        }
    }

    private double adjustJoystickSensitivity(double speed) {
        // Adjust sensitivity of joysticks. When the joystick is barely
        // pressed/moved send small output to motors. When joystick
        // is press/moved alot send BIG output to motors.
        // y=a(x^3)+(1-a)x
        double a = .2;
        return (a * (speed * speed * speed)) + ((1 - a) * speed);
    }

    public boolean toggleInverseDrive() {
        boolean buttonPressed = Robot.oi.getSelectButton();
        if (buttonPressed && toggleHelp) {
            toggleInverseDrive = !toggleInverseDrive;
        }
        // leftMaster.setInverted(toggleInverseDrive);
        // rightMaster.setInverted(toggleInverseDrive);
        toggleHelp = buttonPressed;
        return toggleInverseDrive;
    }

    public boolean getToggleInverseDrive() {
        return toggleInverseDrive;
    }





	public void resetEncoders() {
		leftMaster.setSelectedSensorPosition(leftMaster.getDeviceID(), 0, 0);
		rightMaster.setSelectedSensorPosition(rightMaster.getDeviceID(), 0, 0);
    }
    
    private double minRpm(double inputRpm, double minRpm) {
		double outputRpm = inputRpm;
		if (Math.abs(inputRpm) < minRpm) {
			if (outputRpm < 0) {
				outputRpm = -minRpm;
			} else if (outputRpm > 0) {
				outputRpm = minRpm;
			}
		}
		return outputRpm;
	}

    public boolean isTurnDone() {
		if (turnController.onTarget()) {
			turnOnTargets++;
		}
		return (turnOnTargets > 0);
	}

	public void startTurn(double turn) {
		turnOnTargets = 0;
		turnController.reset();
		navx.reset();
		//resetEncoders();
		turnController.setSetpoint(turn);
		turnController.enable();
    }
    
    public void stopTurn() {
		turnController.reset();
		leftMaster.set(0);
		rightMaster.set(0);
	}
}
