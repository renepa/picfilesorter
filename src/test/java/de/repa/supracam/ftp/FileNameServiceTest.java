package de.repa.supracam.ftp;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.repa.supracam.ftp.FileNameTestHelper.createAssertedValidFileName;

public class FileNameServiceTest {

    private FileNameService fileNameService = new FileNameService();

    @Test
    public void testFilterAllValidFileNames() throws Exception {
        List<String> fileList = Arrays.asList("2017_02_12_18_29_12.jpg",
                "2017_02_12_07_36_49.jpg",
                "2017_02_10_23_05_00.jpg",
                "2017_02_11_15_38_30.jpg",
                "2017_02_11_15_46_18.jpg");
        List<ValidFileName> resultList = fileNameService.filerValidFileNames(fileList);
        Assertions.assertThat(resultList).extracting("value").contains("2017_02_12_18_29_12.jpg",
                "2017_02_12_07_36_49.jpg",
                "2017_02_10_23_05_00.jpg",
                "2017_02_11_15_38_30.jpg",
                "2017_02_11_15_46_18.jpg");

    }

    @Test
    public void testFilterInvalidFileNames() throws Exception {
        List<String> fileNamesToFilter = Arrays.asList(
                "cam/.",
                "2017_2_12_8_29_12.jpg", //legal
                "1017_02_12_07_36_49.jpg",
                "2017_02_10_23_05_70.jpg",
                "2017_02_11_15_66_30.jpg",
                "2017_02_11_1546_18.jpg",
                "2017_0211_15_46_18.jpg",
                "2017_0211_15_46_18.jg",
                "2017_0211_1546_18.jg");

        List<ValidFileName> resultList = fileNameService.filerValidFileNames(fileNamesToFilter);
        Assertions.assertThat(resultList).extracting("value").contains(
                "2017_2_12_8_29_12.jpg"
        );
    }

    @Test
    public void testFilterWithEmptyList() throws Exception {
        List<ValidFileName> resultList = fileNameService.filerValidFileNames(Collections.EMPTY_LIST);
        Assertions.assertThat(resultList).isNotNull();
        Assertions.assertThat(resultList).isEmpty();
    }

    @Test
    public void testFilterWithNullList() throws Exception {
        List<ValidFileName> resultList = fileNameService.filerValidFileNames(null);
        Assertions.assertThat(resultList).isNotNull();
        Assertions.assertThat(resultList).isEmpty();
    }

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
        Set<ValidFileNamesOfADay> resultSet = fileNameService.groupFileNamesByDay(fileList);
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

    private Set<String> getValidFileNamesByDay(Set<ValidFileNamesOfADay> fileNamesOfDays, String dayString) {
        for (ValidFileNamesOfADay fileNames : fileNamesOfDays) {
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
        Set<ValidFileNamesOfADay> resultMap = fileNameService.groupFileNamesByDay(fileList);
        Assertions.assertThat(resultMap.isEmpty());
    }
}
