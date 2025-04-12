package org.common.models;


import java.io.Serializable;
import java.util.Arrays;

/**
 * Класс перечисления типов билетов
 */
public enum TicketType implements Serializable {
    VIP,
    USUAL,
    BUDGETARY,
    CHEAP;
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
    public static java.util.List<String> getNamesArray(){
        return Arrays.asList(getNames(TicketType.class));
    }
}
