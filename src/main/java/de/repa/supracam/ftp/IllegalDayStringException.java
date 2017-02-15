package de.repa.supracam.ftp;

public class IllegalDayStringException extends RuntimeException {
    public IllegalDayStringException(String illegalDayString) {
        super("The day string " + illegalDayString + " is not valid");
    }
}
