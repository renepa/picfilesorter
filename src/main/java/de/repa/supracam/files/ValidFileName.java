package de.repa.supracam.files;

import de.repa.supracam.files.exceptions.IllegalFileNameException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidFileName {
    private final String value;

    private ValidFileName(String value) {
        this.value = value;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getValue() {
        return value;
    }

    public String getDayString() {
        Pattern datePatter = Pattern.compile(Regex.DATE_REGEX);
        Matcher matcher = datePatter.matcher(this.value);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new IllegalFileNameException(value);
    }

    public static class Builder {
        private Builder() {
        }

        public Optional<ValidFileName> build(String value) {
            if (value != null && value.matches(Regex.FILE_REGEX)) {
                return Optional.of(new ValidFileName(value));
            }
            return Optional.empty();
        }
    }
}
