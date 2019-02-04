package org.usfirst.frc2876.DeepSpace2019.Pixy2;

// These two links heled figure out how to define this class
// https://docs.oracle.com/javase/tutorial/essential/exceptions/chained.html
// https://stackoverflow.com/questions/3776327/how-to-define-custom-exception-class-in-java-the-easiest-way
public class Pixy2Exception extends Exception {
    private static final long serialVersionUID = 1L;
    public Pixy2Exception() {

    }
    public Pixy2Exception(String msg) {
        super(msg);
    }
    public Pixy2Exception(String msg, Throwable cause) {
        super(msg,cause);
    }
    public Pixy2Exception(Throwable cause) {
        super(cause);
    }
}
