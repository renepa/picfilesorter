package de.repa.supracam.files.exceptions;

public class IllegalFileNameException extends RuntimeException {
    public IllegalFileNameException(String illegalFileName) {
        super("Found illegal filename '" + illegalFileName + "'");
    }
}
