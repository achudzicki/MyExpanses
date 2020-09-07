package com.chudzick.expanses.mappers;

public interface ObjectMapper<T, L> {

    T mapObjects(L oldObject);

    L reverseMapping(T oldObject);
}
