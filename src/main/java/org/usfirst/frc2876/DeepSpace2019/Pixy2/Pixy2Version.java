package org.usfirst.frc2876.DeepSpace2019.Pixy2;

import java.util.Arrays;

public class Pixy2Version {

    Pixy2I2C i2c;
    short hardware;
    byte firmwareMajor;
    byte firmwareMinor;
    short firmwareBuild;
    String firmwareType;
    byte[] rawBytes;

    Pixy2Request request;
    Pixy2Response response;

    byte requestType = 14;
    byte responseType = 15;

    // public Pixy2Version(byte[] rawBytes) {
    //     this.rawBytes = rawBytes;
    //     parseResponse();
    // }

    public Pixy2Version(Pixy2I2C i2c) {
        this.i2c = i2c;
    }

    public boolean get() {
        request = new Pixy2Request(requestType, null);
        if (i2c.send(request.buf())) {
            response = new Pixy2Response(i2c);
            try {
                rawBytes = response.recv();
            } catch (Pixy2Exception ex) {
                System.out.println(ex);
                ex.printStackTrace();
                return false;
            }
            parseResponse();
            return true;
        }
        return false;
    }

    private void parseResponse() {
        // See
        // https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:protocol_reference#getversion
        // or/and
        // https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:porting_guide#the-serial-protocol
        //
        // 0x00 // first byte of hardware version (little endian -> least-significant
        // byte first)
        // 0x22 // second byte of hardware version
        // 0x03 // firmware major version number
        // 0x00 // firmware minor version number
        // 0x0a // first byte of firmware build number (little endian ->
        // least-significant byte first)
        // 0x00 // second byte of firmware build number
        // 0x67 // byte 0 of firmware type ASCII string
        // 0x65 // byte 1 of firmware type ASCII string
        // 0x6e // byte 2 of firmware type ASCII string
        // 0x65 // byte 3 of firmware type ASCII string
        // 0x72 // byte 4 of firmware type ASCII string
        // 0x61 // byte 5 of firmware type ASCII string
        // 0x6c // byte 6 of firmware type ASCII string
        // 0x00 // byte 7 of firmware type ASCII string
        // 0x00 // byte 8 of firmware type ASCII string
        // 0x00 // byte 9 of firmware type ASCII string
        
        if(rawBytes.length != 7){
            return;
        }

        hardware = Pixy2.bytesToShort(rawBytes[1], rawBytes[0]);
        firmwareMajor = rawBytes[2];
        firmwareMinor = rawBytes[3];
        firmwareBuild = Pixy2.bytesToShort(rawBytes[5], rawBytes[4]);
        firmwareType = new String(Arrays.copyOfRange(rawBytes, 6, rawBytes.length));
    }

    void print() {
        String str = String.format("hardware ver: 0x%x firmware ver: %d.%d.%d %s", hardware, firmwareMajor,
                firmwareMinor, firmwareBuild, firmwareType);
        System.out.println(str);
    }

}