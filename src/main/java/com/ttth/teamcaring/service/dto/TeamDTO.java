/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A DTO for the Team entity.
 *
 * @author Dai Mai
 */
public class TeamDTO implements Serializable {

    /** The id. */
    private Long    id;

    /** The name. */
    private String  name;

    /** The description. */
    private String  description;

    /** The level. */
    @JsonProperty("teamLevel")
    private Integer level;

    /** The total member. */
    private Integer totalMember;

    /** The extra group name. */
    private String  extraGroupName;

    /** The extra group description. */
    private String  extraGroupDescription;

    /** The extra group total member. */
    private Integer extraGroupTotalMember;

    /** The owner id. */
    @JsonIgnore
    private Long    ownerId;

    /** The icon id. */
    private Long    iconId;

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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *        the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *        the new description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Gets the total member.
     *
     * @return the total member
     */
    public Integer getTotalMember() {
        return totalMember;
    }

    /**
     * Sets the total member.
     *
     * @param totalMember
     *        the new total member
     */
    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    /**
     * Gets the extra group name.
     *
     * @return the extra group name
     */
    public String getExtraGroupName() {
        return extraGroupName;
    }

    /**
     * Sets the extra group name.
     *
     * @param extraGroupName
     *        the new extra group name
     */
    public void setExtraGroupName(String extraGroupName) {
        this.extraGroupName = extraGroupName;
    }

    /**
     * Gets the extra group description.
     *
     * @return the extra group description
     */
    public String getExtraGroupDescription() {
        return extraGroupDescription;
    }

    /**
     * Sets the extra group description.
     *
     * @param extraGroupDescription
     *        the new extra group description
     */
    public void setExtraGroupDescription(String extraGroupDescription) {
        this.extraGroupDescription = extraGroupDescription;
    }

    /**
     * Gets the extra group total member.
     *
     * @return the extra group total member
     */
    public Integer getExtraGroupTotalMember() {
        return extraGroupTotalMember;
    }

    /**
     * Sets the extra group total member.
     *
     * @param extraGroupTotalMember
     *        the new extra group total member
     */
    public void setExtraGroupTotalMember(Integer extraGroupTotalMember) {
        this.extraGroupTotalMember = extraGroupTotalMember;
    }

    /**
     * Gets the owner id.
     *
     * @return the owner id
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the owner id.
     *
     * @param customUserId
     *        the new owner id
     */
    public void setOwnerId(Long customUserId) {
        this.ownerId = customUserId;
    }

    /**
     * Gets the icon id.
     *
     * @return the icon id
     */
    public Long getIconId() {
        return iconId;
    }

    /**
     * Sets the icon id.
     *
     * @param iconId
     *        the new icon id
     */
    public void setIconId(Long iconId) {
        this.iconId = iconId;
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

        TeamDTO teamDTO = (TeamDTO) o;
        if (teamDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamDTO.getId());
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
        return "TeamDTO{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='"
                + getDescription() + "'" + ", level='" + getLevel() + "'" + ", totalMember='"
                + getTotalMember() + "'" + ", extraGroupName='" + getExtraGroupName() + "'"
                + ", extraGroupDescription='" + getExtraGroupDescription() + "'"
                + ", extraGroupTotalMember='" + getExtraGroupTotalMember() + "'" + "}";
    }

    /**
     * To team detail dto.
     *
     * @return the team detail DTO
     */
    public TeamDetailDTO toTeamDetailDto() {
        TeamDetailDTO detailDTO = new TeamDetailDTO();
        detailDTO.setId(id);
        detailDTO.setIconId(iconId);
        detailDTO.setLevel(level);
        detailDTO.setName(name);
        detailDTO.setTotalMember(totalMember);
        detailDTO.setDescription(description);
        detailDTO.setExtraGroupDescription(extraGroupDescription);
        detailDTO.setExtraGroupName(extraGroupName);
        detailDTO.setExtraGroupTotalMember(extraGroupTotalMember);
        return detailDTO;
    }
}
