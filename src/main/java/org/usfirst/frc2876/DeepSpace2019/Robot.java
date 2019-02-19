package org.usfirst.frc2876.DeepSpace2019;

import org.usfirst.frc2876.DeepSpace2019.commands.AutonomousCommand;
import org.usfirst.frc2876.DeepSpace2019.subsystems.Arm;
import org.usfirst.frc2876.DeepSpace2019.subsystems.DriveTrain;
import org.usfirst.frc2876.DeepSpace2019.subsystems.Hatch;
import org.usfirst.frc2876.DeepSpace2019.subsystems.Scoop;
import org.usfirst.frc2876.DeepSpace2019.subsystems.Vision;
import org.usfirst.frc2876.DeepSpace2019.utils.RobotSettings;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<>();

    public static OI oi;

    public static RobotSettings robotSettings;

    public static DriveTrain driveTrain;
    public static Arm arm;
    public static Scoop scoop;
    public static Hatch hatch;
    public static Vision vision;

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {

        robotSettings = new RobotSettings();

        driveTrain = new DriveTrain();
        arm = new Arm();
        scoop = new Scoop();
        hatch = new Hatch();
        vision = new Vision();

        // OI must be constructed after subsystems. If the OI creates Commands
        // (which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Add commands to Autonomous Sendable Chooser

        chooser.setDefaultOption("Autonomous Command", new AutonomousCommand());
        SmartDashboard.putData("Auto mode", chooser);

        hatch.setupShuffleboard();
        SmartDashboard.putData(hatch);
        driveTrain.setupShuffleboard();
        SmartDashboard.putData(driveTrain);
        arm.setupShuffleboard();
        SmartDashboard.putData(arm);

        vision.setupShuffleboard();
        SmartDashboard.putData(vision);

        SmartDashboard.putData(Scheduler.getInstance());
    }

    /**
     * This function is called when the disabled button is hit. You can use it to
     * reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        autonomousCommand = chooser.getSelected();
        // schedule the autonomous command (example)
        if (autonomousCommand != null)
            autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null)
            autonomousCommand.cancel();

    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
}
