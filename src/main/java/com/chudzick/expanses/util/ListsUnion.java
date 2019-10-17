package com.chudzick.expanses.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListsUnion {

    private ListsUnion() {}

    public static <E> List<E> union(Collection<? extends E> list1, List<? extends E> list2) {
        List<E> result = new ArrayList<>(list1);
        result.addAll(list2);
        return result;
    }
}
