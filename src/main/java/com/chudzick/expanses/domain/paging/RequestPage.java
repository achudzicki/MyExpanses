package com.chudzick.expanses.domain.paging;

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
