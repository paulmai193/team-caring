/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class RequestJoinTeamDTO.
 *
 * @author Dai Mai
 */
public class ResponseJoinTeamDTO implements Serializable {

    /** The groups member id. */
    @NotNull
    @JsonProperty("requestId")
    private Long                   groupsMemberId;

    /** The response. */
    @NotNull
    private AnwserResponseStatus response;

    /**
     * Gets the groups member id.
     *
     * @return the groupsMemberId
     */
    public Long getGroupsMemberId() {
        return groupsMemberId;
    }

    /**
     * Sets the groups member id.
     *
     * @param groupsMemberId
     *        the groupsMemberId to set
     */
    public void setGroupsMemberId(Long groupsMemberId) {
        this.groupsMemberId = groupsMemberId;
    }

    /**
     * Groups member id.
     *
     * @param groupsMemberId
     *        the groups member id
     * @return the response join team DTO
     */
    public ResponseJoinTeamDTO groupsMemberId(Long groupsMemberId) {
        this.setGroupsMemberId(groupsMemberId);
        return this;
    }

    /**
     * Gets the response.
     *
     * @return the response
     */
    public AnwserResponseStatus getResponse() {
        return response;
    }

    /**
     * Sets the response.
     *
     * @param response
     *        the response to set
     */
    public void setResponse(AnwserResponseStatus response) {
        this.response = response;
    }

    /**
     * Response.
     *
     * @param response
     *        the response
     * @return the response join team DTO
     */
    public ResponseJoinTeamDTO response(AnwserResponseStatus response) {
        this.setResponse(response);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResponseJoinTeamDTO{" + "requestId=" + getGroupsMemberId() + "}";
    }

}
