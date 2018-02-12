/*
 * 
 */
package com.ttth.teamcaring.service.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class FcmResponseDTO.
 *
 * @author Dai Mai
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FcmResponseDTO {

    /** The multicast id. */
    private long    multicast_id;

    /** The success. */
    private Integer success;

    /** The failure. */
    private Integer failure;

    /** The canonical ids. */
    private Object  canonical_ids;

    /** The results. */
    private Object  results;

    /**
     * Gets the multicast id.
     *
     * @return the multicast id
     */
    public long getMulticast_id() {
        return multicast_id;
    }

    /**
     * Sets the multicast id.
     *
     * @param multicast_id
     *        the new multicast id
     */
    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    /**
     * Gets the success.
     *
     * @return the success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     * Sets the success.
     *
     * @param success
     *        the new success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
     * Gets the failure.
     *
     * @return the failure
     */
    public Integer getFailure() {
        return failure;
    }

    /**
     * Sets the failure.
     *
     * @param failure
     *        the new failure
     */
    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    /**
     * Gets the canonical ids.
     *
     * @return the canonical ids
     */
    public Object getCanonical_ids() {
        return canonical_ids;
    }

    /**
     * Sets the canonical ids.
     *
     * @param canonical_ids
     *        the new canonical ids
     */
    public void setCanonical_ids(Object canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    /**
     * Gets the results.
     *
     * @return the results
     */
    public Object getResults() {
        return results;
    }

    /**
     * Sets the results.
     *
     * @param results
     *        the new results
     */
    public void setResults(Object results) {
        this.results = results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FcmResponseDTO{" + "multicast_id=" + multicast_id + ", success=" + success
                + ", failure=" + failure + ", canonical_ids=" + canonical_ids + ", results="
                + results + '}';
    }
}
