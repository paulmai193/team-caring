/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * The Class PageDTO.
 *
 * @author Dai Mai
 * @param <T> the generic type
 */
public class PageDTO<T> implements Serializable {

    /** The total pages. */
    private int     totalPages;

    /** The result. */
    private List<T> result;

    /**
     * Instantiates a new page DTO.
     *
     * @param totalPages
     *        the total pages
     * @param result
     *        the result
     */
    public PageDTO(int totalPages, List<T> result) {
        super();
        this.totalPages = totalPages;
        this.result = result;
    }

    /**
     * Gets the total pages.
     *
     * @return the totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public List<T> getResult() {
        return result;
    }

}
