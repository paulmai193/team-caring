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
 * A Attendee.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "attendee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "attendee")
public class Attendee implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant STATUS_PENDING. */
    public static final int   STATUS_PENDING   = 0;

    /** The Constant STATUS_ACCEPTED. */
    public static final int   STATUS_ACCEPTED  = 1;

    /** The Constant STATUS_REJECTED. */
    public static final int   STATUS_REJECTED  = 2;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              id;

    /** The status. */
    @Column(name = "status")
    private Integer           status;

    /** The custom user. */
    @ManyToOne
    private CustomUser        customUser;

    /** The appointment. */
    @ManyToOne
    private Appointment       appointment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not
    /**
     * Gets the id.
     *
     * @return the id
     */
    // remove
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Status.
     *
     * @param status the status
     * @return the attendee
     */
    public Attendee status(Integer status) {
        this.status = status;
        return this;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * @param customUser the custom user
     * @return the attendee
     */
    public Attendee customUser(CustomUser customUser) {
        this.customUser = customUser;
        return this;
    }

    /**
     * Sets the custom user.
     *
     * @param customUser the new custom user
     */
    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
    }

    /**
     * Gets the appointment.
     *
     * @return the appointment
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Appointment.
     *
     * @param appointment the appointment
     * @return the attendee
     */
    public Attendee appointment(Appointment appointment) {
        this.appointment = appointment;
        return this;
    }

    /**
     * Sets the appointment.
     *
     * @param appointment the new appointment
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters
    // and setters here, do not remove

    /* (non-Javadoc)
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
        Attendee attendee = (Attendee) o;
        if (attendee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attendee.getId());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Attendee{" + "id=" + getId() + ", status='" + getStatus() + "'" + "}";
    }
}
