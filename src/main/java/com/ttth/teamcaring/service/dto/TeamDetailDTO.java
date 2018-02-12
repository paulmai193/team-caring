/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * The Class TeamDetailDTO.
 *
 * @author Dai Mai
 */
public class TeamDetailDTO extends TeamDTO {

    /** The member level. */
    private Integer         memberLevel;

    /** The members. */
    private Set<ProfileDTO> members = new HashSet<>();

    /**
     * Gets the member level.
     *
     * @return the memberLevel
     */
    public Integer getMemberLevel() {
        return memberLevel;
    }

    /**
     * Sets the member level.
     *
     * @param memberLevel
     *        the memberLevel to set
     */
    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
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

}
