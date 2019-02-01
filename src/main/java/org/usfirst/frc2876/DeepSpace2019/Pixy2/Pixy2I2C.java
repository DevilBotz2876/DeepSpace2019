package org.usfirst.frc2876.DeepSpace2019.Pixy2;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class Pixy2I2C {
    String name;
    I2C i2c;
    Port port = Port.kOnboard;

    public Pixy2I2C(String id, int address) {
        i2c = new I2C(port, address);
        name = "Pixy_" + id;
    }

    // This method parses raw data from the pixy into readable integers
    public int cvt(byte upper, byte lower) {
        return (((int) upper & 0xff) << 8) | ((int) lower & 0xff);
    }

    public boolean recv(byte[] buf) {
        try {
            return !i2c.readOnly(buf, buf.length);
        } catch (RuntimeException e) {
            // SmartDashboard.putString(name + "Status", e.toString());
            System.out.println(name + "  " + e);
            return false;
        }
    }

    public boolean send(byte[] buf) {
        return !i2c.writeBulk(buf);
    }

    /*
     * This is our original test/bring-up method to see if we can talk to pixy2.
     * This method is useful if you want to test basic connectivity with pixy2 over
     * i2c. Do not use this method unless you are testing talking to pixy2.
     */
    public void version() {
        /*
         * https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:porting_guide#the-serial-
         * protocol
         * 
         * Write some code that calls send() to send version cmd to pixy2 Then write
         * some code that calls recv() to get response Parse the response and print out
         * the version. The link above has example/steps to do this.
         * 
         * Use printBytes to print out what is being sent and recv to help
         * understand/debug problems.
         * 
         * GET_VERSION defines the bytes you need to send.
         */
        byte[] GET_VERSION = new byte[] { (byte) 0xAE, (byte) 0xc1, 0x0e, 0 };

        System.out.println("Version Started");
     
        Pixy2.printBytes("GET_VERSION", GET_VERSION);
        boolean t = send(GET_VERSION);
        System.out.println("send returned: " + t);

        byte[] resp = new byte[6 + 16];
        boolean b = recv(resp);
        System.out.println("recv returned: " + b);
        Pixy2.printBytes("GET_VERSION resp", resp);
    }
}