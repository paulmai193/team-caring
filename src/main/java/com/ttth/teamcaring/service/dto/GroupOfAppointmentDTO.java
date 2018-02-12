/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Class GroupOfAppointmentDTO.
 *
 * @author Dai Mai
 */
public class GroupOfAppointmentDTO implements Serializable, Comparable<GroupOfAppointmentDTO> {

    /** The user. */
    private CustomUserDTO              user;
    
    /** The list. */
    private Set<DetailAppointmentDTO> list;
    
    
    /**
     * Instantiates a new group of appointment DTO.
     */
    public GroupOfAppointmentDTO() {
    }

    /**
     * Instantiates a new group of appointment DTO.
     *
     * @param user the user
     */
    public GroupOfAppointmentDTO(CustomUserDTO user) {
        super();
        this.user = user;
        this.list = new TreeSet<>();
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public CustomUserDTO getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the user to set
     */
    public void setUser(CustomUserDTO user) {
        this.user = user;
    }

    /**
     * Gets the list.
     *
     * @return the list
     */
    public Set<DetailAppointmentDTO> getList() {
        return list;
    }

    /**
     * Sets the list.
     *
     * @param list the list to set
     */
    public void setList(Set<DetailAppointmentDTO> list) {
        this.list = list;
    }
    
    /**
     * Adds the list.
     *
     * @param item the item
     */
    public void addList(DetailAppointmentDTO item) {
        this.list.add(item);
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(GroupOfAppointmentDTO o) {
        return this.user.getNickname().compareToIgnoreCase(o.getUser().getNickname());
    }
}
