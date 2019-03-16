package org.usfirst.frc2876.DeepSpace2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class CGDriveOffHatch extends CommandGroup {

        public CGDriveOffHatch() {
                // TODO may need to adjust ramp rate if acceleration is too slow. Remember to
                // set it back to original value if changing it. Better to set it to original
                // value in xboxDrive command.
                //
                addSequential(new ArmUpDrive(), 1.0);
                addSequential(new DriveHatch(), 1.0);
               
        }
}
