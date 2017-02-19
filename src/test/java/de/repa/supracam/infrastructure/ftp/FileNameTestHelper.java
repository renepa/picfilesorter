package de.repa.supracam.infrastructure.ftp;

import de.repa.supracam.files.model.ValidFileName;
import org.assertj.core.api.Assertions;

import java.util.Optional;

public class FileNameTestHelper {
    public static ValidFileName createAssertedValidFileName(String value) {
        Optional<ValidFileName> build = ValidFileName.builder().build(value);
        Assertions.assertThat(build.isPresent()).isTrue();
        return build.get();
    }
}