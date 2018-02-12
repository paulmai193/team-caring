/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttth.teamcaring.domain.CustomUser;

/**
 * The Class CreateAppointmentDTO.
 *
 * @author Dai Mai
 */
public class CreateAppointmentDTO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long     serialVersionUID = 1L;

    /** The name. */
    private String                name;

    /** The description. */
    private String                description;

    /** The last modified date. (yyyy-MM-dd HH:mm) */
    @JsonProperty("time")
    private String                createdDate;

    /** The repeat type. */
    private AppointmentRepeatType repeatType;

    /** The user id. */
    private Long                  userId;

    /** The team id. */
    private Long                  teamId;

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
     *        the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Name.
     *
     * @param name the name
     * @return the creates the appointment DTO
     */
    public CreateAppointmentDTO name(String name) {
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
     *        the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Description.
     *
     * @param description the description
     * @return the creates the appointment DTO
     */
    public CreateAppointmentDTO description(String description) {
        this.setDescription(description);
        return this;
    }

    /**
     * Gets the created date.
     *
     * @return the createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the created date.
     *
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Created date.
     *
     * @param createdDate the created date
     * @return the creates the appointment DTO
     */
    public CreateAppointmentDTO createdDate(String createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    /**
     * Gets the repeat type.
     *
     * @return the repeatType
     */
    public AppointmentRepeatType getRepeatType() {
        return repeatType;
    }

    /**
     * Sets the repeat type.
     *
     * @param repeatType
     *        the repeatType to set
     */
    public void setRepeatType(AppointmentRepeatType repeatType) {
        this.repeatType = repeatType;
    }

    /**
     * Repeat type.
     *
     * @param repeatType the repeat type
     * @return the creates the appointment DTO
     */
    public CreateAppointmentDTO repeatType(AppointmentRepeatType repeatType) {
        this.setRepeatType(repeatType);
        return this;
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

    /**
     * User id.
     *
     * @param userId the user id
     * @return the creates the appointment DTO
     */
    public CreateAppointmentDTO userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

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
     * Team id.
     *
     * @param teamId the team id
     * @return the creates the appointment DTO
     */
    public CreateAppointmentDTO teamId(Long teamId) {
        this.setTeamId(teamId);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CreateAppointmentDTO [name=").append(name).append(", description=")
                .append(description).append(", time=").append(createdDate).append(", repeatType=")
                .append(repeatType).append(", userId=").append(userId).append(", teamId=")
                .append(teamId).append("]");
        return builder.toString();
    }

    /**
     * Cast to appointment DTO.
     *
     * @param customUser
     *        the custom user
     * @return the appointment DTO
     */
    public AppointmentDTO castToAppointmentDTO(CustomUser customUser) {
        return castToAppointmentDTO(customUser.getId());
    }

    /**
     * Cast to appointment DTO.
     *
     * @param customUserDTO
     *        the custom user DTO
     * @return the appointment DTO
     */
    public AppointmentDTO castToAppointmentDTO(CustomUserDTO customUserDTO) {
        return castToAppointmentDTO(customUserDTO.getId());
    }

    /**
     * Cast to appointment DTO.
     *
     * @param customUserId
     *        the custom user id
     * @return the appointment DTO
     */
    private AppointmentDTO castToAppointmentDTO(Long customUserId) {
        return new AppointmentDTO().customUserId(customUserId).description(description).name(name)
                .repeatType(repeatType.getValue()).teamId(teamId).time(createdDate);
    }

    /**
     * Cast to update appointment DTO.
     *
     * @return the update appointment DTO
     */
    public UpdateAppointmentDTO castToUpdateAppointmentDTO() {
        UpdateAppointmentDTO updateAppointmentDTO = new UpdateAppointmentDTO();
        updateAppointmentDTO.setDescription(description);
        updateAppointmentDTO.setCreatedDate(createdDate);
        updateAppointmentDTO.setName(name);
        updateAppointmentDTO.setRepeatType(repeatType);
        updateAppointmentDTO.setTeamId(teamId);
        updateAppointmentDTO.setUserId(userId);

        return updateAppointmentDTO;
    }

}
