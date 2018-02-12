/*
 * 
 */
package com.ttth.teamcaring.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A DTO for the Notification entity.
 *
 * @author Dai Mai
 */
@JsonIgnoreProperties(value = { "lastModifiedDate" }, allowGetters = true)
public class NotificationDTO implements Serializable {

    /** The id. */
    private Long    id;

    /** The title. */
    private String  title;

    /** The message. */
    private String  message;

    /** The read. */
    private Boolean read;

    /** The type. */
    private Integer type;

    /** The target id. */
    private Long    targetId;

    /** The custom user id. */
    private Long    customUserId;

    /** The last modified date. */
    @JsonProperty("time")
    private String  lastModifiedDate;

    /**
     * Instantiates a new notification DTO.
     */
    public NotificationDTO() {
        this.read = false;
    }

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
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *        the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Title.
     *
     * @param title
     *        the title
     * @return the notification DTO
     */
    public NotificationDTO title(String title) {
        this.setTitle(title);
        return this;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message
     *        the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Message.
     *
     * @param message
     *        the message
     * @return the notification DTO
     */
    public NotificationDTO message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * Checks if is read.
     *
     * @return the boolean
     */
    public Boolean isRead() {
        return read;
    }

    /**
     * Sets the read.
     *
     * @param read
     *        the new read
     */
    public void setRead(Boolean read) {
        this.read = read;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *        the new type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * Type.
     *
     * @param type
     *        the type
     * @return the notification DTO
     */
    public NotificationDTO type(Integer type) {
        this.setType(type);
        return this;
    }

    /**
     * Gets the target id.
     *
     * @return the target id
     */
    public Long getTargetId() {
        return targetId;
    }

    /**
     * Sets the target id.
     *
     * @param targetId
     *        the new target id
     */
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    /**
     * Target id.
     *
     * @param targetId
     *        the target id
     * @return the notification DTO
     */
    public NotificationDTO targetId(Long targetId) {
        this.setTargetId(targetId);
        return this;
    }

    /**
     * Gets the custom user id.
     *
     * @return the custom user id
     */
    public Long getCustomUserId() {
        return customUserId;
    }

    /**
     * Sets the custom user id.
     *
     * @param customUserId
     *        the new custom user id
     */
    public void setCustomUserId(Long customUserId) {
        this.customUserId = customUserId;
    }

    /**
     * Custom user id.
     *
     * @param customUserId
     *        the custom user id
     * @return the notification DTO
     */
    public NotificationDTO customUserId(Long customUserId) {
        this.setCustomUserId(customUserId);
        return this;
    }

    /**
     * Gets the last modified date.
     *
     * @return the lastModifiedDate
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the last modified date.
     *
     * @param lastModifiedDate
     *        the lastModifiedDate to set
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (notificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationDTO.getId());
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
        return "NotificationDTO{" + "id=" + getId() + ", title='" + getTitle() + "'" + ", message='"
                + getMessage() + "'" + ", read='" + isRead() + "'" + ", type='" + getType() + "'"
                + ", targetId='" + getTargetId() + "'" + "}";
    }
}
