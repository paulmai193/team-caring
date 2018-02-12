/*
 * 
 */
package com.ttth.teamcaring.web.rest.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.repository.support.PageableExecutionUtils.TotalSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import com.ttth.teamcaring.service.dto.PageDTO;

/**
 * Utility class for handling pagination.
 * 
 * <p>
 * Pagination uses the same principles as the
 * <a href="https://developer.github.com/v3/#pagination">GitHub API</a>, and
 * follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link
 * header)</a>.
 *
 * @author Dai Mai
 */
public final class PaginationUtil {

    /**
     * Instantiates a new pagination util.
     */
    private PaginationUtil() {
    }

    /**
     * Generate pagination http headers.
     *
     * @param page
     *        the page
     * @param baseUrl
     *        the base url
     * @return the http headers
     */
    public static HttpHeaders generatePaginationHttpHeaders(Page page, String baseUrl) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        String link = "";
        if ((page.getNumber() + 1) < page.getTotalPages()) {
            link = "<" + generateUri(baseUrl, page.getNumber() + 1, page.getSize())
                    + ">; rel=\"next\",";
        }
        // prev link
        if ((page.getNumber()) > 0) {
            link += "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize())
                    + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }
        link += "<" + generateUri(baseUrl, lastPage, page.getSize()) + ">; rel=\"last\",";
        link += "<" + generateUri(baseUrl, 0, page.getSize()) + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }

    /**
     * Generate uri.
     *
     * @param baseUrl
     *        the base url
     * @param page
     *        the page
     * @param size
     *        the size
     * @return the string
     */
    private static String generateUri(String baseUrl, int page, int size) {
        return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", page)
                .queryParam("size", size).toUriString();
    }

    /**
     * Generate search pagination http headers.
     *
     * @param query
     *        the query
     * @param page
     *        the page
     * @param baseUrl
     *        the base url
     * @return the http headers
     */
    public static HttpHeaders generateSearchPaginationHttpHeaders(String query, Page page,
            String baseUrl) {
        String escapedQuery;
        try {
            escapedQuery = URLEncoder.encode(query, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        String link = "";
        if ((page.getNumber() + 1) < page.getTotalPages()) {
            link = "<" + generateUri(baseUrl, page.getNumber() + 1, page.getSize()) + "&query="
                    + escapedQuery + ">; rel=\"next\",";
        }
        // prev link
        if ((page.getNumber()) > 0) {
            link += "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize()) + "&query="
                    + escapedQuery + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }
        link += "<" + generateUri(baseUrl, lastPage, page.getSize()) + "&query=" + escapedQuery
                + ">; rel=\"last\",";
        link += "<" + generateUri(baseUrl, 0, page.getSize()) + "&query=" + escapedQuery
                + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }

    /**
     * Generate search pagination dto.
     *
     * @param <T> the generic type
     * @param page        the page
     * @return the page DTO
     */
    public static <T> PageDTO<T> generateSearchPaginationDto(Page<T> page) {
        int totalPages = page.getTotalPages();
        List<T> result = page.getContent();
        return new PageDTO<>(totalPages, result);
    }

    /**
     * Paging list.
     *
     * @param <T> the generic type
     * @param list        the list
     * @param pageable        the pageable
     * @return the page
     */
    public static <T> Page<T> pagingList(Collection<T> list, Pageable pageable) {
        if (Objects.isNull(list)) {
            throw new IllegalArgumentException("List must not NULL");
        }
        List<T> subList = subList(list, pageable.getPageNumber(), pageable.getPageSize());

        return PageableExecutionUtils.getPage(subList, pageable, new TotalSupplier() {

            /*
             * (non-Javadoc)
             * 
             * @see org.springframework.data.repository.support.
             * PageableExecutionUtils.TotalSupplier#get()
             */
            @Override
            public long get() {
                return list.size();
            }
        });
    }

    /**
     * Sub list.
     *
     * @param <T> the generic type
     * @param list        the list
     * @param pageNumber        the page number, start at 0
     * @param pageSize        the page size
     * @return the list
     * @throws IndexOutOfBoundsException         the index out of bounds exception
     */
    public static <T> List<T> subList(Collection<T> list, int pageNumber, int pageSize)
            throws IndexOutOfBoundsException {
        List<T> temp = new ArrayList<T>(list);
        if (list.size() > 0) {
            int start = pageNumber * pageSize;
            int records = 0;
            int n = list.size() / pageSize;
            if (pageNumber < n) {
                // When not last page
                records = pageSize;
                int end = start + records;
                temp = temp.subList(start, end);
            }
            else if (pageNumber - n <= 1 && start < list.size()) {
                // Last page
                records = list.size() % pageSize;
                int end = start + records;
                temp = temp.subList(start, end);
            }
            else {
                // Out of page
                temp.clear();
            }
        }
        return temp;
    }

}
