
package org.usfirst.frc2876.DeepSpace2019.Pixy2;

public class Pixy2GetMainFeatures {

    Pixy2I2C i2c;
    byte[] rawBytes;
    Pixy2Request request;
    Pixy2Response response;
    public Pixy2Vector[] vectors;

    public static final byte LINE_VECTOR = 1;
    public static final byte VECTOR_LENGTH = 6;

    byte requestType = 48;
    byte responseType = 49;

    public Pixy2GetMainFeatures(Pixy2I2C i2c) {
        this.i2c = i2c;
        vectors = null;
    }

    public void get() throws Pixy2Exception {
        //https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:line_api
        //https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:protocol_reference#getmainfeatures-features-wait
        byte[] payload = {0, 1};
        request = new Pixy2Request(requestType, payload);
        if (i2c.send(request.buf())) {            
            response = new Pixy2Response(i2c);
            rawBytes = response.recv();
            if (response.m_type != responseType) {
                throw new Pixy2Exception(String.format("Response 0x%02X did match request 0x%02X. %s", response.m_type, responseType, response));
            }
            parseResponse();
        }
    }

    private void parseResponse() {
        // see https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:protocol_reference#getmainfeatures-features-wait
        if (rawBytes.length == 0) {
            return;
        }
        //Pixy2.printBytes("getMainFeatures reply:", rawBytes); 
        //getMainFeatures reply:: 01 06 2C 07 3A 0D AB 04

        if(rawBytes[0] == LINE_VECTOR){
            //https://github.com/charmedlabs/pixy2/blob/master/src/host/arduino/libraries/Pixy2/Pixy2Line.h#L180
            int numVectors = rawBytes[1]/VECTOR_LENGTH;
            vectors = new Pixy2Vector[numVectors];
            for(int i = 0; i < numVectors; i++){
                byte[] buf = new byte[VECTOR_LENGTH];
                int offset = (VECTOR_LENGTH * i) + 2;
                System.arraycopy(rawBytes, offset, buf, 0, VECTOR_LENGTH);
                Pixy2Vector v = new Pixy2Vector(buf);
                vectors[i] = v;
                //v.print();
            }
        }
        
    }

}