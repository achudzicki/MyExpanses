package com.chudzick.expanses.factories.paging;

import com.chudzick.expanses.domain.paging.RequestPage;
import com.chudzick.expanses.util.paging.PagingUtils;

import java.util.List;

public class PageFactory<T> {
    private final PagingUtils<T> pagingUtils;

    public PageFactory(PagingUtils<T> pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    public RequestPage<T> getRequestPage(List<T> listOfItems, int pageNum, int itemsOnPage) {
        List<T> pageContent = pagingUtils.getPageContent(listOfItems, pageNum, itemsOnPage);
        int allPages = pagingUtils.totalPageSize(listOfItems, itemsOnPage);
        return new RequestPage<>(pageContent, listOfItems.size(), pageNum, allPages, itemsOnPage);
    }
}