package com.ttth.teamcaring.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the Groups entity.
 */
public class GroupsDTO implements Serializable {

    private Long id;

    private String description;

    @JsonIgnore
    private Boolean offical;

    private Integer totalMember;

    @JsonIgnore
    private Long customUserId;

    private Set<GroupsMemberDTO> members = new HashSet<>();

    @JsonIgnore
    private Long teamId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isOffical() {
        return offical;
    }

    public void setOffical(Boolean offical) {
        this.offical = offical;
    }

    public Integer getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    public Long getCustomUserId() {
        return customUserId;
    }

    public void setCustomUserId(Long customUserId) {
        this.customUserId = customUserId;
    }

    public Set<GroupsMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<GroupsMemberDTO> groupsMembers) {
        this.members = groupsMembers;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupsDTO groupsDTO = (GroupsDTO) o;
        if(groupsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroupsDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", offical='" + isOffical() + "'" +
            ", totalMember='" + getTotalMember() + "'" +
            "}";
    }
}
