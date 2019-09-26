package com.chudzick.expanses.util;

import java.util.ArrayList;
import java.util.List;

public class ListsUnion {

    public static <E> List<E> union(List<? extends E> list1, List<? extends E> list2) {
        List<E> result = new ArrayList<>(list1);
        result.addAll(list2);
        return result;
    }
}
