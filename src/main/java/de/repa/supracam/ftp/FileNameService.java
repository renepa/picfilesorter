package de.repa.supracam.ftp;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileNameService {
    private static final String DATE_REGEX =
            "^[1-2][0-9]{3}"
                    + "_(0?[1-9]|[1][0-2])"
                    + "_([1-2][0-9]|3[0-1]|0?[1-9])";

    private static final String TIME_REGEX =
            "(1[0-9]|2[0-3]|0?[0-9])"
                    + "_(0?[0-9]|[1-5][0-9])"
                    + "_(0?[0-9]|[1-5][0-9])";

    private static final String FILE_REGEX = DATE_REGEX + "_" + TIME_REGEX + "\\.jpg";


    public List<String> filerValidFileNames(List<String> fileNamesToValidate) {
        if (fileNamesToValidate != null && !fileNamesToValidate.isEmpty()) {
            return fileNamesToValidate.stream()
                    .map(filename -> filename.substring(filename.lastIndexOf('/') + 1))
                    .filter(fileName -> fileName.matches(FILE_REGEX))
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
        Pattern datePatter = Pattern.compile(DATE_REGEX);
        Matcher matcher = datePatter.matcher(fileName);
        if (matcher.find()) {
            return matcher.group();
        }
        return "undefined";
    }
}
