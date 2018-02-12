/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the GroupsMember entity.
 *
 * @author Dai Mai
 */
public class GroupsMemberDTO implements Serializable {

    /** The id. */
    private Long    id;

    /** The level. */
    private Integer level;

    /** The status. */
    private Integer status;

    /** The custom user id. */
    private Long    customUserId;

    /** The groups id. */
    private Long    groupsId;

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
     * Id.
     *
     * @param id
     *        the id
     * @return the groups member DTO
     */
    public GroupsMemberDTO id(Long id) {
        this.setId(id);
        return this;
    }

    /**
     * Gets the level.
     *
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Sets the level.
     *
     * @param level
     *        the new level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * Level.
     *
     * @param level
     *        the level
     * @return the groups member DTO
     */
    public GroupsMemberDTO level(Integer level) {
        this.setLevel(level);
        return this;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Status.
     *
     * @param status
     *        the status
     * @return the groups member DTO
     */
    public GroupsMemberDTO status(Integer status) {
        this.setStatus(status);
        return this;
    }

    /**
     * Sets the status.
     *
     * @param status
     *        the new status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Gets the custom user id.
     *
     * @return the custom user id
     */
    public Long getCustomUserId() {
        return customUserId;
    }

    /**
     * Sets the custom user id.
     *
     * @param customUserId
     *        the new custom user id
     */
    public void setCustomUserId(Long customUserId) {
        this.customUserId = customUserId;
    }

    /**
     * Custom user id.
     *
     * @param customUserId
     *        the custom user id
     * @return the groups member DTO
     */
    public GroupsMemberDTO customUserId(Long customUserId) {
        this.setCustomUserId(customUserId);
        return this;
    }

    /**
     * Gets the groups id.
     *
     * @return the groups id
     */
    public Long getGroupsId() {
        return groupsId;
    }

    /**
     * Sets the groups id.
     *
     * @param groupsId
     *        the new groups id
     */
    public void setGroupsId(Long groupsId) {
        this.groupsId = groupsId;
    }

    /**
     * Groups id.
     *
     * @param groupsId
     *        the groups id
     * @return the groups member DTO
     */
    public GroupsMemberDTO groupsId(Long groupsId) {
        this.setGroupsId(groupsId);
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

        GroupsMemberDTO groupsMemberDTO = (GroupsMemberDTO) o;
        if (groupsMemberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupsMemberDTO.getId());
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
        return "GroupsMemberDTO{" + "id=" + getId() + ", level='" + getLevel() + "'" + ", status='"
                + getStatus() + "'" + "}";
    }

    /**
     * Cast to simple groups member DTO.
     *
     * @return the simple groups member DTO
     */
    public SimpleGroupsMemberDTO castToSimpleGroupsMemberDTO() {
        return new SimpleGroupsMemberDTO().level(level).id(id);
    }
}
