package org.usfirst.frc2876.DeepSpace2019.Pixy2;

import java.util.ArrayList;

public class Pixy2GetBlocks {

    Pixy2I2C i2c;
    byte[] rawBytes;

    Pixy2Request request;
    Pixy2Response response;

    byte requestType = 32;
    byte responseType = 33;
    byte sigMap;
    byte maxBlocks;

    private ArrayList<Pixy2Block> blocks;
  

    public Pixy2GetBlocks(Pixy2I2C i2c, byte sigMap, byte maxBlocks) {
        this.i2c = i2c;
        this.sigMap = sigMap;
        this.maxBlocks = maxBlocks;
        this.blocks = new ArrayList<Pixy2Block>();
    }

    public void get() throws Pixy2Exception {
        byte[] payload = {this.sigMap, this.maxBlocks};
        
        request = new Pixy2Request(requestType, payload);
        if (i2c.send(request.buf())) {
            response = new Pixy2Response(i2c);
            rawBytes = response.recv();
            if (response.m_type != responseType) {
                throw new Pixy2Exception(String.format("Response 0x%02X did match request 0x%02X. %s", response.m_type, responseType, response));
            }
            parseResponse();
            return;
        }
        return;
    }

    private void parseResponse() {

        // https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:protocol_reference#color-connected-components-packet-reference
        for (int i = 0; i < rawBytes.length; i += 14) {
            Pixy2Block b = new Pixy2Block(
                Pixy2.bytesToShort(rawBytes, i),
                Pixy2.bytesToShort(rawBytes, i+2),
                Pixy2.bytesToShort(rawBytes, i+4),
                Pixy2.bytesToShort(rawBytes, i+6),
                Pixy2.bytesToShort(rawBytes, i+8),
                Pixy2.bytesToShort(rawBytes, i+10),
                Pixy2.bytesToShort(rawBytes, i+12),
                Pixy2.bytesToShort(rawBytes, i+14)
            );
                
                // ((rawBytes[i + 1] & 0xff) << 8) | (rawBytes[i] & 0xff),
                //     ((rawBytes[i + 3] & 0xff) << 8) | (rawBytes[i + 2] & 0xff),
                //     ((rawBytes[i + 5] & 0xff) << 8) | (rawBytes[i + 4] & 0xff),
                //     ((rawBytes[i + 7] & 0xff) << 8) | (rawBytes[i + 6] & 0xff),
                //     ((rawBytes[i + 9] & 0xff) << 8) | (rawBytes[i + 8] & 0xff),
                //     ((rawBytes[i + 11] & 0xff) << 8) | (rawBytes[i + 10] & 0xff),
                //     (rawBytes[i + 12] & 0xff), (rawBytes[i + 13] & 0xff));
            blocks.add(b);
        }
    }

}