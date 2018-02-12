/*
 * 
 */
package com.ttth.teamcaring.service.dto;

/**
 * The Class UpdateAppointmentDTO.
 *
 * @author Dai Mai
 */
public class UpdateAppointmentDTO extends CreateAppointmentDTO {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private Long              id;

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
     * @param id        the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UpdateAppointmentDTO [id=").append(id).append(", name=").append(getName())
                .append(", description=").append(getDescription()).append(", time=")
                .append(getCreatedDate()).append(", repeatType=").append(getRepeatType())
                .append(", userId=").append(getUserId()).append(", teamId=").append(getTeamId())
                .append("]");
        return builder.toString();
    }

}
