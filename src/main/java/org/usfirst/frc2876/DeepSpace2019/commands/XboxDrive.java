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

        // TODO:
        // - Test that robot drives fwd/back and turns left/right correctly when inverse
        // drive is set both ways.
        // - clean this up, why negate get call?
        // - break out the call to setVelocityArcadeJoysticks out of if/else block.
        // Create temp var to store the speed and rotate values and negate appropriately
        // inside if/else block
        // - Give get func clearer name, don't include action/verb in get function name
        if (!Robot.driveTrain.getToggleInverseDrive()) {
            Robot.driveTrain.setVelocityArcadeJoysticks(-xbox.getY(Hand.kLeft), -xbox.getX(Hand.kRight));
            Robot.driveTrain.server.setSource(Robot.driveTrain.scoopCamera);
        } else {
            Robot.driveTrain.setVelocityArcadeJoysticks(xbox.getY(Hand.kLeft), -xbox.getX(Hand.kRight));
            // Robot.driveTrain.server.setSource(Robot.driveTrain.hatchCamera);
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
