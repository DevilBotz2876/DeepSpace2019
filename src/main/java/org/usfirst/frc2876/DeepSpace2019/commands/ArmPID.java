/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc2876.DeepSpace2019.commands;

import org.usfirst.frc2876.DeepSpace2019.Robot;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArmPID extends PIDCommand {
  public ArmPID() {
    // Set PID values
    super(0.1, 0, 0);

    requires(Robot.arm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    setSetpoint(Robot.arm.getPosition());
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.arm.updateDashboardPID(getPIDController());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {

    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    getPIDController().disable();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }

  protected double returnPIDInput() {
    return Robot.arm.getPosition();
  }

  protected void usePIDOutput(double output) {
    SmartDashboard.putNumber("armPID output",output);
    // TODO check if direction is correct.
    Robot.arm.set(output);
  }
}
