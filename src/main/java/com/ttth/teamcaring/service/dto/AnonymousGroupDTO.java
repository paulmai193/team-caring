package com.ttth.teamcaring.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class AnonymousGroupDTO.
 *
 * @author Dai Mai
 */
public class AnonymousGroupDTO implements Serializable {
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;

    /** The offical. */
	@JsonIgnore
    private Boolean offical;

    /** The total member. */
    private Integer totalMember;

    /**
     * Instantiates a new anonymous group DTO.
     */
    public AnonymousGroupDTO() {
		this.offical = false;
	}
    
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Checks if is offical.
	 *
	 * @return the offical
	 */
	public Boolean isOffical() {
		return offical;
	}

	/**
	 * Gets the total member.
	 *
	 * @return the totalMember
	 */
	public Integer getTotalMember() {
		return totalMember;
	}

	/**
	 * Sets the total member.
	 *
	 * @param totalMember the totalMember to set
	 */
	public void setTotalMember(Integer totalMember) {
		this.totalMember = totalMember;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GroupsDTO{" +
            "description='" + getDescription() + "'" +
            ", offical='" + isOffical() + "'" +
            ", totalMember='" + getTotalMember() + "'" +
            "}";
    }
}
