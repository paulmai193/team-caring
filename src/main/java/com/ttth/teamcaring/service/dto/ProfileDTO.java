package com.ttth.teamcaring.service.dto;

import java.io.Serializable;

/**
 * The Class ProfileDTO.
 *
 * @author Dai Mai
 */
public class ProfileDTO implements Serializable {
	
	/** The anonymous group. */
	private AnonymousGroupDTO anonymousGroup;
	
	/** The profile. */
	private CustomUserDTO profile;

	/**
	 * Gets the anonymous group.
	 *
	 * @return the anonymousGroup
	 */
	public AnonymousGroupDTO getAnonymousGroup() {
		return anonymousGroup;
	}

	/**
	 * Sets the anonymous group.
	 *
	 * @param anonymousGroup the anonymousGroup to set
	 */
	public void setAnonymousGroup(AnonymousGroupDTO anonymousGroup) {
		this.anonymousGroup = anonymousGroup;
	}

	/**
	 * Gets the profile.
	 *
	 * @return the profile
	 */
	public CustomUserDTO getProfile() {
		return profile;
	}

	/**
	 * Sets the profile.
	 *
	 * @param profile the profile to set
	 */
	public void setProfile(CustomUserDTO profile) {
		this.profile = profile;
	}
	
}
