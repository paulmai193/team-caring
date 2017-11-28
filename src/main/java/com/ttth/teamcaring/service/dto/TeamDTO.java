package com.ttth.teamcaring.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Team entity.
 *
 * @author Dai Mai
 */
public class TeamDTO implements Serializable, Cloneable {

    /** The id. */
    private Long id;

    /** The name. */
    private String name;

    /** The description. */
    private String description;

    /** The level. */
    private Integer level;

    /** The icon id. */
    private Long iconId;

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
     * @param id the new id
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
     * @param name the new name
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
     * @param description the new description
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
     * @param level the new level
     */
    public void setLevel(Integer level) {
        this.level = level;
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
     * @param iconId the new icon id
     */
    public void setIconId(Long iconId) {
        this.iconId = iconId;
    }

    /* (non-Javadoc)
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
        if(teamDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamDTO.getId());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TeamDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
