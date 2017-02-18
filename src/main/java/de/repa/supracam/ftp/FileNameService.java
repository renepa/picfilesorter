package de.repa.supracam.ftp;

import java.util.*;

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

    public Set<ValidFileNamesOfADay> groupFileNamesByDay(List<ValidFileName> fileNames) {
        Map<String, ValidFileNamesOfADay> tempOrderMap = new HashMap<>();
        Set<ValidFileNamesOfADay> resultSet = new HashSet<>();
        for (ValidFileName fileName : fileNames) {
            String dayString = fileName.getDayString();
            if (!tempOrderMap.containsKey(dayString)) {
                ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(dayString);
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
