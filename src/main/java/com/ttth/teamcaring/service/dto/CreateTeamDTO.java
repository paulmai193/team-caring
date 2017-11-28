package com.ttth.teamcaring.service.dto;

/**
 * The Class CreateTeamDTO.
 *
 * @author Dai Mai
 */
public class CreateTeamDTO extends TeamDTO {
		
	/** The group. */
	private OfficalGroupDTO group;

	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	public OfficalGroupDTO getGroup() {
		return group;
	}

	/**
	 * Sets the group.
	 *
	 * @param group the new group
	 */
	public void setGroup(OfficalGroupDTO group) {
		this.group = group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateTeamDTO other = (CreateTeamDTO) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.getId().equals(other.group.getId()))
			return false;
		return true;
	}

}
