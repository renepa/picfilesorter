package de.repa.supracam.ftp;

import java.util.HashSet;
import java.util.Set;

public class ValidFileNamesOfADay {
    private String day;
    private Set<String> fileNames = new HashSet<>();

    private ValidFileNamesOfADay(String validDayString) {
        this.day = validDayString;
    }

    public boolean addValidFileName(String validFileName) {
        if (validFileName.matches(FileNameRegexes.FILE_REGEX) && validFileName.contains(day)) {
            this.fileNames.add(validFileName);
            return true;
        }
        return false;
    }

    public String getDay() {
        return day;
    }

    public Set<String> getFileNames() {
        return new HashSet<>(this.fileNames);
    }

    public static ValidFileNamesOfADay createValidFileNamesOfDay(String validDayString) throws IllegalDayStringException {
        if (validateDay(validDayString)) {
            return new ValidFileNamesOfADay(validDayString);
        }
        throw new IllegalDayStringException(validDayString);
    }

    public static ValidFileNamesOfADay createValidFileNamesOfDay(String validDayString, Set<String> fileNames) throws IllegalDayStringException {
        ValidFileNamesOfADay validFileNamesOfDay = createValidFileNamesOfDay(validDayString);
        fileNames.forEach(fileName -> validFileNamesOfDay.addValidFileName(fileName));
        return validFileNamesOfDay;
    }

    private static boolean validateDay(String dayString) {
        return dayString.matches(FileNameRegexes.DATE_REGEX);
    }
}
