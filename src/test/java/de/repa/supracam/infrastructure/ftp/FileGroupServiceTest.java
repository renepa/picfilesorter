package de.repa.supracam.infrastructure.ftp;

import de.repa.supracam.files.FileGroupService;
import de.repa.supracam.files.model.ValidFileName;
import de.repa.supracam.files.model.FilesByDayDirectory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.repa.supracam.infrastructure.ftp.FileNameTestHelper.createAssertedValidFileName;

public class FileGroupServiceTest {

    private FileGroupService fileGroupService = new FileGroupService();

    @Test
    public void testGroupFileNamesByDay() throws Exception {
        List<ValidFileName> fileList = Arrays.asList(
                createAssertedValidFileName("2017_02_12_18_29_12.jpg"),
                createAssertedValidFileName("2017_02_12_07_36_49.jpg"),
                createAssertedValidFileName("2017_02_10_23_06_00.jpg"),
                createAssertedValidFileName("2017_02_11_15_38_30.jpg"),
                createAssertedValidFileName("2017_02_11_15_46_18.jpg"),
                createAssertedValidFileName("2017_02_11_15_47_18.jpg"),
                createAssertedValidFileName("2017_02_09_18_29_12.jpg"),
                createAssertedValidFileName("2016_03_09_18_29_12.jpg"),
                createAssertedValidFileName("2017_02_10_23_05_00.jpg"));
        Set<FilesByDayDirectory> resultSet = fileGroupService.groupFileNamesByDay(fileList);
        Assertions.assertThat(resultSet)
                .extracting("day")
                .contains("2017_02_12", "2017_02_10", "2017_02_11", "2017_02_09", "2016_03_09");

        Assertions.assertThat(getValidFileNamesByDay(resultSet, "2017_02_12"))
                .contains("2017_02_12_18_29_12.jpg", "2017_02_12_07_36_49.jpg");

        Assertions.assertThat(getValidFileNamesByDay(resultSet, "2017_02_10"))
                .contains("2017_02_10_23_06_00.jpg", "2017_02_10_23_05_00.jpg");

        Assertions.assertThat(getValidFileNamesByDay(resultSet, "2017_02_11"))
                .contains("2017_02_11_15_38_30.jpg", "2017_02_11_15_46_18.jpg", "2017_02_11_15_47_18.jpg");


        Assertions.assertThat(getValidFileNamesByDay(resultSet, "2017_02_09"))
                .contains("2017_02_09_18_29_12.jpg");

        Assertions.assertThat(getValidFileNamesByDay(resultSet, "2016_03_09"))
                .contains("2016_03_09_18_29_12.jpg");
    }

    private Set<String> getValidFileNamesByDay(Set<FilesByDayDirectory> fileNamesOfDays, String dayString) {
        for (FilesByDayDirectory fileNames : fileNamesOfDays) {
            if (fileNames.getDay().equals(dayString)) {
                return fileNames.getFileNames().stream()
                        .map(validFileName -> validFileName.getValue())
                        .collect(Collectors.toSet());

            }
        }
        throw new IllegalArgumentException("No fileContainer found for day " + dayString);
    }

    @Test
    public void testGroupFileNamesByDayWithEmptyList() throws Exception {
        List<ValidFileName> fileList = Collections.EMPTY_LIST;
        Set<FilesByDayDirectory> resultMap = fileGroupService.groupFileNamesByDay(fileList);
        Assertions.assertThat(resultMap.isEmpty());
    }
}
