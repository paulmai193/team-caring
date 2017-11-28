package com.ttth.teamcaring.service.dto;


import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the CustomUser entity.
 */
public class CustomUserDTO implements Serializable {

	@JsonIgnore
    private Long id;

    private String fullName;

    private String nickname;

    @JsonIgnore
    private String pushToken;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomUserDTO customUserDTO = (CustomUserDTO) o;
        if(customUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomUserDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", pushToken='" + getPushToken() + "'" +
            "}";
    }
}
