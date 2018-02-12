/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * The Class ProfileDTO.
 *
 * @author Dai Mai
 */
public class ProfileDTO extends CustomUserDTO {

    /** The number appointments. */
    private int                   numberAppointments = 0;

    /** The members. */
    private Set<ProfileDTO>       members            = new HashSet<>();

    /** The joined team. */
    private SimpleGroupsMemberDTO joinedTeam;

    /**
     * Gets the number appointments.
     *
     * @return the numberAppointments
     */
    public int getNumberAppointments() {
        return numberAppointments;
    }

    /**
     * Sets the number appointments.
     *
     * @param numberAppointments
     *        the numberAppointments to set
     */
    public void setNumberAppointments(int numberAppointments) {
        this.numberAppointments = numberAppointments;
    }

    /**
     * Number appointments.
     *
     * @param numberAppointment the number appointment
     * @return the profile DTO
     */
    public ProfileDTO numberAppointments(int numberAppointment) {
        this.setNumberAppointments(numberAppointment);
        return this;
    }

    /**
     * Gets the members.
     *
     * @return the members
     */
    public Set<ProfileDTO> getMembers() {
        return members;
    }

    /**
     * Sets the members.
     *
     * @param members
     *        the members to set
     */
    public void setMembers(Set<ProfileDTO> members) {
        this.members = members;
    }

    /**
     * Gets the joined team.
     *
     * @return the joinedTeam
     */
    public SimpleGroupsMemberDTO getJoinedTeam() {
        return joinedTeam;
    }

    /**
     * Sets the joined team.
     *
     * @param joinedTeam
     *        the joinedTeam to set
     */
    public void setJoinedTeam(SimpleGroupsMemberDTO joinedTeam) {
        this.joinedTeam = joinedTeam;
    }

    /**
     * Joined team.
     *
     * @param joinedTeam
     *        the joined team
     * @return the profile DTO
     */
    public ProfileDTO joinedTeam(SimpleGroupsMemberDTO joinedTeam) {
        this.setJoinedTeam(joinedTeam);
        return this;
    }

    /**
     * Joined team.
     *
     * @param joinedTeam
     *        the joined team
     * @return the profile DTO
     */
    public ProfileDTO joinedTeam(GroupsMemberDTO joinedTeam) {
        return this.joinedTeam(joinedTeam.castToSimpleGroupsMemberDTO());
    }

}
