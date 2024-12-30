package com.kinder.kinder_ielts.util;

import java.math.BigDecimal;
import java.util.Objects;

public class CompareUtil {

    /**
     * Returns the first non-null value using Objects.requireNonNullElse.
     *
     * @param <T>            The type of the values.
     * @param value          The primary value to check.
     * @param fallbackValue  The fallback value if the primary is null.
     * @return The non-null value.
     */
    public static <T> T compare(T value, T fallbackValue) {
        return Objects.requireNonNullElse(value, fallbackValue);
    }
}
