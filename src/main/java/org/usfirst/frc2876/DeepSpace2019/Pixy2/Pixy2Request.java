package org.usfirst.frc2876.DeepSpace2019.Pixy2;
import java.util.Arrays;

public class Pixy2Request {
    
    private byte[] payload;
    private byte type;
    private byte[] request;

    public Pixy2Request(byte type, byte[] payload) {
        this.type = type;
        this.payload = payload;
        this.request = build();
    }

    public boolean send(Pixy2I2C i2c) {
        return i2c.send(request);
    }

    public byte[] buf() {
        return request;
    }

    // Put all the bytes needed to send a request to pixy2 in a byte buffer.
    private byte[] build() {
        // https://github.com/charmedlabs/pixy2/blob/master/src/host/arduino/libraries/Pixy2/TPixy2.h#L266
        int len = 0;
        if (payload != null) {
            len = payload.length;
        }
        byte[] header = { (byte) 0xAE, (byte) 0xC1, type, (byte)len };
        
        // https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#copyOf-byte:A-int-
        request = Arrays.copyOf(header, header.length + len);
        if (payload != null) {
           // https://docs.oracle.com/javase/8/docs/api/java/lang/System.html#arraycopy-java.lang.Object-int-java.lang.Object-int-int-
            System.arraycopy(payload, 0, request, header.length, len);
        }
        return request;
    }

}