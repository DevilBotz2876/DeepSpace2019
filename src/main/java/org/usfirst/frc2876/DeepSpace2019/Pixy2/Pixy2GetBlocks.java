package org.usfirst.frc2876.DeepSpace2019.Pixy2;

public class Pixy2GetBlocks {

    Pixy2I2C i2c;
    byte[] rawBytes;

    Pixy2Request request;
    Pixy2Response response;

    byte requestType = 32;
    byte responseType = 33;
    byte sigMap;
    byte maxBlocks;
  

    public Pixy2GetBlocks(Pixy2I2C i2c, byte sigMap, byte maxBlocks) {
        this.i2c = i2c;
        this.sigMap = sigMap;
        this.maxBlocks = maxBlocks;
    }

    public boolean get() {
        byte[] payload = {this.sigMap, this.maxBlocks};
        
        request = new Pixy2Request(requestType, payload);
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

        // https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:protocol_reference#color-connected-components-packet-reference
        
    }

}