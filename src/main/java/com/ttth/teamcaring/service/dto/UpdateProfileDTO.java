package com.ttth.teamcaring.service.dto;

import java.io.Serializable;

public class UpdateProfileDTO implements Serializable {
	
	private AnonymousGroupDTO anonymousGroup;
	
	private CustomUserDTO profile;

	public UpdateProfileDTO(AnonymousGroupDTO anonymousGroup, CustomUserDTO profile) {
		super();
		this.anonymousGroup = anonymousGroup;
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

	/**
	 * @return the profile
	 */
	public CustomUserDTO getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(CustomUserDTO profile) {
		this.profile = profile;
	}
	
}
