package com.chudzick.expanses.domain.paging;

public enum PageSettings {
    PAGE_CONTENT_SIZE(15),
    SMALL_PAGE_CONTENT_SIZE(5),
    FIRST_PAGE(0);

    private int value;

    PageSettings(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
