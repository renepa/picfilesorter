package de.repa.filesorter.files.model;

import de.repa.filesorter.files.model.exceptions.IllegalDayStringException;

import java.util.HashSet;
import java.util.Set;

public class FilesByDayDirectory {
    private String day;
    private Set<ValidFileName> fileNames = new HashSet<>();

    private FilesByDayDirectory(String validDayString) {
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

    public static FilesByDayDirectory createValidFileNamesOfDay(String validDayString) throws IllegalDayStringException {
        if (validateDay(validDayString)) {
            return new FilesByDayDirectory(validDayString);
        }
        throw new IllegalDayStringException(validDayString);
    }

    private static boolean validateDay(String dayString) {
        return dayString.matches(Regex.DATE_REGEX);
    }
}
