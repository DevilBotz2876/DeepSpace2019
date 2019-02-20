package org.usfirst.frc2876.DeepSpace2019.utils;

import edu.wpi.first.wpilibj.DigitalInput;

public class RobotSettings {

    DigitalInput dioPort;

    private int practiceOrCompBotJumper = 2;

    public RobotSettings() {
        dioPort = new DigitalInput(practiceOrCompBotJumper);
    }

    public RobotSettings(int dioChannel) {
        dioPort = new DigitalInput(dioChannel);
    }

    // Check digital IO port to determine if practice bot or not. Practice bot
    // should have a jumper present on port/pins. Comp bot should not. Safer to
    // default to comp bot.  Test that this all works on practice bot.
    public boolean isCompBot() {
        // If digital port stuff doesn't work as expected, just make this return true
        // all the time if working on compbot, return false if on practice bot.
        boolean isCB = dioPort.get();
        return isCB;
    }

}