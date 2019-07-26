package com.chudzick.expanses.util;

import java.util.HashMap;
import java.util.Map;

// IT IS POSSIBLE TO GET MONTH NAME FROM LOCAL DATE BY  .getDisplayName(), BUT IT RETURNS
// LIPCA INSTEAD OF LIPIEC
public final class PolishMothNameResolver {
    private static final Map<Integer, String> mapOfMonths = new HashMap<>();

    static {
        mapOfMonths.put(1, "STYCZEŃ");
        mapOfMonths.put(2, "LUTY");
        mapOfMonths.put(3, "MARZEC");
        mapOfMonths.put(4, "KWIECIEŃ");
        mapOfMonths.put(5, "MAJ");
        mapOfMonths.put(6, "CZERWIEC");
        mapOfMonths.put(7, "LIPIEC");
        mapOfMonths.put(8, "SIERPIEŃ");
        mapOfMonths.put(9, "WRZESIEŃ");
        mapOfMonths.put(10, "PAŹDZIERNIK");
        mapOfMonths.put(11, "LISTOPAD");
        mapOfMonths.put(12, "GRUDZIEŃ");
    }

    public static String getMonthName(int monthOfYear) {
        return mapOfMonths.getOrDefault(monthOfYear, "");
    }
}
