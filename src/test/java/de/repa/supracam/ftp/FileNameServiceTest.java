package de.repa.supracam.ftp;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        List<String> fileNamesToFilter = Arrays.asList("cam/.",
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
}
