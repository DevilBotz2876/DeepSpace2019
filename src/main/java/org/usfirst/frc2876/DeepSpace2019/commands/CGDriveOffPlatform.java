package org.usfirst.frc2876.DeepSpace2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc2876.DeepSpace2019.*;

/**
 *
 */
public class CGDriveOffPlatform extends CommandGroup {

        public CGDriveOffPlatform() {
                // TODO may need to adjust ramp rate if acceleration is too slow. Remember to
                // set it back to original value if changing it. Better to set it to original
                // value in xboxDrive command.
                //
                addSequential(new ArmUpDrive(), .5);
                addSequential(new DriveForward(), 1.0);
                addSequential(new HatchPosition(Robot.hatch.CARRY), .5);
                // addParallel(new WaitCommand(3));
                // addSequential(new DriveStop());
                // addSequential(new DriveRotate(180.0));
        }
}
