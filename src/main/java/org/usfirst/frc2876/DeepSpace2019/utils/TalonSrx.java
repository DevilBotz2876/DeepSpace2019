package org.usfirst.frc2876.DeepSpace2019.utils;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * A class that mimics {@link edu.wpi.first.wpilibj.Encoder a WPILib Encoder}
 * sendable properties, but gets data from a {@link TalonSRX} instead of digital
 * input pins on the roboRIO.
 * 
 * See https://www.chiefdelphi.com/t/talonsrx-encoder-values-to-smart-dashboard-shuffleboard/343651/2
 */
public class TalonSrx extends SendableBase {

  private final WPI_TalonSRX talon;

  public TalonSrx(WPI_TalonSRX talon) {
    this.talon = talon;
  }

  public String getControlMode() {
    ControlMode m = talon.getControlMode();
    return m.toString();
  }

  public double getOutput() {
    return talon.get();
  }

  public double getDistance() {
    return talon.getSelectedSensorPosition(0);
  }

  public double getRate() {
    return talon.getSelectedSensorVelocity(0);
  }

  public double getError() {
    return talon.getClosedLoopError(0);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("Encoder");
    builder.addDoubleProperty("Distance", this::getDistance, null);
    builder.addDoubleProperty("Speed", this::getRate, null);
    builder.addStringProperty("Mode", this::getControlMode, null);
    builder.addDoubleProperty("Mode", this::getOutput, null);
    
  }
}