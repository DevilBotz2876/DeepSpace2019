package org.usfirst.frc2876.DeepSpace2019.commands;

import org.usfirst.frc2876.DeepSpace2019.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class XboxDrive extends Command {

    private boolean velocityMode;
    private double forward;

    public XboxDrive() {

        requires(Robot.driveTrain);

    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        velocityMode = false;
        forward = 1.0;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        XboxController xbox = Robot.oi.getXboxController();

        // If you press and hold right bumper robot drives in velocity mode. Release
        // bumper and switches back to open loop mode.
        if (xbox.getBumperPressed(Hand.kRight)) {
            velocityMode = true;
        } else if (xbox.getBumperReleased(Hand.kRight)) {
            velocityMode = false;
        }

        if (xbox.getBumperPressed(Hand.kLeft)) {
            //forward = forward;
        } else if (xbox.getBumperReleased(Hand.kLeft)) {
            forward *= -1;
        }

        if (!velocityMode) {
            // Robot.driveTrain.velocityTankDrive(-xbox.getY(Hand.kLeft), -xbox.getY(Hand.kRight));
            Robot.driveTrain.setVelocityArcadeJoysticks(-xbox.getY(Hand.kRight)*forward, -xbox.getX(Hand.kLeft)*forward);
        } else {
            //Robot.driveTrain.tankDrive(-xbox.getY(Hand.kLeft), xbox.getY(Hand.kRight));
            Robot.driveTrain.arcadeDrive(xbox.getX(Hand.kRight), -xbox.getY(Hand.kLeft));
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
