package com.chudzick.expanses.domain.paging;

import java.util.Collections;
import java.util.List;

public class RequestPage<T> {
    private final List<T> content;
    private final long allItems;
    private final int currentPage;
    private final int totalPages;
    private final int itemsOnPage;

    public RequestPage(List<T> content, long allItems, int currentPage, int totalPages, int itemsOnPage) {
        this.content = content;
        this.allItems = allItems;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.itemsOnPage = itemsOnPage;
    }

    private RequestPage() {
        this.content = Collections.emptyList();
        this.allItems = 0;
        this.currentPage = 0;
        this.totalPages = 0;
        this.itemsOnPage = 0;
    }

    public static <T> RequestPage<T> emptyPage() {
        return new RequestPage<>();
    }

    public List<T> getContent() {
        return content;
    }

    public long getAllItems() {
        return allItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getItemsOnPage() {
        return itemsOnPage;
    }
}
