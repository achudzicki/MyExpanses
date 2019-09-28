package com.chudzick.expanses.util.paging;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PagingUtils<T> {

    public List<T> getPageContent(List<T> listOfItems, int pageNum, int itemsOnPage) {
        List<T> pageContent = new ArrayList<>();
        ListIterator<T> iterator = listOfItems.listIterator();
        int startIndex = pageNum == 0 ? 0 : pageNum * itemsOnPage;
        int endIndex = (startIndex + itemsOnPage) > listOfItems.size() ? listOfItems.size() : startIndex + itemsOnPage;

        while (iterator.hasNext()) {
            int index = iterator.nextIndex();
            T next = iterator.next();
            if (index >= startIndex && index < endIndex) {
                pageContent.add(next);
            }
        }
        return pageContent;
    }

    public int totalPageSize(List listOfItems, int pageNum) {
        return listOfItems.size() % pageNum != 0 ? (listOfItems.size() / pageNum) + 1 : listOfItems.size() / pageNum;
    }
}
