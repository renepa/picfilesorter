package de.repa.supracam.ftp;

public class IllegalFileNameException extends Exception {
    public IllegalFileNameException(String illegalFileName) {
        super("Found illegal filename '" + illegalFileName + "'");
    }
}
