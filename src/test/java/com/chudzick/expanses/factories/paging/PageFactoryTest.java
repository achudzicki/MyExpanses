package com.chudzick.expanses.factories.paging;

import com.chudzick.expanses.domain.paging.RequestPage;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageFactoryTest {
    private static final PageFactory<Integer> PAGE_FACTORY = new PageFactory<>();

    @Test
    public void getRequestPage_passListWith100ItemsPageNum2ItemsOnPage10_ReturnRequestPage() {
        List<Integer> list = prepareList(100);

        RequestPage<Integer> result = PAGE_FACTORY.getRequestPage(list, 2, 10);

        Assert.assertEquals(list.size(), result.getAllItems());
        Assert.assertEquals(10, result.getContent().size());
        Assert.assertEquals(2, result.getCurrentPage());
        Assert.assertEquals(10, result.getItemsOnPage());
        Assert.assertEquals(10, result.getTotalPages());
    }

    @Test
    public void getRequestPage_passListWith100ItemsPageNumber0ItemsOnPage10_ReturnRequestPage() {
        List<Integer> list = prepareList(100);

        RequestPage<Integer> result = PAGE_FACTORY.getRequestPage(list, 0, 10);

        Assert.assertEquals(list.size(), result.getAllItems());
        Assert.assertEquals(10, result.getContent().size());
        Assert.assertEquals(0, result.getCurrentPage());
        Assert.assertEquals(10, result.getItemsOnPage());
        Assert.assertEquals(10, result.getTotalPages());
    }

    @Test
    public void getRequestPage_passListWith100ItemsPageNumber999ItemsOnPage10_ReturnRequestPage() {
        List<Integer> list = prepareList(100);

        RequestPage<Integer> result = PAGE_FACTORY.getRequestPage(list, 999, 10);

        Assert.assertEquals(list.size(), result.getAllItems());
        Assert.assertEquals(10, result.getContent().size());
        Assert.assertEquals(9, result.getCurrentPage());
        Assert.assertEquals(10, result.getItemsOnPage());
        Assert.assertEquals(10, result.getTotalPages());
    }

    @Test
    public void getRequestPage_passListWith100ItemsPageNumber1ItemsOnPage0_ReturnRequestPage() {
        List<Integer> list = prepareList(100);

        RequestPage<Integer> result = PAGE_FACTORY.getRequestPage(list, 1, 0);

        Assert.assertEquals(list.size(), result.getAllItems());
        Assert.assertEquals(1, result.getContent().size());
        Assert.assertEquals(1, result.getCurrentPage());
        Assert.assertEquals(1, result.getItemsOnPage());
        Assert.assertEquals(100, result.getTotalPages());
    }

    @Test
    public void getRequestPage_passListWith100ItemsPageNumberLessThan0ItemsOnPage0_ReturnRequestPage() {
        List<Integer> list = prepareList(100);

        RequestPage<Integer> result = PAGE_FACTORY.getRequestPage(list, -1, 0);

        Assert.assertEquals(list.size(), result.getAllItems());
        Assert.assertEquals(1, result.getContent().size());
        Assert.assertEquals(0, result.getCurrentPage());
        Assert.assertEquals(1, result.getItemsOnPage());
        Assert.assertEquals(100, result.getTotalPages());
    }


    @Test
    public void getRequestPage_passNullList_ReturnEmptyRequestPage() {
        RequestPage<Integer> result = PAGE_FACTORY.getRequestPage(null, 2, 10);

        Assert.assertEquals(0, result.getAllItems());
        Assert.assertEquals(0, result.getContent().size());
        Assert.assertEquals(0, result.getCurrentPage());
        Assert.assertEquals(0, result.getItemsOnPage());
        Assert.assertEquals(0, result.getTotalPages());
    }


    private List<Integer> prepareList(int listElements) {
        return IntStream.range(0, listElements).boxed().collect(Collectors.toList());
    }
}
