package de.repa.supracam.ftp;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileNameService {
    private static final String FILE_REGEX =
            "[2][0-9]{3}"
                    + "_(0?[1-9]|[1][0-2])"
                    + "_(0?[1-9]|[1-2][0-9]|3[0-1])"
                    + "_(0?[0-9]|1[0-9]|2[0-3])"
                    + "_(0?[0-9]|[1-5][0-9])"
                    + "_(0?[0-9]|[1-5][0-9])"
                    + "\\.jpg";


    public List<String> filerValidFileNames(List<String> fileNamesToValidate) {
        if (fileNamesToValidate != null && !fileNamesToValidate.isEmpty()) {
            return fileNamesToValidate.stream()
                    .map(filename -> filename.substring(filename.lastIndexOf('/') + 1))
                    .filter(fileName -> fileName.matches(FILE_REGEX))
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }
}
