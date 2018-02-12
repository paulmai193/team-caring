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
public class RequestJoinTeamDTO implements Serializable {

    /** The team id. */
    @NotNull
    private Long teamId;

    /** The user id. */
    @JsonProperty("leaderId")
    @NotNull
    private Long userId;

    /**
     * Gets the team id.
     *
     * @return the teamId
     */
    public Long getTeamId() {
        return teamId;
    }

    /**
     * Sets the team id.
     *
     * @param teamId
     *        the teamId to set
     */
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    /**
     * Gets the user id.
     *
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *        the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RequestJoinTeamDTO{" + "teamId=" + getTeamId() + ", leaderId='" + getUserId() + "}";
    }

}
