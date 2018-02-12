/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * The Class RequestUpdateLevelDTO.
 *
 * @author Dai Mai
 */
public class RequestUpdateLevelDTO implements Serializable {

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
     * @return the request update level DTO
     */
    public RequestUpdateLevelDTO id(Long id) {
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
     * @return the request update level DTO
     */
    public RequestUpdateLevelDTO level(Integer level) {
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

        RequestUpdateLevelDTO requestUpdateLevelDTO = (RequestUpdateLevelDTO) o;
        if (requestUpdateLevelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requestUpdateLevelDTO.getId());
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
        return "RequestUpdateLevelDTO{" + "id=" + getId() + ", level='" + getLevel() + "'" + "}";
    }

}
