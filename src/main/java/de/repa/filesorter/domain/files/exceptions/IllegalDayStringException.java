package de.repa.filesorter.domain.files.exceptions;

public class IllegalDayStringException extends RuntimeException {
    public IllegalDayStringException(String illegalDayString) {
        super("The day string " + illegalDayString + " is not valid");
    }
}
