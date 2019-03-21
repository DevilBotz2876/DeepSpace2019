package org.usfirst.frc2876.DeepSpace2019.commands;

import org.usfirst.frc2876.DeepSpace2019.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class XboxDrive extends Command {

    public XboxDrive() {
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        XboxController xbox = Robot.oi.getXboxController();

        if (Robot.driveTrain.isHatchForward()) {
            Robot.driveTrain.setVelocityArcadeJoysticks(xbox.getY(Hand.kLeft), -xbox.getX(Hand.kRight));
        } else {
            Robot.driveTrain.setVelocityArcadeJoysticks(-xbox.getY(Hand.kLeft), -xbox.getX(Hand.kRight));
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.driveTrain.setVelocityArcadeJoysticks(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
