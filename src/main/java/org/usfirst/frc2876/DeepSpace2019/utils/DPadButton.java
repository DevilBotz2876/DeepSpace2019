package org.usfirst.frc2876.DeepSpace2019.utils;


import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;

public class DPadButton extends Button {

    GenericHID joystick;
    Direction direction;

    public DPadButton(XboxController joystick, Direction direction) {
        this.joystick = joystick;
        this.direction = direction;
    }

    public DPadButton(XboxController joystick) {
        this.joystick = joystick;
        this.direction = Direction.UP;
    }

    public static enum Direction {
        UP(0), RIGHT(90), DOWN(180), LEFT(270);

        int direction;

        private Direction(int direction) {
            this.direction = direction;
        }
    }

    public boolean get() {
        int dPadValue = joystick.getPOV();
        return (dPadValue == direction.direction) || (dPadValue == (direction.direction + 45) % 360)
                || (dPadValue == (direction.direction + 315) % 360);
    }

}