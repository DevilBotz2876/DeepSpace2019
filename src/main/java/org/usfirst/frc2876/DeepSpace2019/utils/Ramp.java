package org.usfirst.frc2876.DeepSpace2019.utils;

import edu.wpi.first.wpilibj.RobotController;


// See https://github.com/blair-robot-project/449-central-repo/blob/master/RoboRIO/src/main/java/org/usfirst/frc/team449/robot/generalInterfaces/doubleUnaryOperator/RampComponent.java
public class Ramp {
    private double lastTime;
    private double lastValue;
    private double maxChangePerMilli;

    public Ramp(double maxChangePerSecond) {
        setMaxChangePerSecond(maxChangePerSecond);
    }

    private double timeNowMilli() {
        double microSeconds = RobotController.getFPGATime();
        return microSeconds/1000;
    }
    
    public double get(double value) {
        double now = timeNowMilli();
        if (value > lastValue) {
            lastValue = Math.min(value, lastValue + (now - lastTime) * maxChangePerMilli);
        } else {
            lastValue = Math.max(value, lastValue - (now - lastTime) * maxChangePerMilli);
        }
        lastTime = timeNowMilli();
        return lastValue;
    }

    public void setMaxChangePerSecond(double maxChangePerSecond) {
        this.maxChangePerMilli = maxChangePerSecond/1000;
    }
}