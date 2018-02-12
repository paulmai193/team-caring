/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the GroupsMember entity.
 *
 * @author Dai Mai
 */
public class SimpleGroupsMemberDTO implements Serializable {

    /** The id. */
    @NotNull
    private Long    id;

    /** The level. */
    @NotNull
    private Integer level;

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
     * @return the simple groups member DTO
     */
    public SimpleGroupsMemberDTO id(Long id) {
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
     * @return the simple groups member DTO
     */
    public SimpleGroupsMemberDTO level(Integer level) {
        this.setLevel(level);
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

        SimpleGroupsMemberDTO groupsMemberDTO = (SimpleGroupsMemberDTO) o;
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
        return "GroupsMemberDTO{" + "id=" + getId() + ", level='" + getLevel() + "'" + "}";
    }

}
