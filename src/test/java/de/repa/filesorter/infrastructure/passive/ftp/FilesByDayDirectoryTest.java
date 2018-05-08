package de.repa.filesorter.infrastructure.passive.ftp;

import de.repa.filesorter.domain.files.FilesByDayDirectory;
import de.repa.filesorter.domain.files.exceptions.IllegalDayStringException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static de.repa.filesorter.infrastructure.passive.ftp.FileNameTestHelper.createAssertedValidFileName;

public class FilesByDayDirectoryTest {

    @Test
    public void testCreateWithValidDayString() throws Exception {
        String day = "2017_02_15";
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay(day);
        Assertions.assertThat(validFileNamesOfDay.getDay()).isEqualTo(day);
    }

    @Test
    public void testCreateWithInvalidDayString() throws Exception {
        String day = "201702_15";
        try {
            FilesByDayDirectory.createValidFileNamesOfDay(day);
        } catch (IllegalDayStringException e) {
            Assertions.assertThat(e.getMessage()).contains(day);
        }
    }

    @Test
    public void testAddValidFileName() throws Exception {
        String day = "2017_02_15";
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay(day);
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
        FilesByDayDirectory validFileNamesOfDay = FilesByDayDirectory.createValidFileNamesOfDay(day);
        Assertions.assertThat(validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2015_02_15_07_36_49.jpg"))).isFalse();
        Assertions.assertThat(validFileNamesOfDay.addValidFileName(createAssertedValidFileName("2017_01_15_07_36_49.jpg"))).isFalse();

        Assertions.assertThat(validFileNamesOfDay.getFileNames()).isEmpty();
    }
}
