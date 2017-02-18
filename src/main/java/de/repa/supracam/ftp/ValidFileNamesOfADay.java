package de.repa.supracam.ftp;

import java.util.HashSet;
import java.util.Set;

public class ValidFileNamesOfADay {
    private String day;
    private Set<ValidFileName> fileNames = new HashSet<>();

    private ValidFileNamesOfADay(String validDayString) {
        this.day = validDayString;
    }

    public boolean addValidFileName(ValidFileName validFileName) {
        if(validFileName.getDayString().equals(this.day)) {
            fileNames.add(validFileName);
            return true;
        }
        return false;
    }

    public String getDay() {
        return day;
    }

    public Set<ValidFileName> getFileNames() {
        return new HashSet<>(this.fileNames);
    }

    public static ValidFileNamesOfADay createValidFileNamesOfDay(String validDayString) throws IllegalDayStringException {
        if (validateDay(validDayString)) {
            return new ValidFileNamesOfADay(validDayString);
        }
        throw new IllegalDayStringException(validDayString);
    }

    private static boolean validateDay(String dayString) {
        return dayString.matches(FileNameRegexes.DATE_REGEX);
    }
}
