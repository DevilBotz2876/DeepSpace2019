package org.usfirst.frc2876.DeepSpace2019.Pixy2;

import java.util.Arrays;

public class Pixy2Block {
    public final static int CCC_MAX_SIGNATURE = 7;
    
    private int signature;
    private int x;
    private int y;
    private int width;
    private int height;
    private int angle;
    private int index;
    private int age;

    /**
     * Constructs signature block instance
     * 
     * @param signature Block signature
     * @param x         X value
     * @param y         Y value
     * @param width     Block width
     * @param height    Block height
     * @param angle     Angle from camera
     * @param index     Block index
     * @param age       Block age
     */
    public Pixy2Block(int signature, int x, int y, int width, int height, int angle, int index, int age) {
        this.signature = signature;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.index = index;
        this.age = age;
    }

    /**
     * Prints signature block data to console
     */
    public void print() {
        System.out.println(toString());
    }

    /**
     * Creats a string of signature data
     * 
     * @return String of signature data
     */
    public String toString() {
        int i, j;
        int[] sig = new int[6];
        int d;
        boolean flag;
        String out = "";
        if (signature > CCC_MAX_SIGNATURE) {
            // color code! (CC)
            // convert signature number to an octal string
            for (i = 12, j = 0, flag = false; i >= 0; i -= 3) {
                d = (signature >> i) & 0x07;
                if (d > 0 && !flag)
                    flag = true;
                if (flag)
                    sig[j++] = d + '0';
            }
            sig[j] = '\0';
            out = "CC block sig: " + Arrays.toString(sig) + " (" + signature + " decimal) x: " + x + " y: " + y
                    + " width: " + width + " height: " + height + " angle: " + angle + " index: " + index + " age: "
                    + age;

        } else // regular block. Note, angle is always zero, so no need to print
            out = "sig: " + signature + " x: " + x + " y: " + y + " width: " + width + " height: " + height
                    + " index: " + index + " age: " + age;
        return out;
    }

    /**
     * @return Block signature
     */
    public int getSignature() {
        return signature;
    }

    /**
     * @return Block X value
     */
    public int getX() {
        return x;
    }

    /**
     * @return Block Y value
     */
    public int getY() {
        return y;
    }

    /**
     * @return Block width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Block height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return Angle from camera
     */
    public int getAngle() {
        return angle;
    }

    /**
     * @return Block index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return Block age
     */
    public int getAge() {
        return age;
    }

}
