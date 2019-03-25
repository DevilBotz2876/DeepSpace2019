package org.usfirst.frc2876.DeepSpace2019.Pixy2;

public class Pixy2ChangeProg {

    Pixy2I2C i2c;
    byte[] rawBytes;

    Pixy2Request request;
    Pixy2Response response;

    Mode mode;

    byte requestType = 2;
    byte responseType = 1;
    final int PIXY_MAX_PROGNAME = 33;

    public static enum Mode {
        CCC(0), LINE(1), VIDEO(2);

        int mode;

        private Mode(int mode) {
            this.mode = mode;
        }

        public String toString() {
            switch (mode) {
            case 0:
                return "color_connected_components";
            case 1:
                return "line_tracking";
            case 2:
                return "video";
            default:
                return null;
            }
        }
    }

    public Pixy2ChangeProg(Pixy2I2C i2c, Mode mode) {
        this.i2c = i2c;
        this.mode = mode;
    }

    public boolean get() {
        // byte[] payload = new byte[PIXY_MAX_PROGNAME];
        byte[] payload = this.mode.toString().getBytes();
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
        // It is hard to tell what args this request should take. Need to search
        // examples in charmedlabs repo to find out. There is no description in protocol
        // ref about how to change mode/prog.
        //
        // https://github.com/charmedlabs/pixy2/blob/master/src/host/arduino/libraries/Pixy2/TPixy2.h#L278
        // https://github.com/charmedlabs/pixy2/search?q=changeProg&unscoped_q=changeProg

    }

}