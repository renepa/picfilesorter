package de.repa.supracam.files.model.exceptions;

public class IllegalFileNameException extends RuntimeException {
    public IllegalFileNameException(String illegalFileName) {
        super("Found illegal filename '" + illegalFileName + "'");
    }
}
