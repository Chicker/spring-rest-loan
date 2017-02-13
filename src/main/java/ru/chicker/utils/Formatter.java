package ru.chicker.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class Formatter {
    
    private Formatter() {}
    
    public static String getLocalizedBigDecimalValue(BigDecimal input, Locale locale) {
        NumberFormat fmt = NumberFormat.getNumberInstance(locale);
        
        fmt.setMaximumFractionDigits(2);
        fmt.setMinimumFractionDigits(2);
        fmt.setGroupingUsed(true);
        
        return fmt.format(input);
    }
}
