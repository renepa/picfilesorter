package de.repa.filesorter.domain.files;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FilesByDayDirectoryFactory {

    public Set<FilesByDayDirectory> createFilesByDayDirectories(List<ValidFileName> fileNames) {
        Map<String, FilesByDayDirectory> tempOrderMap = new HashMap<>();
        Set<FilesByDayDirectory> resultSet = new HashSet<>();
        for (ValidFileName fileName : fileNames) {
            String dayString = fileName.getDayString();
            if (!tempOrderMap.containsKey(dayString)) {
                FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay(dayString);
                validFileNamesOfDay.addValidFileName(fileName);
                resultSet.add(validFileNamesOfDay);
                tempOrderMap.put(dayString, validFileNamesOfDay);
            } else {
                tempOrderMap.get(dayString).addValidFileName(fileName);
            }
        }
        return resultSet;
    }
}
