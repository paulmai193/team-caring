/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the Note entity.
 *
 * @author Dai Mai
 */
public class CreateNoteDTO implements Serializable {

    /** The general. */
    private String general;

    /** The separate. */
    private String separate;

    /** The reminder. */
    private String reminder;

    /** The custom user id. */
    @JsonIgnore
    private Long   customUserId;

    /** The appointment id. */
    @NotNull
    private Long   appointmentId;

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
     * Gets the custom user id.
     *
     * @return the customUserId
     */
    public Long getCustomUserId() {
        return customUserId;
    }

    /**
     * Sets the custom user id.
     *
     * @param customUserId the customUserId to set
     */
    public void setCustomUserId(Long customUserId) {
        this.customUserId = customUserId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CreateNoteDTO [general=").append(general).append(", separate=")
                .append(separate).append(", reminder=").append(reminder).append(", customUserId=")
                .append(customUserId).append(", appointmentId=").append(appointmentId).append("]");
        return builder.toString();
    }

    /**
     * Cast to note DTO.
     *
     * @return the note DTO
     */
    public NoteDTO castToNoteDTO() {
        return new NoteDTO().general(general).separate(separate).reminder(reminder)
                .appointmentId(appointmentId).customUserId(customUserId);
    }
}
