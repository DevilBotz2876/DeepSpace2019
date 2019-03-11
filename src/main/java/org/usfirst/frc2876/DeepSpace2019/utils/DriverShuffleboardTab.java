/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc2876.DeepSpace2019.utils;

import org.usfirst.frc2876.DeepSpace2019.Robot;
import org.usfirst.frc2876.DeepSpace2019.commands.CGDriveOffPlatform;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.SendableCameraWrapper;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class DriverShuffleboardTab {

    public ShuffleboardTab driveTeamTab;

    private NetworkTableEntry nteInverseDriveToggle;
    private NetworkTableEntry nteIsVectorFound;
    private NetworkTableEntry ntePIDSetpoint;

    public DriverShuffleboardTab(){

    }

    public void setup(){
        /////////////////////////
        // DRIVE TEAM COMMANDS //
        /////////////////////////

        driveTeamTab = Shuffleboard.getTab("DriveTeam");

        // Camera
        driveTeamTab.add("Camera", SendableCameraWrapper.wrap(Robot.driveTrain.server.getSource()));

        // Gyro

        // Auto Leave Platform
        //I CHANGED NAME TO "Auto Leave Platform"
        driveTeamTab.add("Auto Leave Platform", new CGDriveOffPlatform());

        // Starting Position (Hatch-front or Cargo-front)
        nteInverseDriveToggle = driveTeamTab.add("Hatch-Side Forward", true)
        .withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

        // Is Vector Found
        nteIsVectorFound = driveTeamTab.add("IsVectorFound", false).withSize(6, 6).withPosition(12, 0).getEntry();

        // Arm (encoder position + set-point)

        // Zero Arm Pos

        // Hatch (encoder postion + set-point)
        ntePIDSetpoint = driveTeamTab.add("HatchSetpoint", 0).withSize(5, 3).withPosition(0, 15).getEntry();

        // Zero Hatch Pos

    }


}
