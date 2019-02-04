package org.usfirst.frc2876.DeepSpace2019.Pixy2;

public class Pixy2 {
    Pixy2I2C i2c;

    public Pixy2(String name, int address) {
        i2c = new Pixy2I2C(name, address);
    }

    // https://stackoverflow.com/questions/736815/2-bytes-to-short-java
    public static short bytesToShort(byte upper, byte lower) {
        // return (((int) upper & 0xff) << 8) | ((int) lower & 0xff);
        return (short)(((upper & 0xFF) << 8) | (lower & 0xFF));
    }

    public static void printBytes(String msg, byte[] b) {
        System.out.print(msg + ": ");
        for (int i = 0; i < b.length; i++) {
            System.out.format("%02X ", b[i]);
        }
        System.out.println();
    }

    public static String bytesToString(byte[] b) {
        // Each byte takes 2 chars and a space after
        StringBuffer strbuf = new StringBuffer(b.length*3);
        for (int i = 0; i < b.length; i++) {
            strbuf.append(String.format("%02X ", b[i]));            
        }
        return strbuf.toString();
    }


    public void version() {
        Pixy2Version v = new Pixy2Version(i2c);
        v.print();
    }
    public void setLed() {
        byte r=1;
        byte g=1;
        byte b=1;
        
        Pixy2SetLED sl = new Pixy2SetLED(i2c, r, g, b);
        sl.toString();
        
    }

    public Pixy2Vector[] getVectors() throws Pixy2Exception {
        Pixy2GetMainFeatures mf = new Pixy2GetMainFeatures(i2c);
        mf.get();
        return mf.vectors;
    }
}