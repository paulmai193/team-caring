/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CustomUser entity.
 *
 * @author Dai Mai
 */
public class CustomUserDTO implements Serializable {

    /** The id. */
    private Long    id;

    /** The full name. */
    private String  fullName;

    /** The nickname. */
    private String  nickname;

    /** The email. */
    private String  email;

    /** The image url. */
    private String  imageUrl;

    /** The push token. */
    private String  pushToken;

    /** The extra group name. */
    private String  extraGroupName;

    /** The extra group description. */
    private String  extraGroupDescription;

    /** The extra group total member. */
    private Integer extraGroupTotalMember;

    /** The user id. */
    private Long    userId;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *        the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param fullName
     *        the new full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname.
     *
     * @param nickname
     *        the new nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email
     *        the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the image url.
     *
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image url.
     *
     * @param imageUrl
     *        the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the push token.
     *
     * @return the push token
     */
    public String getPushToken() {
        return pushToken;
    }

    /**
     * Sets the push token.
     *
     * @param pushToken
     *        the new push token
     */
    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    /**
     * Gets the extra group name.
     *
     * @return the extra group name
     */
    public String getExtraGroupName() {
        return extraGroupName;
    }

    /**
     * Sets the extra group name.
     *
     * @param extraGroupName
     *        the new extra group name
     */
    public void setExtraGroupName(String extraGroupName) {
        this.extraGroupName = extraGroupName;
    }

    /**
     * Gets the extra group description.
     *
     * @return the extra group description
     */
    public String getExtraGroupDescription() {
        return extraGroupDescription;
    }

    /**
     * Sets the extra group description.
     *
     * @param extraGroupDescription
     *        the new extra group description
     */
    public void setExtraGroupDescription(String extraGroupDescription) {
        this.extraGroupDescription = extraGroupDescription;
    }

    /**
     * Gets the extra group total member.
     *
     * @return the extra group total member
     */
    public Integer getExtraGroupTotalMember() {
        return extraGroupTotalMember;
    }

    /**
     * Sets the extra group total member.
     *
     * @param extraGroupTotalMember
     *        the new extra group total member
     */
    public void setExtraGroupTotalMember(Integer extraGroupTotalMember) {
        this.extraGroupTotalMember = extraGroupTotalMember;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *        the new user id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomUserDTO customUserDTO = (CustomUserDTO) o;
        if (customUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customUserDTO.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CustomUserDTO{" + "id=" + getId() + ", fullName='" + getFullName() + "'"
                + ", nickname='" + getNickname() + "'" + ", email='" + getEmail() + "'"
                + ", imageUrl='" + getImageUrl() + "'" + ", pushToken='" + getPushToken() + "'"
                + ", extraGroupName='" + getExtraGroupName() + "'" + ", extraGroupDescription='"
                + getExtraGroupDescription() + "'" + ", extraGroupTotalMember='"
                + getExtraGroupTotalMember() + "'" + "}";
    }

    /**
     * Cast to simple profile dto.
     *
     * @return the profile DTO
     */
    public ProfileDTO castToSimpleProfileDto() {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(id);
        profileDTO.setFullName(fullName);
        profileDTO.setNickname(nickname);
        profileDTO.setImageUrl(imageUrl);
        profileDTO.setUserId(userId);
        profileDTO.setEmail(email);
        return profileDTO;
    }

    /**
     * Cast to profile dto.
     *
     * @return the profile DTO
     */
    public ProfileDTO castToProfileDto() {
        ProfileDTO profileDTO = this.castToSimpleProfileDto();
        profileDTO.setExtraGroupDescription(extraGroupDescription);
        profileDTO.setExtraGroupName(extraGroupName);
        profileDTO.setExtraGroupTotalMember(extraGroupTotalMember);
        return profileDTO;
    }
}
