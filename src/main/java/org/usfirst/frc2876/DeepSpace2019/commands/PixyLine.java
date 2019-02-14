package org.usfirst.frc2876.DeepSpace2019.commands;

import org.usfirst.frc2876.DeepSpace2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class PixyLine extends Command {
  public PixyLine() {
    requires(Robot.vision);
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.vision.lineController.setAbsoluteTolerance(10);
    Robot.vision.lineController.setSetpoint(39);
    Robot.vision.lineController.enable();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double out = Robot.vision.lineController.get();
    double baseVelocity=0;//Robot.driveTrain.MAX_RPM*.3;
    // TODO double check the signs are correct.
    //Robot.driveTrain.velocityTankDrive(baseVelocity-out, baseVelocity+out);
    Robot.vision.updateShuffleDrivetrainOutputs(baseVelocity-out, baseVelocity+out);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    // TODO probably should also check that we haven't hit the wall of rocket or cargo ship as well.
    //return Robot.vision.lineController.onTarget();
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.vision.lineController.disable();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }

  
}
