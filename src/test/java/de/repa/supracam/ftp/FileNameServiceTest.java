package de.repa.supracam.ftp;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Test;

import java.util.*;

public class FileNameServiceTest {

    private FileNameService fileNameService = new FileNameService();

    @Test
    public void testFilterAllValidFileNames() throws Exception {
        List<String> fileList = Arrays.asList("2017_02_12_18_29_12.jpg",
                "2017_02_12_07_36_49.jpg",
                "2017_02_10_23_05_00.jpg",
                "2017_02_11_15_38_30.jpg",
                "2017_02_11_15_46_18.jpg");
        List<String> resultList = fileNameService.filerValidFileNames(fileList);
        Assertions.assertThat(resultList).contains("2017_02_12_18_29_12.jpg",
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

        List<String> resultList = fileNameService.filerValidFileNames(fileNamesToFilter);
        Assertions.assertThat(resultList).contains(
                "2017_2_12_8_29_12.jpg"
        );
    }

    @Test
    public void testFilterWithEmptyList() throws Exception {
        List<String> resultList = fileNameService.filerValidFileNames(Collections.EMPTY_LIST);
        Assertions.assertThat(resultList).isNotNull();
        Assertions.assertThat(resultList).isEmpty();
    }

    @Test
    public void testFilterWithNullList() throws Exception {
        List<String> resultList = fileNameService.filerValidFileNames(null);
        Assertions.assertThat(resultList).isNotNull();
        Assertions.assertThat(resultList).isEmpty();
    }

    @Test
    public void testGroupFileNamesByDay() throws Exception {
        List<String> fileList = Arrays.asList(
                "2017_02_12_18_29_12.jpg",
                "2017_02_12_07_36_49.jpg",
                "2017_02_10_23_06_00.jpg",
                "2017_02_11_15_38_30.jpg",
                "2017_02_11_15_46_18.jpg",
                "2017_02_11_15_47_18.jpg",
                "2017_02_09_18_29_12.jpg",
                "2016_03_09_18_29_12.jpg",
                "2017_02_10_23_05_00.jpg");
        Set<ValidFileNamesOfADay> resultSet = fileNameService.groupFileNamesByDay(fileList);
        Assertions.assertThat(resultSet)
                .extracting("day", "fileNames")
                .contains(
                        Tuple.tuple("2017_02_12", new HashSet<>(Arrays.asList("2017_02_12_18_29_12.jpg", "2017_02_12_07_36_49.jpg"))),
                        Tuple.tuple("2017_02_10", new HashSet<>(Arrays.asList("2017_02_10_23_06_00.jpg", "2017_02_10_23_05_00.jpg"))),
                        Tuple.tuple("2017_02_11", new HashSet<>(Arrays.asList("2017_02_11_15_38_30.jpg", "2017_02_11_15_46_18.jpg", "2017_02_11_15_47_18.jpg"))),
                        Tuple.tuple("2017_02_09", new HashSet<>(Arrays.asList("2017_02_09_18_29_12.jpg"))),
                        Tuple.tuple("2016_03_09", new HashSet<>(Arrays.asList("2016_03_09_18_29_12.jpg"))));
    }

    @Test
    public void testGroupFileNamesByDayWithEmptyList() throws Exception {
        List<String> fileList = Collections.EMPTY_LIST;
        Set<ValidFileNamesOfADay> resultMap = fileNameService.groupFileNamesByDay(fileList);
        Assertions.assertThat(resultMap.isEmpty());
    }

    @Test(expected = IllegalDayStringException.class)
    public void testGroupFileNamesByDayWithIllegalFileName() throws Exception {
        List<String> fileList = Arrays.asList(
                "2017_02_12_18_29_12.jpg",
                "2017_02_12_07_36_49.jpg",
                "2017_02_10_23_06_00.jpg",
                "201702_11_15_38_30.jpg",
                "2017_023_10_23_06_00.jpg",
                "2017_023_10_2306_00.jpg");
        fileNameService.groupFileNamesByDay(fileList);
    }
}
