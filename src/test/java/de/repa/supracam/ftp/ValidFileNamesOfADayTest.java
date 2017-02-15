package de.repa.supracam.ftp;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class ValidFileNamesOfADayTest {

    @Test
    public void testCreateWithValidDayString() throws Exception {
        String day = "2017_02_15";
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day);
        Assertions.assertThat(validFileNamesOfDay.getDay()).isEqualTo(day);
    }

    @Test
    public void testCreateWithValidDayStringAndFileNameSet() throws Exception {
        String day = "2017_02_15";
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day, new HashSet<String>());
        Assertions.assertThat(validFileNamesOfDay.getDay()).isEqualTo(day);
    }

    @Test(expected = IllegalDayStringException.class)
    public void testCreateWithInvalidDayString() throws Exception {
        String day = "201702_15";
        ValidFileNamesOfADay.createValidFileNamesOfDay(day);
    }

    @Test(expected = IllegalDayStringException.class)
    public void testCreateWithInvalidDayStringAndFileList() throws Exception {
        String day = "201702_15";
        ValidFileNamesOfADay.createValidFileNamesOfDay(day, new HashSet<String>());
    }

    @Test
    public void testAddValidFileName() throws Exception {
        String day = "2017_02_15";
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day);
        Assertions.assertThat(validFileNamesOfDay.addValidFileName("2017_02_15_07_36_49.jpg")).isTrue();
        Assertions.assertThat(validFileNamesOfDay.addValidFileName("2017_02_15_09_36_41.jpg")).isTrue();

        Assertions.assertThat(validFileNamesOfDay.getFileNames().size()).isEqualTo(2);
        Assertions.assertThat(validFileNamesOfDay.getFileNames())
                .contains(
                        "2017_02_15_07_36_49.jpg",
                        "2017_02_15_09_36_41.jpg");
    }

    @Test
    public void testAddValidFileNamesAsSet() throws Exception {
        String day = "2017_02_15";
        HashSet<String> strings = new HashSet<>(Arrays.asList("2017_02_15_07_36_49.jpg", "2017_02_15_09_36_41.jpg"));
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day, strings);

        Assertions.assertThat(validFileNamesOfDay.getFileNames().size()).isEqualTo(2);
        Assertions.assertThat(validFileNamesOfDay.getFileNames())
                .contains(
                        "2017_02_15_07_36_49.jpg",
                        "2017_02_15_09_36_41.jpg");
    }

    @Test
    public void testAddInvalidFileNameForTheDay() throws Exception {
        String day = "2017_02_15";
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day);
        Assertions.assertThat(validFileNamesOfDay.addValidFileName("2015_02_15_07_36_49.jpg")).isFalse();
        Assertions.assertThat(validFileNamesOfDay.addValidFileName("2017_01_15_07_36_49.jpg")).isFalse();

        Assertions.assertThat(validFileNamesOfDay.getFileNames()).isEmpty();
    }

    @Test
    public void testAddInvalidFileNameForTheDayAsSet() throws Exception {
        String day = "2017_02_15";
        HashSet<String> strings = new HashSet<>(Arrays.asList("2015_02_15_07_36_49.jpg", "2017_01_15_07_36_49.jpg"));
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day, strings);
        Assertions.assertThat(validFileNamesOfDay.getFileNames()).isEmpty();
    }

    @Test
    public void testAddInvalidFileNameByRegex() throws Exception {
        String day = "2017_02_15";
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day);
        Assertions.assertThat(validFileNamesOfDay.addValidFileName("2017_02_15_0736_49.jpg")).isFalse();
    }

    @Test
    public void testAddInvalidFileNameByRegexAsSet() throws Exception {
        String day = "2017_02_15";
        HashSet<String> strings = new HashSet<>(Arrays.asList("2017_02_15_0736_49.jpg"));
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day, strings);
    }
}
