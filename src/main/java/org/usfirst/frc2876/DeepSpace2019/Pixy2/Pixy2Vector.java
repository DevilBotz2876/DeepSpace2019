package org.usfirst.frc2876.DeepSpace2019.Pixy2;

public class Pixy2Vector {
    // TODO fill out implementation based on:
    // https://github.com/charmedlabs/pixy2/blob/2adc6caba774a3056448d0feb0c6b89855a392f4/src/host/arduino/libraries/Pixy2/Pixy2Line.h#L49

    // Hints
    // - use String.format to replace sprintf
    // - Make everything public, don't make get/set methods.
    // - use int instead of uint8_t type
    public Pixy2Vector(byte[] vector) {
        m_x0 = vector[0];
        m_y0 = vector[1];
        m_x1 = vector[2];
        m_y1 = vector[3];
        m_index = vector[4];
        m_flags = vector[5];
    }
    void print() {
        System.out.printf("vector: (%d %d) (%d %d) index: %d flags %d", m_x0, m_y0, m_x1, m_y1, m_index, m_flags);
        System.out.println();
    }

    int m_x0;
    int m_y0;
    int m_x1;
    int m_y1;
    int m_index;
    int m_flags;
}