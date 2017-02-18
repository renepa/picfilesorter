package de.repa.supracam.ftp;

public class IllegalFileNameException extends RuntimeException {
    public IllegalFileNameException(String illegalFileName) {
        super("Found illegal filename '" + illegalFileName + "'");
    }
}
