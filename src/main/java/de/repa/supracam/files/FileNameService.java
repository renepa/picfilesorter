package de.repa.supracam.files;

import de.repa.supracam.files.model.FilesByDayDirectory;
import de.repa.supracam.files.model.ValidFileName;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FileNameService {

    public List<ValidFileName> filerValidFileNames(List<String> fileNamesToValidate) {
        List<ValidFileName> resultList = new ArrayList<>();
        if (fileNamesToValidate != null) {
            for (String fileName : fileNamesToValidate) {
                Optional<ValidFileName> build = ValidFileName.builder().build(fileName);
                if (build.isPresent()) {
                    resultList.add(build.get());
                }
            }
        }
        return resultList;
    }

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
