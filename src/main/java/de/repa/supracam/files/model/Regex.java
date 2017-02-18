package de.repa.supracam.files.model;

public final class Regex {
    public static final String DATE_REGEX =
            "^[1-2][0-9]{3}"
                    + "_(0?[1-9]|[1][0-2])"
                    + "_([1-2][0-9]|3[0-1]|0?[1-9])";

    public static final String TIME_REGEX =
            "(1[0-9]|2[0-3]|0?[0-9])"
                    + "_(0?[0-9]|[1-5][0-9])"
                    + "_(0?[0-9]|[1-5][0-9])";

    public static final String FILE_REGEX = DATE_REGEX + "_" + TIME_REGEX + "\\.jpg";

    private Regex() {
    }
}
