/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc2876.DeepSpace2019.commands;

import org.usfirst.frc2876.DeepSpace2019.Robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

// This command does nothing with hatch subsystem.  Set this to default command and it won't interfere(set motors to unexpected speed/state for example) with any testing you might do while working on hatch subsystem

public class ArmXbox extends Command {
  public ArmXbox() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.arm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double rtrigger = Robot.oi.xboxController.getTriggerAxis(Hand.kRight);
    //double trigger = Robot.oi.xboxController.getRawAxis(Robot.oi.RIGHT_TRIGGER);
    double ltrigger = Robot.oi.xboxController.getTriggerAxis(Hand.kLeft);
    //double trigger = Robot.oi.xboxController.getRawAxis(Robot.oi.LEFT_TRIGGER);
    
    if (Math.abs(ltrigger) > .05) {
      Robot.arm.armDown();
    }
    else if (Math.abs(rtrigger) > .05) {
      Robot.arm.armUp();
    } else {
      Robot.arm.armStop();
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
