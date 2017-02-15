package ru.chicker.utils;

import java.util.Collections;
import java.util.Map;

public final class ExceptionHandlersUtils {
    private ExceptionHandlersUtils() {
    }

    public static Map error(Object message) {
        return Collections.singletonMap("error", message);
    }
}
