package de.repa.supracam.infrastructure.ftp;

import de.repa.supracam.files.model.ValidFileName;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Optional;

public class ValidFileNameTest {

    @Test
    public void testValidFileName() throws Exception {
        String fileName = "2017_02_19_18_02_59.jpg";
        Optional<ValidFileName> build = ValidFileName.builder().build(fileName);
        Assertions.assertThat(build.isPresent()).isTrue();
        Assertions.assertThat(build.get().getValue()).isEqualTo(fileName);

        fileName = "2017_02_19_18_02_8.jpg";
        build = ValidFileName.builder().build(fileName);
        Assertions.assertThat(build.isPresent()).isTrue();
        Assertions.assertThat(build.get().getValue()).isEqualTo(fileName);

        fileName = "2017_02_19_18_2_09.jpg";
        build = ValidFileName.builder().build(fileName);
        Assertions.assertThat(build.isPresent()).isTrue();
        Assertions.assertThat(build.get().getValue()).isEqualTo(fileName);

        fileName = "2017_02_19_8_02_00.jpg";
        build = ValidFileName.builder().build(fileName);
        Assertions.assertThat(build.isPresent()).isTrue();
        Assertions.assertThat(build.get().getValue()).isEqualTo(fileName);

        fileName = "2017_2_19_08_02_22.jpg";
        build = ValidFileName.builder().build(fileName);
        Assertions.assertThat(build.isPresent()).isTrue();
        Assertions.assertThat(build.get().getValue()).isEqualTo(fileName);
    }

    @Test
    public void testInvalidFileNameWithWrongFileType() throws Exception {
        Assertions.assertThat(ValidFileName.builder().build("2017_02_19_18_02_00.png")
                .isPresent()).isFalse();
        Assertions.assertThat(ValidFileName.builder().build("2017_02_19_18_02_00.jp")
                .isPresent()).isFalse();
        Assertions.assertThat(ValidFileName.builder().build("2017_02_19_18_02_00.")
                .isPresent()).isFalse();
        Assertions.assertThat(ValidFileName.builder().build("2017_02_19_18_02_00")
                .isPresent()).isFalse();
    }

    @Test
    public void testInvalidFileNameWithWrongTimeFormat() throws Exception {
        Assertions.assertThat(ValidFileName.builder().build("2017_02_19_18_02_61.jpg")
                .isPresent()).isFalse();

        Assertions.assertThat(ValidFileName.builder().build("2017_02_19_18_02_.jpg")
                .isPresent()).isFalse();


        Assertions.assertThat(ValidFileName.builder().build("2017_02_19_18_61_59.jpg")
                .isPresent()).isFalse();


        Assertions.assertThat(ValidFileName.builder().build("2017_02_19_24_02_55.jpg")
                .isPresent()).isFalse();
    }

    @Test
    public void testInvalidDateFormat() throws Exception {
        Assertions.assertThat(ValidFileName.builder().build("2017_05_32_18_02_59.jpg")
                .isPresent()).isFalse();

        Assertions.assertThat(ValidFileName.builder().build("2017_13_19_18_02_59.jpg")
                .isPresent()).isFalse();
    }

    @Test
    public void testBuildWithNullDate() throws Exception {
        Assertions.assertThat(ValidFileName.builder().build(null)
                .isPresent()).isFalse();
    }

    @Test
    public void testGetDayString() throws Exception {
        Optional<ValidFileName> build = ValidFileName.builder().build("2017_05_11_18_02_59.jpg");
        Assertions.assertThat(build.isPresent()).isTrue();
        Assertions.assertThat(build.get().getDayString()).isEqualTo("2017_05_11");

        build = ValidFileName.builder().build("2017_5_11_18_02_59.jpg");
        Assertions.assertThat(build.isPresent()).isTrue();
        Assertions.assertThat(build.get().getDayString()).isEqualTo("2017_5_11");

        build = ValidFileName.builder().build("2017_05_1_18_02_59.jpg");
        Assertions.assertThat(build.isPresent()).isTrue();
        Assertions.assertThat(build.get().getDayString()).isEqualTo("2017_05_1");

        build = ValidFileName.builder().build("2017_5_1_18_02_59.jpg");
        Assertions.assertThat(build.isPresent()).isTrue();
        Assertions.assertThat(build.get().getDayString()).isEqualTo("2017_5_1");
    }
}
