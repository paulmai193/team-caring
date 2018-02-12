/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Groups entity.
 *
 * @author Dai Mai
 */
public class GroupsDTO implements Serializable {

    /** The id. */
    private Long id;

    /** The leader id. */
    private Long leaderId;

    /** The team id. */
    private Long teamId;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *        the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the leader id.
     *
     * @return the leader id
     */
    public Long getLeaderId() {
        return leaderId;
    }

    /**
     * Sets the leader id.
     *
     * @param customUserId
     *        the new leader id
     */
    public void setLeaderId(Long customUserId) {
        this.leaderId = customUserId;
    }

    /**
     * Leader id.
     *
     * @param customUserId
     *        the custom user id
     * @return the groups DTO
     */
    public GroupsDTO leaderId(Long customUserId) {
        this.setLeaderId(customUserId);
        return this;
    }

    /**
     * Gets the team id.
     *
     * @return the team id
     */
    public Long getTeamId() {
        return teamId;
    }

    /**
     * Sets the team id.
     *
     * @param teamId
     *        the new team id
     */
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    /**
     * Team id.
     *
     * @param teamId
     *        the team id
     * @return the groups DTO
     */
    public GroupsDTO teamId(Long teamId) {
        this.setTeamId(teamId);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupsDTO groupsDTO = (GroupsDTO) o;
        if (groupsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupsDTO.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GroupsDTO{" + "id=" + getId() + "}";
    }
}
