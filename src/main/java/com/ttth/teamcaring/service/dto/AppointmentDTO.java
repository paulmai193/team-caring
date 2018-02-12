/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A DTO for the Appointment entity.
 *
 * @author Dai Mai
 */
public class AppointmentDTO implements Serializable {

    /** The id. */
    private Long    id;

    /** The name. */
    private String  name;

    /** The description. */
    private String  description;

    /** The repeat type. */
    private Integer repeatType;

    /** The custom user id. */
    private Long    customUserId;

    /** The team id. */
    private Long    teamId;

    /** The last modified date. (yyyy-MM-dd HH:mm) */
    @JsonProperty("time")
    private String  time;

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
     * Name.
     *
     * @param name
     *        the name
     * @return the appointment DTO
     */
    public AppointmentDTO name(String name) {
        this.setName(name);
        return this;
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
     * Description.
     *
     * @param description
     *        the description
     * @return the appointment DTO
     */
    public AppointmentDTO description(String description) {
        this.setDescription(description);
        return this;
    }

    /**
     * Gets the repeat type.
     *
     * @return the repeat type
     */
    public Integer getRepeatType() {
        return repeatType;
    }

    /**
     * Sets the repeat type.
     *
     * @param repeatType
     *        the new repeat type
     */
    public void setRepeatType(Integer repeatType) {
        this.repeatType = repeatType;
    }

    /**
     * Repeat type.
     *
     * @param repeatType
     *        the repeat type
     * @return the appointment DTO
     */
    public AppointmentDTO repeatType(Integer repeatType) {
        this.setRepeatType(repeatType);
        return this;
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
     * @return the appointment DTO
     */
    public AppointmentDTO customUserId(Long customUserId) {
        this.setCustomUserId(customUserId);
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
     * @return the appointment DTO
     */
    public AppointmentDTO teamId(Long teamId) {
        this.setTeamId(teamId);
        return this;
    }

    

    
    /**
     * Gets the time.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    
    /**
     * Sets the time.
     *
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Time.
     *
     * @param time the time
     * @return the appointment DTO
     */
    public AppointmentDTO time(String time) {
        this.setTime(time);
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

        AppointmentDTO appointmentDTO = (AppointmentDTO) o;
        if (appointmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appointmentDTO.getId());
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
        StringBuilder builder = new StringBuilder();
        builder.append("AppointmentDTO [id=").append(id).append(", name=").append(name)
                .append(", description=").append(description).append(", repeatType=")
                .append(repeatType).append(", customUserId=").append(customUserId)
                .append(", teamId=").append(teamId).append(", time=")
                .append(time).append("]");
        return builder.toString();
    }

}
