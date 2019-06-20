package com.chudzick.expanses.mappers;

public interface ObjectMapper<T,L> {

    public T mapObjects(L oldObject);
}
