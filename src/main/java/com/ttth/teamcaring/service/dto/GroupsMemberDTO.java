package com.ttth.teamcaring.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the GroupsMember entity.
 */
public class GroupsMemberDTO implements Serializable {

    private Long id;

    private Integer level;

    private Set<CustomUserDTO> members = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Set<CustomUserDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<CustomUserDTO> customUsers) {
        this.members = customUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupsMemberDTO groupsMemberDTO = (GroupsMemberDTO) o;
        if(groupsMemberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupsMemberDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroupsMemberDTO{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
