import edu.wpi.first.wpilibj.DigitalInput;

public class RobotSettings {

    DigitalInput dioPort;

    public RobotSettings(int dioChannel) {
        dioPort = new DigitalInput(dioChannel);
    }

    public boolean isCompBot() {
        return dioPort.get();
    }

    
}