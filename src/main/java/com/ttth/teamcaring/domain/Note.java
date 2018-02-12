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
 * A Note.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "note")
public class Note extends AbstractAuditingEntity implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The general. */
    @Column(name = "jhi_general")
    private String general;

    /** The separate. */
    @Column(name = "separate")
    private String separate;

    /** The reminder. */
    @Column(name = "reminder")
    private String reminder;

    /** The custom user. */
    @ManyToOne
    private CustomUser customUser;

    /** The appointment. */
    @ManyToOne
    private Appointment appointment;

    /**
     * Gets the id.
     *
     * @return the id
     */
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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
     * Gets the general.
     *
     * @return the general
     */
    public String getGeneral() {
        return general;
    }

    /**
     * General.
     *
     * @param general the general
     * @return the note
     */
    public Note general(String general) {
        this.general = general;
        return this;
    }

    /**
     * Sets the general.
     *
     * @param general the new general
     */
    public void setGeneral(String general) {
        this.general = general;
    }

    /**
     * Gets the separate.
     *
     * @return the separate
     */
    public String getSeparate() {
        return separate;
    }

    /**
     * Separate.
     *
     * @param separate the separate
     * @return the note
     */
    public Note separate(String separate) {
        this.separate = separate;
        return this;
    }

    /**
     * Sets the separate.
     *
     * @param separate the new separate
     */
    public void setSeparate(String separate) {
        this.separate = separate;
    }

    /**
     * Gets the reminder.
     *
     * @return the reminder
     */
    public String getReminder() {
        return reminder;
    }

    /**
     * Reminder.
     *
     * @param reminder the reminder
     * @return the note
     */
    public Note reminder(String reminder) {
        this.reminder = reminder;
        return this;
    }

    /**
     * Sets the reminder.
     *
     * @param reminder the new reminder
     */
    public void setReminder(String reminder) {
        this.reminder = reminder;
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
     * @return the note
     */
    public Note customUser(CustomUser customUser) {
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
     * @return the note
     */
    public Note appointment(Appointment appointment) {
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        Note note = (Note) o;
        if (note.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), note.getId());
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
        return "Note{" +
            "id=" + getId() +
            ", general='" + getGeneral() + "'" +
            ", separate='" + getSeparate() + "'" +
            ", reminder='" + getReminder() + "'" +
            "}";
    }
}
