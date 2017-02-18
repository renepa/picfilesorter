package de.repa.supracam.files.model.exceptions;

public class IllegalDayStringException extends RuntimeException {
    public IllegalDayStringException(String illegalDayString) {
        super("The day string " + illegalDayString + " is not valid");
    }
}
