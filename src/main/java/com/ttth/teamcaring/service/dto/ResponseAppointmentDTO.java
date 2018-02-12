/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class ResponseAppointmentDTO.
 *
 * @author Dai Mai
 */
public class ResponseAppointmentDTO implements Serializable {

    /** The groups member id. */
    @NotNull
    @JsonProperty("requestId")
    private Long                   attendeeId;

    /** The response. */
    @NotNull
    private AnwserResponseStatus response;    
    
    /**
     * Gets the attendee id.
     *
     * @return the attendeeId
     */
    public Long getAttendeeId() {
        return attendeeId;
    }

    /**
     * Sets the attendee id.
     *
     * @param attendeeId the attendeeId to set
     */
    public void setAttendeeId(Long attendeeId) {
        this.attendeeId = attendeeId;
    }

    /**
     * Attendee id.
     *
     * @param appointmentId the appointment id
     * @return the response appointment DTO
     */
    public ResponseAppointmentDTO attendeeId(Long appointmentId) {
        this.setAttendeeId(appointmentId);
        return this;
    }

    /**
     * Gets the response.
     *
     * @return the response
     */
    public AnwserResponseStatus getResponse() {
        return response;
    }

    /**
     * Sets the response.
     *
     * @param response
     *        the response to set
     */
    public void setResponse(AnwserResponseStatus response) {
        this.response = response;
    }

    /**
     * Response.
     *
     * @param response
     *        the response
     * @return the response join team DTO
     */
    public ResponseAppointmentDTO response(AnwserResponseStatus response) {
        this.setResponse(response);
        return this;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResponseAppointmentDTO [attendeeId=").append(attendeeId)
                .append(", response=").append(response).append("]");
        return builder.toString();
    }

}
