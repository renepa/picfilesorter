package de.repa.supracam.ftp;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Optional;

import static de.repa.supracam.ftp.FileNameTestHelper.createAssertedValidFileName;

public class ValidFileNamesOfADayTest {

    @Test
    public void testCreateWithValidDayString() throws Exception {
        String day = "2017_02_15";
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day);
        Assertions.assertThat(validFileNamesOfDay.getDay()).isEqualTo(day);
    }

    @Test(expected = IllegalDayStringException.class)
    public void testCreateWithInvalidDayString() throws Exception {
        String day = "201702_15";
        ValidFileNamesOfADay.createValidFileNamesOfDay(day);
    }

    @Test
    public void testAddValidFileName() throws Exception {
        String day = "2017_02_15";
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day);
        Assertions.assertThat(validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_15_07_36_49.jpg"))).isTrue();
        Assertions.assertThat(validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_02_15_09_36_41.jpg"))).isTrue();

        Assertions.assertThat(validFileNamesOfDay.getFileNames().size()).isEqualTo(2);
        Assertions.assertThat(validFileNamesOfDay.getFileNames()).extracting("value")
                .contains(
                        "2017_02_15_07_36_49.jpg",
                        "2017_02_15_09_36_41.jpg");
    }

    @Test
    public void testAddInvalidFileNameForTheDay() throws Exception {
        String day = "2017_02_15";
        ValidFileNamesOfADay validFileNamesOfDay = ValidFileNamesOfADay.createValidFileNamesOfDay(day);
        Assertions.assertThat(validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2015_02_15_07_36_49.jpg"))).isFalse();
        Assertions.assertThat(validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_01_15_07_36_49.jpg"))).isFalse();

        Assertions.assertThat(validFileNamesOfDay.getFileNames()).isEmpty();
    }
}
