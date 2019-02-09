package org.usfirst.frc2876.DeepSpace2019;

// import org.usfirst.frc2876.DeepSpace2019.commands.TestPixy;
import org.usfirst.frc2876.DeepSpace2019.commands.AutonomousCommand;
import org.usfirst.frc2876.DeepSpace2019.commands.HatchDown;
import org.usfirst.frc2876.DeepSpace2019.commands.HatchStop;
import org.usfirst.frc2876.DeepSpace2019.commands.HatchUp;
import org.usfirst.frc2876.DeepSpace2019.commands.TestPixy;// import org.usfirst.frc2876.DeepSpace2019.commands.TestPixy;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());

    public XboxController xboxController;

    //Button rJoyButton;
    //Button lJoyButton;

    public OI() {
        xboxController = new XboxController(0);

        // SmartDashboard Buttons
        SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
        SmartDashboard.putData("testPixy", new TestPixy());
        SmartDashboard.putData("Hatch Stop", new HatchStop());
        SmartDashboard.putData("Hatch Up", new HatchUp());
        SmartDashboard.putData("Hatch Down", new HatchDown());
/* 
        lJoyButton = new JoystickButton(xboxController, LJOY_BUTTON);
        lJoyButton.whileHeld(new HatchDown());
        
        rJoyButton = new JoystickButton(xboxController, RJOY_BUTTON);
        rJoyButton.whileHeld(new HatchUp()); */
    }

    public XboxController getXboxController() {
        return xboxController;
    }

    // TODO Not sure if we need any of these methods if using the XboxController class
    //
    // public double getLeftX() {
	// 	return xboxController.getRawAxis(LEFT_X_AXIS);
	// }

	// public double getLeftY() {
	// 	return xboxController.getRawAxis(LEFT_Y_AXIS);
	// }

	// public double getRightX() {
	// 	return xboxController.getRawAxis(RIGHT_X_AXIS);
	// }

	// public double getRightY() {
	// 	return xboxController.getRawAxis(RIGHT_Y_AXIS);
	// }

	// public double getLeftTrigger() {
	// 	return xboxController.getRawAxis(LEFT_TRIGGER);
	// }

	// public double getRightTrigger() {
	// 	return xboxController.getRawAxis(RIGHT_TRIGGER);
	// }

	// public static final int LEFT_X_AXIS = 0, LEFT_Y_AXIS = 1, LEFT_TRIGGER = 2, RIGHT_TRIGGER = 3, RIGHT_X_AXIS = 4,
	// 		RIGHT_Y_AXIS = 5, DPAD_X_AXIS = 6, DPAD_Y_AXIS = 7;
	
	// public static final int LEFT_BUMPER = 5,
	// 		RIGHT_BUMPER = 6,
	// 		A_BUTTON=1,
	// 		B_BUTTON=2,
	// 		X_BUTTON=3,
	// 		Y_BUTTON=4,
	// 		START_BUTTON=8,
	// 		LJOY_BUTTON=9,
    // 		RJOY_BUTTON=10;
    


}

