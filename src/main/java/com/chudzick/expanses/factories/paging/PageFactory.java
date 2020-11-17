package com.chudzick.expanses.factories.paging;

import com.chudzick.expanses.domain.paging.RequestPage;
import com.chudzick.expanses.util.paging.PagingUtils;

import java.util.List;

public class PageFactory<T> {

    public RequestPage<T> getRequestPage(List<T> listOfItems, int pageNum, int itemsOnPage) {
        if (listOfItems == null) {
            return RequestPage.emptyPage();
        }
        if (itemsOnPage < 1) {
            itemsOnPage = 1;
        }
        PagingUtils<T> pagingUtils = new PagingUtils<>();
        int allPages = pagingUtils.totalPageSize(listOfItems, itemsOnPage);
        if (allPages <= pageNum) {
            pageNum = allPages - 1;
        }
        if (pageNum < 0) {
            pageNum = 0;
        }
        List<T> pageContent = pagingUtils.getPageContent(listOfItems, pageNum, itemsOnPage);
        return new RequestPage<>(pageContent, listOfItems.size(), pageNum, allPages, itemsOnPage);
    }
}
