/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc2876.DeepSpace2019.commands;

import org.usfirst.frc2876.DeepSpace2019.Robot;
import org.usfirst.frc2876.DeepSpace2019.Pixy2.Pixy2Vector;

import edu.wpi.first.wpilibj.command.Command;

public class TestPixy extends Command {
  public TestPixy() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Pixy2Vector[] vectors;
    vectors = Robot.pixyHatch.getVectors();
    pixyTurn(vectors[0]);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
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

  public void pixyTurn(Pixy2Vector v){
    if(v.m_x0 == v.m_x1){
        return;
    }

    double deg = Math.toDegrees(Math.atan((v.m_y1 - v.m_y0)/(v.m_x1 - v.m_x0)));

    if(v.m_x1 > v.m_x0){
        //turn robot clockwise
    }else if(v.m_x1 < v.m_x0){
        //turn robot counterclockwise
        deg = deg * -1;
    }
    System.out.println("degree of change: " + deg);
  }
}