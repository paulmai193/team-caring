/*
 * 
 */
package com.ttth.teamcaring.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Notification.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notification")
public class Notification extends AbstractAuditingEntity
        implements Serializable, Comparable<Notification> {

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID          = 1L;

    /** The Constant TYPE_REQUEST_TEAM. */
    public static final Integer TYPE_REQUEST_TEAM         = 1;

    /** The Constant TYPE_RESPONSE_TEAM. */
    public static final Integer TYPE_RESPONSE_TEAM        = 2;

    /** The Constant TYPE_REQUEST_APPOINTMENT. */
    public static final Integer TYPE_REQUEST_APPOINTMENT  = 3;

    /** The Constant TYPE_RESPONSE_APPOINTMENT. */
    public static final Integer TYPE_RESPONSE_APPOINTMENT = 4;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long                id;

    /** The title. */
    @Column(name = "title")
    private String              title;

    /** The message. */
    @Column(name = "message")
    private String              message;

    /** The read. */
    @Column(name = "jhi_read")
    private Boolean             read;

    /** The type. */
    @Column(name = "jhi_type")
    private Integer             type;

    /** The target id. */
    @Column(name = "target_id")
    private Long                targetId;

    /** The custom user. */
    @ManyToOne
    private CustomUser          customUser;

    /**
     * Gets the id.
     *
     * @return the id
     */
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not
    // remove
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
     * Title.
     *
     * @param title
     *        the title
     * @return the notification
     */
    public Notification title(String title) {
        this.title = title;
        return this;
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
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Message.
     *
     * @param message
     *        the message
     * @return the notification
     */
    public Notification message(String message) {
        this.message = message;
        return this;
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
     * Checks if is read.
     *
     * @return the boolean
     */
    public Boolean isRead() {
        return read;
    }

    /**
     * Read.
     *
     * @param read
     *        the read
     * @return the notification
     */
    public Notification read(Boolean read) {
        this.read = read;
        return this;
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
     * Type.
     *
     * @param type
     *        the type
     * @return the notification
     */
    public Notification type(Integer type) {
        this.type = type;
        return this;
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
     * Gets the target id.
     *
     * @return the target id
     */
    public Long getTargetId() {
        return targetId;
    }

    /**
     * Target id.
     *
     * @param targetId
     *        the target id
     * @return the notification
     */
    public Notification targetId(Long targetId) {
        this.targetId = targetId;
        return this;
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
     * Gets the custom user.
     *
     * @return the custom user
     */
    public CustomUser getCustomUser() {
        return customUser;
    }

    /**
     * Custom user.
     *
     * @param customUser
     *        the custom user
     * @return the notification
     */
    public Notification customUser(CustomUser customUser) {
        this.customUser = customUser;
        return this;
    }

    /**
     * Sets the custom user.
     *
     * @param customUser
     *        the new custom user
     */
    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters
    // and setters here, do not remove

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
        Notification notification = (Notification) o;
        if (notification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notification.getId());
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
        return "Notification{" + "id=" + getId() + ", title='" + getTitle() + "'" + ", message='"
                + getMessage() + "'" + ", read='" + isRead() + "'" + ", type='" + getType() + "'"
                + ", targetId='" + getTargetId() + "'" + "}";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Notification o) {
        return this.getLastModifiedDate().compareTo(o.getLastModifiedDate());
    }
}
