/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc2876.DeepSpace2019.utils;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.usfirst.frc2876.DeepSpace2019.Robot;
import org.usfirst.frc2876.DeepSpace2019.commands.CGDriveOffPlatform;
import org.usfirst.frc2876.DeepSpace2019.commands.HatchZeroPosition;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.SendableCameraWrapper;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class DriverShuffleboardTab {

    public ShuffleboardTab driveTeamTab;

    private WPI_TalonSRX talonSRX5;
    private WPI_TalonSRX talonSRX6;
    private WPI_TalonSRX talonSRX8;
    
    private WPI_TalonSRX master;
    private TalonSrxEncoder encoder;

    private WPI_TalonSRX masterArm;
    private WPI_TalonSRX followerArm;

    private NetworkTableEntry nteInverseDriveToggle;
    private NetworkTableEntry nteIsVectorFound;
    private NetworkTableEntry ntePIDSetpointHatch;
    private NetworkTableEntry nteSetPositionHatch;
    private NetworkTableEntry nteSetPositionArm;
    private NetworkTableEntry ntePIDSetpointArm;

    public DriverShuffleboardTab(){
        talonSRX5 = new WPI_TalonSRX(5);
        talonSRX6 = new WPI_TalonSRX(6);
        talonSRX8 = new WPI_TalonSRX(8);
        master = talonSRX8;
        encoder = new TalonSrxEncoder(master);

        masterArm = talonSRX5;
        followerArm = talonSRX6;

        followerArm.follow(master);
    }

    public void setup(){
        /////////////////////////
        // DRIVE TEAM COMMANDS //
        /////////////////////////

        driveTeamTab = Shuffleboard.getTab("DriveTeam");

        // Camera
        driveTeamTab.add("Camera", SendableCameraWrapper.wrap(Robot.driveTrain.server.getSource()))
        .withSize(17, 21).withPosition(0, 0);

        // Gyro

        // Auto Leave Platform
        //I CHANGED NAME TO "Auto Leave Platform"
        driveTeamTab.add("Auto Leave Platform", new CGDriveOffPlatform())
        .withSize(7, 3).withPosition(17, 6);

        // Starting Position (Hatch-front or Cargo-front)
        nteInverseDriveToggle = driveTeamTab.add("Hatch-Side Forward", true).withPosition(17, 12)
        .withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

        // Is Vector Found
        nteIsVectorFound = driveTeamTab.add("IsVectorFound", false)
        .withSize(7, 6).withPosition(17, 0).getEntry();

        // Arm (encoder position + set-point)

        // Zero Arm Pos

        // Hatch (encoder postion + set-point)
        ntePIDSetpointHatch = driveTeamTab.add("HatchSetpoint", 0).withSize(5, 3).withPosition(0, 15).getEntry();

        // Zero Hatch Pos

    }

    public void setupSecond(){
        // Arm Encoder

        // Zero Arm Pos

        // Hatch (encoder postion + set-point)
        //driveTeamTab.add("HatchEncoder", encoder).withPosition(18, 0).withSize(10, 6);
        // ntePIDSetpoint = driveTeamTab.add("HatchSetpoint", 0)
        // .withSize(7, 3).withPosition(17, 9).getEntry();

        // Zero Hatch Pos
        // ShuffleboardLayout hatchCommand = driveTeamTab.getLayout("Commands", BuiltInLayouts.kList).withSize(7, 10)
        //         .withProperties(Map.of("Label position", "HIDDEN")).withPosition(0, 0);
        // hatchCommand.add(new HatchZeroPosition());
    }

    public void periodic() {

        nteIsVectorFound.setBoolean(Robot.vision.isVectorPresent());
        ntePIDSetpointHatch.setDouble(Robot.hatch.getSetpoint());


    }

}
