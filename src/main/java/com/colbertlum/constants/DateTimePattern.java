package com.colbertlum.constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimePattern {
    public static final String DATE_PATTERN_FILE_SAFETY = "yyyy-MM-dd";
    public static final String DATE_PATTERN = "dd/MMM/yyyy";

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.ENGLISH);

    public static LocalDate getLocalDate(String value) {
        return LocalDate.parse(value, FORMATTER);
    }

    public static String parseString(LocalDate localDate){
        return localDate.format(FORMATTER);
    }

    public static String parseStringPathSafety(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN_FILE_SAFETY));
    }
}
