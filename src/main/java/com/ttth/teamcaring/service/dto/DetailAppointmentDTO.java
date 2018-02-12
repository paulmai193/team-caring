/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.util.Collection;

/**
 * The Class DetailAppointmentDTO.
 *
 * @author Dai Mai
 */
public class DetailAppointmentDTO extends UpdateAppointmentDTO
        implements Comparable<DetailAppointmentDTO> {

    /** The Constant serialVersionUID. */
    private static final long    serialVersionUID = 1L;

    /** The image url. */
    private String               imageUrl;

    /** The team. */
    private String               team;

    /** The member. */
    private String               member;

    /** The status. */
    private int status;

    /** The notes. */
    private Collection<NoteDTO>  notes;

    /**
     * Gets the image url.
     *
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image url.
     *
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the team.
     *
     * @return the team
     */
    public String getTeam() {
        return team;
    }

    /**
     * Sets the team.
     *
     * @param team the team to set
     */
    public void setTeam(String team) {
        this.team = team;
    }

    /**
     * Gets the member.
     *
     * @return the member
     */
    public String getMember() {
        return member;
    }

    /**
     * Sets the member.
     *
     * @param member the member to set
     */
    public void setMember(String member) {
        this.member = member;
    }

    /**
     * Gets the notes.
     *
     * @return the notes
     */
    public Collection<NoteDTO> getNotes() {
        return notes;
    }

    /**
     * Sets the notes.
     *
     * @param notes the notes to set
     */
    public void setNotes(Collection<NoteDTO> notes) {
        this.notes = notes;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UpdateAppointmentDTO [id=").append(getId()).append(", name=")
                .append(getName()).append(", description=").append(getDescription())
                .append(", time=").append(getCreatedDate()).append(", repeatType=")
                .append(getRepeatType()).append(", userId=").append(getUserId())
                .append(", imageUrl=").append(getImageUrl()).append(", teamId=").append(getTeamId())
                .append(", team=").append(team).append(", member=").append(member)
                .append(", status=").append(status).append("]");
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(DetailAppointmentDTO o) {
        return o.getCreatedDate().compareTo(this.getCreatedDate());
    }

}
