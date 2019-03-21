/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc2876.DeepSpace2019.utils;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.usfirst.frc2876.DeepSpace2019.Robot;
import org.usfirst.frc2876.DeepSpace2019.commands.CGDriveOffHatch;
import org.usfirst.frc2876.DeepSpace2019.commands.CGResetHatchPosition;
import org.usfirst.frc2876.DeepSpace2019.commands.CGDriveOffCargo;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.SendableCameraWrapper;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class DriverShuffleboardTab {

    public ShuffleboardTab driveTeamTab;

    private NetworkTableEntry nteIsHatchForward;
    private NetworkTableEntry nteIsVectorFound;
    private NetworkTableEntry nteHatchPosition;
    private NetworkTableEntry nteSetPositionHatch;
    private NetworkTableEntry nteSetPositionArm;
    private NetworkTableEntry ntePIDSetpointArm;

    public DriverShuffleboardTab(){
        
    }

    public void setup(SendableChooser<Command> chooser){
        /////////////////////////
        // DRIVE TEAM COMMANDS //
        /////////////////////////

        driveTeamTab = Shuffleboard.getTab("DriveTeam");

        // Camera
        if (Robot.driveTrain.server != null) {
            driveTeamTab.add("Camera", SendableCameraWrapper.wrap(Robot.driveTrain.server.getSource()))
            .withSize(17, 21).withPosition(0, 0);
        }

        // Gyro

        // Auto Leave Platform

        driveTeamTab.add("Sandstorm", chooser).withPosition(17, 10).withSize(15, 3);

        //I CHANGED NAME TO "Auto Leave Platform"
        driveTeamTab.add("Auto Drive Cargo", new CGDriveOffCargo())
        .withSize(7, 3).withPosition(17, 6);

        driveTeamTab.add("Auto Drive Hatch", new CGDriveOffHatch())
        .withSize(7, 3).withPosition(25, 6);

        // Zero Arm Pos
        driveTeamTab.add("Reset Hatch Position", new CGResetHatchPosition())
        .withSize(7,3).withPosition(33,6);

        // Starting Position (Hatch-front or Cargo-front)
        nteIsHatchForward = driveTeamTab.add("Hatch-Side Forward", Robot.driveTrain.isHatchForward())
        .withSize(7,6).withPosition(25, 0).getEntry();

        // Is Vector Found
        nteIsVectorFound = driveTeamTab.add("IsVectorFound", false)
        .withSize(7, 6).withPosition(17, 0).getEntry();

        // Arm (encoder position + set-point)


        // Hatch (encoder postion + set-point)
        nteHatchPosition = driveTeamTab.add("HatchPosition", Robot.hatch.getPosition())
        .withSize(5, 5).withPosition(33, 0).getEntry();

    }
    
    public void periodic() {

        nteIsVectorFound.setBoolean(Robot.vision.isVectorPresent());
        nteHatchPosition.setDouble(Robot.hatch.getPosition());
        nteIsHatchForward.setBoolean(Robot.driveTrain.isHatchForward());


    }

}
