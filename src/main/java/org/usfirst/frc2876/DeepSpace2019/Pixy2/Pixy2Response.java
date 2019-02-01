package org.usfirst.frc2876.DeepSpace2019.Pixy2;

public class Pixy2Response {

    public static final boolean PIXY_RESULT_OK = true;
    public static final boolean PIXY_RESULT_ERROR = false;
    public static final boolean PIXY_RESULT_CHECKSUM_ERROR = false;
    public static final short PIXY_CHECKSUM_SYNC = (short) 0xc1af;
    public static final short PIXY_NO_CHECKSUM_SYNC = (short) 0xc1ae;

    Pixy2I2C i2c;
    boolean m_cs = false;
    public short checksum;
    public byte m_type;
    // byte m_length;
    public byte[] payload;

    public Pixy2Response(Pixy2I2C i2c) {
        this.i2c = i2c;
    }

    public Pixy2Response() {
        this.i2c = null;
    }

    private short calcChecksum() {
        if (!m_cs) {
            return 0;
        }
        short cs = 0;
        for (int i = 0; i < payload.length; i++) {
            cs += payload[i];
        }
        return cs;
    }

    private void getSync() throws Pixy2Exception {
        // implement this here
        // https://github.com/charmedlabs/pixy2/blob/master/src/host/arduino/libraries/Pixy2/TPixy2.h#L169

        int i, j;
        boolean res;

        short start;
        byte[] buf = new byte[1];
        byte byteCur = 0;
        byte bytePrev = 0;
        // parse bytes until we find sync
        for (i = j = 0; true; i++) {
            // System.out.format("sync: %02X %02X\n", bytePrev, byteCur);
            res = i2c.recv(buf);
            if (res == PIXY_RESULT_OK) {
                byteCur = buf[0];
                // since we're using little endian, previous byte is least significant byte
                // start = cprev;
                // current byte is most significant byte
                // start |= c << 8;
                start = Pixy2.bytesToShort(byteCur, bytePrev);

                // System.out.format("sync: %02X %02X\n", bytePrev, byteCur);
                System.out.format("sync: %02X %02X start: %02X %02X %02X\n", bytePrev, byteCur, start,
                        PIXY_CHECKSUM_SYNC, PIXY_NO_CHECKSUM_SYNC);
                System.out.println("start " + (short) start + " == " + PIXY_CHECKSUM_SYNC);

                bytePrev = byteCur;
                // cprev = c;
                if (start == PIXY_CHECKSUM_SYNC) {
                    m_cs = true;
                    return;
                }
                if (start == PIXY_NO_CHECKSUM_SYNC) {
                    m_cs = false;
                    return;
                }
            }
            // If we've read some bytes and no sync, then wait and try again.
            // And do that several more times before we give up.
            // Pixy guarantees to respond within 100us.
            if (i >= 4) {
                if (j >= 4) {
                    throw new Pixy2Exception("Failed to find sync bytes");
                }
                // TODO add a Thread.sleep here
                // delayMicroseconds(25);
                j++;
                i = 0;
            }
        }
    }

    public byte[] recv() throws Pixy2Exception {
        // https://github.com/charmedlabs/pixy2/blob/master/src/host/arduino/libraries/Pixy2/TPixy2.h#L217
        // int csCalc, csSerial;
        boolean res;

        getSync();
        if (m_cs) {
            byte[] buf = new byte[4];
            res = i2c.recv(buf);
            if (res != PIXY_RESULT_OK) {
                throw new Pixy2Exception("Failed to read response header with checksum.");
            }
            m_type = buf[0];
            byte length = buf[1];
            if(length == 0){
                return new byte[0];
            }
            payload = new byte[length];
            res = i2c.recv(payload);
            if (res != PIXY_RESULT_OK) {
                throw new Pixy2Exception("Failed to read reponse data");
            }

            // https://github.com/charmedlabs/pixy2/blob/master/src/host/arduino/libraries/Pixy2/Pixy2I2C.h#L56
            short csCalc = calcChecksum();
            short csSerial = Pixy2.bytesToShort(buf[3], buf[2]);
            if (csSerial != csCalc) {
                //throw new Pixy2Exception(String.format("Checksums did not match: calc=%d resp=%d(%02X %02X)",
                        //csCalc, csSerial, buf[2], buf[3]));
                System.out.println(String.format("Checksums did not match: calc=%d resp=%d(%02X %02X)",
                csCalc, csSerial, buf[2], buf[3]));

                
            }
        } else {
            byte[] buf = new byte[2];
            res = i2c.recv(buf);
            if (res != PIXY_RESULT_OK) {
                throw new Pixy2Exception("Failed to read response header without checksum.");
            }
            m_type = buf[0];
            byte length = buf[1];

            payload = new byte[length];
            res = i2c.recv(payload);
            if (res != PIXY_RESULT_OK) {
                throw new Pixy2Exception("Failed to read reponse data");
            }
        }
        return payload;
    }
}
