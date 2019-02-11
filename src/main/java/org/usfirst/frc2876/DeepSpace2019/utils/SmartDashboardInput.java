package org.usfirst.frc2876.DeepSpace2019.utils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// See https://github.com/blair-robot-project/449-central-repo/blob/master/RoboRIO/src/main/java/org/usfirst/frc/team449/robot/generalInterfaces/doubleUnaryOperator/RampComponent.java
public class SmartDashboardInput {
    /**
	 * Initialize value on SmartDashboard for user input, but leave old value if already present.
	 *
	 * @param key The SmartDashboard key to associate with the value.
	 * @param defValue The default value to assign if not already on dashboard.
	 *
	 * @return The current value that appears on the dashboard.
	 */
	public static double createNumber(String key, double defValue) {

        // See if already on dashboard, and if so, fetch current value
        double value = SmartDashboard.getNumber(key, defValue);
  
        // Make sure value is on dashboard, puts back current value if already set
        // otherwise puts back default value
        SmartDashboard.putNumber(key, value);
  
        return value;
      }
      
      /**
       * Initialize value on SmartDashboard for user input, but leave old value if already present.
       *
       * @param key The SmartDashboard key to associate with the value.
       * @param defValue The default value to assign if not already on dashboard.
       *
       * @return The current value that appears on the dashboard.
       */
      public static String createString(String key, String defValue) {
  
        // See if already on dashboard, and if so, fetch current value
        String value = SmartDashboard.getString(key, defValue);
  
        // Make sure value is on dashboard, puts back current value if already set
        // otherwise puts back default value
        SmartDashboard.putString(key, value);
  
        return value;
      }
      
      public static void putCommand(Command c){
          SmartDashboard.putString("DriveTrain Current Command", c.toString());
          SmartDashboard.putString("DriveTrain Current Command Group", c.getGroup().toString());
      }
}