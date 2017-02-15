package de.repa.supracam.ftp;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileNameService {

    public List<String> filerValidFileNames(List<String> fileNamesToValidate) {
        if (fileNamesToValidate != null && !fileNamesToValidate.isEmpty()) {
            return fileNamesToValidate.stream()
                    .filter(fileName -> fileName.matches(FileNameRegexes.FILE_REGEX))
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public Map<String, Set<String>> groupFileNamesByDay(List<String> fileNames) {
        Map<String, Set<String>> resultMap = new HashMap<>();
        for (String fileName : fileNames) {
            String dateOfFileName = getDateOfFileName(fileName);
            addFileNameToMapByDateOfFile(resultMap, fileName, dateOfFileName);
        }
        return resultMap;
    }

    private void addFileNameToMapByDateOfFile(Map<String, Set<String>> resultMap, String fileName, String dateOfFileName) {
        if (!resultMap.containsKey(dateOfFileName)) {
            resultMap.put(dateOfFileName, new HashSet<String>(Arrays.asList(fileName)));
        } else {
            resultMap.get(dateOfFileName).add(fileName);
        }
    }

    private String getDateOfFileName(String fileName) {
        Pattern datePatter = Pattern.compile(FileNameRegexes.DATE_REGEX);
        Matcher matcher = datePatter.matcher(fileName);
        if (matcher.find()) {
            return matcher.group();
        }
        return "undefined";
    }
}
