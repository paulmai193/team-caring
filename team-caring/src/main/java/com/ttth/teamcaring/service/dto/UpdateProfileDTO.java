package com.ttth.teamcaring.service.dto;

import java.io.Serializable;

public class UpdateProfileDTO implements Serializable {
	private UserDTO profile;
	private AnonymousGroupDTO anonymousGroup;
	public UpdateProfileDTO(UserDTO profile, AnonymousGroupDTO anonymousGroup) {
		super();
		this.profile = profile;
		this.anonymousGroup = anonymousGroup;
	}
	/**
	 * @return the profile
	 */
	public UserDTO getProfile() {
		return profile;
	}
	/**
	 * @param profile the profile to set
	 */
	public void setProfile(UserDTO profile) {
		this.profile = profile;
	}
	/**
	 * @return the anonymousGroup
	 */
	public AnonymousGroupDTO getAnonymousGroup() {
		return anonymousGroup;
	}
	/**
	 * @param anonymousGroup the anonymousGroup to set
	 */
	public void setAnonymousGroup(AnonymousGroupDTO anonymousGroup) {
		this.anonymousGroup = anonymousGroup;
	}
	
	
}
