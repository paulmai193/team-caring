/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Attendee entity.
 *
 * @author Dai Mai
 */
public class AttendeeDTO implements Serializable {

    /** The id. */
    private Long    id;

    /** The status. */
    private Integer status;

    /** The custom user id. */
    private Long    customUserId;

    /** The appointment id. */
    private Long    appointmentId;

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
     * Gets the status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Status.
     *
     * @param status the status
     * @return the attendee DTO
     */
    public AttendeeDTO status(Integer status) {
        this.setStatus(status);
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
     * @return the attendee DTO
     */
    public AttendeeDTO customUserId(Long customUserId) {
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
     * @return the attendee DTO
     */
    public AttendeeDTO appointmentId(Long appointmentId) {
        this.setAppointmentId(appointmentId);
        return this;
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

        AttendeeDTO attendeeDTO = (AttendeeDTO) o;
        if (attendeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attendeeDTO.getId());
    }

    /* (non-Javadoc)
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
        builder.append("AttendeeDTO [id=").append(id).append(", status=").append(status)
                .append(", customUserId=").append(customUserId).append(", appointmentId=")
                .append(appointmentId).append("]");
        return builder.toString();
    }

}
