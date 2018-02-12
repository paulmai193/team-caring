/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A DTO for the Note entity.
 *
 * @author Dai Mai
 */
public class NoteDTO implements Serializable {

    /** The id. */
    private Long   id;

    /** The general. */
    private String general;

    /** The separate. */
    private String separate;

    /** The reminder. */
    private String reminder;

    /** The custom user id. */
    private Long   customUserId;

    /** The appointment id. */
    private Long   appointmentId;

    /** The last modified date. */
    @JsonProperty("time")
    private String createdDate;

    private String imageUrl;

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
     * Gets the general.
     *
     * @return the general
     */
    public String getGeneral() {
        return general;
    }

    /**
     * Sets the general.
     *
     * @param general the new general
     */
    public void setGeneral(String general) {
        this.general = general;
    }

    /**
     * General.
     *
     * @param general the general
     * @return the note DTO
     */
    public NoteDTO general(String general) {
        this.setGeneral(general);
        return this;
    }

    /**
     * Gets the separate.
     *
     * @return the separate
     */
    public String getSeparate() {
        return separate;
    }

    /**
     * Sets the separate.
     *
     * @param separate the new separate
     */
    public void setSeparate(String separate) {
        this.separate = separate;
    }

    /**
     * Separate.
     *
     * @param separate the separate
     * @return the note DTO
     */
    public NoteDTO separate(String separate) {
        this.setSeparate(separate);
        return this;
    }

    /**
     * Gets the reminder.
     *
     * @return the reminder
     */
    public String getReminder() {
        return reminder;
    }

    /**
     * Sets the reminder.
     *
     * @param reminder the new reminder
     */
    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    /**
     * Reminder.
     *
     * @param reminder the reminder
     * @return the note DTO
     */
    public NoteDTO reminder(String reminder) {
        this.setReminder(reminder);
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
     * @param customUserId the new custom user id
     */
    public void setCustomUserId(Long customUserId) {
        this.customUserId = customUserId;
    }

    /**
     * Custom user id.
     *
     * @param customUserId the custom user id
     * @return the note DTO
     */
    public NoteDTO customUserId(Long customUserId) {
        this.setCustomUserId(customUserId);
        return this;
    }

    /**
     * Gets the appointment id.
     *
     * @return the appointment id
     */
    public Long getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the appointment id.
     *
     * @param appointmentId the new appointment id
     */
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Appointment id.
     *
     * @param appointmentId the appointment id
     * @return the note DTO
     */
    public NoteDTO appointmentId(Long appointmentId) {
        this.setAppointmentId(appointmentId);
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
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

        NoteDTO noteDTO = (NoteDTO) o;
        if (noteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), noteDTO.getId());
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
        builder.append("NoteDTO [id=").append(id).append(", general=").append(general)
                .append(", separate=").append(separate).append(", reminder=").append(reminder)
                .append(", customUserId=").append(customUserId).append(", appointmentId=")
                .append(appointmentId).append(", createdDate=").append(createdDate)
                .append(", imageUrl=").append(imageUrl).append("]");
        return builder.toString();
    }

}
