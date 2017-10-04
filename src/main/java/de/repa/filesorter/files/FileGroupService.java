package de.repa.filesorter.files;

import de.repa.filesorter.files.model.FilesByDayDirectory;
import de.repa.filesorter.files.model.ValidFileName;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FileGroupService {

    public Set<FilesByDayDirectory> groupFileNamesByDay(List<ValidFileName> fileNames) {
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
