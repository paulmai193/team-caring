/*
 * 
 */
package com.ttth.teamcaring.web.rest.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;

/**
 * Tests based on parsing algorithm in
 * app/components/util/pagination-util.service.js
 *
 * @see PaginationUtil
 */
public class PaginationUtilUnitTest {

    /**
     * Generate pagination http headers test.
     */
    @Test
    public void generatePaginationHttpHeadersTest() {
        String baseUrl = "/api/_search/example";
        List<String> content = new ArrayList<>();
        Page<String> page = new PageImpl<>(content, new PageRequest(6, 50), 400L);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, baseUrl);
        List<String> strHeaders = headers.get(HttpHeaders.LINK);
        assertNotNull(strHeaders);
        assertTrue(strHeaders.size() == 1);
        String headerData = strHeaders.get(0);
        assertTrue(headerData.split(",").length == 4);
        String expectedData = "</api/_search/example?page=7&size=50>; rel=\"next\","
                + "</api/_search/example?page=5&size=50>; rel=\"prev\","
                + "</api/_search/example?page=7&size=50>; rel=\"last\","
                + "</api/_search/example?page=0&size=50>; rel=\"first\"";
        assertEquals(expectedData, headerData);
        List<String> xTotalCountHeaders = headers.get("X-Total-Count");
        assertTrue(xTotalCountHeaders.size() == 1);
        assertTrue(Long.valueOf(xTotalCountHeaders.get(0)).equals(400L));
    }

    /**
     * Comma test.
     */
    @Test
    public void commaTest() {
        String baseUrl = "/api/_search/example";
        List<String> content = new ArrayList<>();
        Page<String> page = new PageImpl<>(content);
        String query = "Test1, test2";
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
                baseUrl);
        List<String> strHeaders = headers.get(HttpHeaders.LINK);
        assertNotNull(strHeaders);
        assertTrue(strHeaders.size() == 1);
        String headerData = strHeaders.get(0);
        assertTrue(headerData.split(",").length == 2);
        String expectedData = "</api/_search/example?page=0&size=0&query=Test1%2C+test2>; rel=\"last\","
                + "</api/_search/example?page=0&size=0&query=Test1%2C+test2>; rel=\"first\"";
        assertEquals(expectedData, headerData);
        List<String> xTotalCountHeaders = headers.get("X-Total-Count");
        assertTrue(xTotalCountHeaders.size() == 1);
        assertTrue(Long.valueOf(xTotalCountHeaders.get(0)).equals(0L));
    }

    /**
     * Multiple pages test.
     */
    @Test
    public void multiplePagesTest() {
        String baseUrl = "/api/_search/example";
        List<String> content = new ArrayList<>();

        // Page 0
        Page<String> page = new PageImpl<>(content, new PageRequest(0, 50), 400L);
        String query = "Test1, test2";
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
                baseUrl);
        List<String> strHeaders = headers.get(HttpHeaders.LINK);
        assertNotNull(strHeaders);
        assertTrue(strHeaders.size() == 1);
        String headerData = strHeaders.get(0);
        assertTrue(headerData.split(",").length == 3);
        String expectedData = "</api/_search/example?page=1&size=50&query=Test1%2C+test2>; rel=\"next\","
                + "</api/_search/example?page=7&size=50&query=Test1%2C+test2>; rel=\"last\","
                + "</api/_search/example?page=0&size=50&query=Test1%2C+test2>; rel=\"first\"";
        assertEquals(expectedData, headerData);
        List<String> xTotalCountHeaders = headers.get("X-Total-Count");
        assertTrue(xTotalCountHeaders.size() == 1);
        assertTrue(Long.valueOf(xTotalCountHeaders.get(0)).equals(400L));

        // Page 1
        page = new PageImpl<>(content, new PageRequest(1, 50), 400L);
        query = "Test1, test2";
        headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, baseUrl);
        strHeaders = headers.get(HttpHeaders.LINK);
        assertNotNull(strHeaders);
        assertTrue(strHeaders.size() == 1);
        headerData = strHeaders.get(0);
        assertTrue(headerData.split(",").length == 4);
        expectedData = "</api/_search/example?page=2&size=50&query=Test1%2C+test2>; rel=\"next\","
                + "</api/_search/example?page=0&size=50&query=Test1%2C+test2>; rel=\"prev\","
                + "</api/_search/example?page=7&size=50&query=Test1%2C+test2>; rel=\"last\","
                + "</api/_search/example?page=0&size=50&query=Test1%2C+test2>; rel=\"first\"";
        assertEquals(expectedData, headerData);
        xTotalCountHeaders = headers.get("X-Total-Count");
        assertTrue(xTotalCountHeaders.size() == 1);
        assertTrue(Long.valueOf(xTotalCountHeaders.get(0)).equals(400L));

        // Page 6
        page = new PageImpl<>(content, new PageRequest(6, 50), 400L);
        headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, baseUrl);
        strHeaders = headers.get(HttpHeaders.LINK);
        assertNotNull(strHeaders);
        assertTrue(strHeaders.size() == 1);
        headerData = strHeaders.get(0);
        assertTrue(headerData.split(",").length == 4);
        expectedData = "</api/_search/example?page=7&size=50&query=Test1%2C+test2>; rel=\"next\","
                + "</api/_search/example?page=5&size=50&query=Test1%2C+test2>; rel=\"prev\","
                + "</api/_search/example?page=7&size=50&query=Test1%2C+test2>; rel=\"last\","
                + "</api/_search/example?page=0&size=50&query=Test1%2C+test2>; rel=\"first\"";
        assertEquals(expectedData, headerData);
        xTotalCountHeaders = headers.get("X-Total-Count");
        assertTrue(xTotalCountHeaders.size() == 1);
        assertTrue(Long.valueOf(xTotalCountHeaders.get(0)).equals(400L));

        // Page 7
        page = new PageImpl<>(content, new PageRequest(7, 50), 400L);
        headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, baseUrl);
        strHeaders = headers.get(HttpHeaders.LINK);
        assertNotNull(strHeaders);
        assertTrue(strHeaders.size() == 1);
        headerData = strHeaders.get(0);
        assertTrue(headerData.split(",").length == 3);
        expectedData = "</api/_search/example?page=6&size=50&query=Test1%2C+test2>; rel=\"prev\","
                + "</api/_search/example?page=7&size=50&query=Test1%2C+test2>; rel=\"last\","
                + "</api/_search/example?page=0&size=50&query=Test1%2C+test2>; rel=\"first\"";
        assertEquals(expectedData, headerData);
    }

    /**
     * Greater semicolon test.
     */
    @Test
    public void greaterSemicolonTest() {
        String baseUrl = "/api/_search/example";
        List<String> content = new ArrayList<>();
        Page<String> page = new PageImpl<>(content);
        String query = "Test>;test";
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
                baseUrl);
        List<String> strHeaders = headers.get(HttpHeaders.LINK);
        assertNotNull(strHeaders);
        assertTrue(strHeaders.size() == 1);
        String headerData = strHeaders.get(0);
        assertTrue(headerData.split(",").length == 2);
        String[] linksData = headerData.split(",");
        assertTrue(linksData.length == 2);
        assertTrue(linksData[0].split(">;").length == 2);
        assertTrue(linksData[1].split(">;").length == 2);
        String expectedData = "</api/_search/example?page=0&size=0&query=Test%3E%3Btest>; rel=\"last\","
                + "</api/_search/example?page=0&size=0&query=Test%3E%3Btest>; rel=\"first\"";
        assertEquals(expectedData, headerData);
        List<String> xTotalCountHeaders = headers.get("X-Total-Count");
        assertTrue(xTotalCountHeaders.size() == 1);
        assertTrue(Long.valueOf(xTotalCountHeaders.get(0)).equals(0L));
    }

    /**
     * Paging list test first page.
     */
    @Test
    public void pagingListTestFirstPage() {
        int total = 49;
        List<String> list = this.prepareList(total);

        int size = 5;
        Page<String> page = PaginationUtil.pagingList(list, new PageRequest(0, size));
        assertNotNull(page);
        assertTrue(page.getTotalPages() == 10);
        assertTrue(page.getNumber() == 0);
        assertTrue(page.getContent().get(0).equalsIgnoreCase("Item 1")); // First
                                                                         // item
        assertTrue(page.getContent().get(page.getContent().size() - 1).equalsIgnoreCase("Item 5")); // Last
                                                                                                    // item
    }

    /**
     * Paging list test fourth page.
     */
    @Test
    public void pagingListTestFourthPage() {
        int total = 49;
        List<String> list = this.prepareList(total);

        int size = 5;
        Page<String> page = PaginationUtil.pagingList(list, new PageRequest(3, size));
        assertNotNull(page);
        assertTrue(page.getTotalPages() == 10);
        assertTrue(page.getNumber() == 3);
        assertTrue(page.getContent().get(0).equalsIgnoreCase("Item 16")); // First
                                                                          // item
        assertTrue(page.getContent().get(page.getContent().size() - 1).equalsIgnoreCase("Item 20")); // Last
                                                                                                     // item
    }

    /**
     * Paging list test last page.
     */
    @Test
    public void pagingListTestLastPage() {
        int total = 49;
        List<String> list = this.prepareList(total);

        int size = 5;
        Page<String> page = PaginationUtil.pagingList(list, new PageRequest(9, size));
        assertNotNull(page);
        assertTrue(page.getTotalPages() == 10);
        assertTrue(page.getNumber() == 9);
        assertTrue(page.getContent().get(0).equalsIgnoreCase("Item 46")); // First
                                                                          // item
        assertTrue(page.getContent().get(page.getContent().size() - 1).equalsIgnoreCase("Item 49")); // Last
                                                                                                     // item
    }

    /**
     * Paging list test out of first page.
     */
    @Test
    public void pagingListTestOutOfFirstPage() {
        int total = 3;
        List<String> list = this.prepareList(total);

        int size = 5;
        Page<String> page = PaginationUtil.pagingList(list, new PageRequest(1, size));
        assertNotNull(page);
        assertTrue(page.getTotalPages() == 1);
        assertTrue(page.getContent().size() == 0);
    }

    /**
     * Paging list test out of page.
     */
    @Test
    public void pagingListTestOutOfPage() {
        int total = 49;
        List<String> list = this.prepareList(total);

        int size = 5;
        Page<String> page = PaginationUtil.pagingList(list, new PageRequest(11, size));
        assertNotNull(page);
        assertTrue(page.getTotalPages() == 10);
        assertTrue(page.getContent().size() == 0);
    }

    /**
     * Prepare list.
     *
     * @param size
     *        the size
     * @return the list
     */
    private List<String> prepareList(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            list.add("Item " + i);
        }
        return list;
    }
}
