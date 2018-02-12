/*
 * 
 */
package com.ttth.teamcaring.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Appointment.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "appointment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appointment")
public class Appointment extends AbstractAuditingEntity
        implements Serializable, Comparable<Appointment> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              id;

    /** The name. */
    @Column(name = "name")
    private String            name;

    /** The description. */
    @Column(name = "description")
    private String            description;

    /** The repeat type. */
    @Column(name = "repeat_type")
    private Integer           repeatType;

    /** The custom user. */
    @ManyToOne
    private CustomUser        customUser;

    /** The team. */
    @ManyToOne
    private Team              team;

    /** The time. */
    @Column(name = "time", nullable = false)
    private Instant           time;

    /** The attendees. */
    @OneToMany(mappedBy = "appointment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attendee>     attendees        = new HashSet<>();

    /** The notes. */
    @OneToMany(mappedBy = "appointment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Note>         notes            = new HashSet<>();

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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Name.
     *
     * @param name the name
     * @return the appointment
     */
    public Appointment name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description.
     *
     * @param description the description
     * @return the appointment
     */
    public Appointment description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the repeat type.
     *
     * @return the repeat type
     */
    public Integer getRepeatType() {
        return repeatType;
    }

    /**
     * Repeat type.
     *
     * @param repeatType the repeat type
     * @return the appointment
     */
    public Appointment repeatType(Integer repeatType) {
        this.repeatType = repeatType;
        return this;
    }

    /**
     * Sets the repeat type.
     *
     * @param repeatType the new repeat type
     */
    public void setRepeatType(Integer repeatType) {
        this.repeatType = repeatType;
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    public Instant getTime() {
        return time;
    }

    /**
     * Sets the time.
     *
     * @param time the time to set
     */
    public void setTime(Instant time) {
        this.time = time;
    }
    
    /**
     * Time.
     *
     * @param time the time
     * @return the appointment
     */
    public Appointment time(Instant time) {
        this.setTime(time);
        return this;
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
     * @return the appointment
     */
    public Appointment customUser(CustomUser customUser) {
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
     * Gets the team.
     *
     * @return the team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Team.
     *
     * @param team the team
     * @return the appointment
     */
    public Appointment team(Team team) {
        this.team = team;
        return this;
    }

    /**
     * Sets the team.
     *
     * @param team the new team
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Gets the attendees.
     *
     * @return the attendees
     */
    public Set<Attendee> getAttendees() {
        return attendees;
    }

    /**
     * Attendees.
     *
     * @param attendees the attendees
     * @return the appointment
     */
    public Appointment attendees(Set<Attendee> attendees) {
        this.attendees = attendees;
        return this;
    }

    /**
     * Adds the attendee.
     *
     * @param attendee the attendee
     * @return the appointment
     */
    public Appointment addAttendee(Attendee attendee) {
        this.attendees.add(attendee);
        attendee.setAppointment(this);
        return this;
    }

    /**
     * Removes the attendee.
     *
     * @param attendee the attendee
     * @return the appointment
     */
    public Appointment removeAttendee(Attendee attendee) {
        this.attendees.remove(attendee);
        attendee.setAppointment(null);
        return this;
    }

    /**
     * Sets the attendees.
     *
     * @param attendees the new attendees
     */
    public void setAttendees(Set<Attendee> attendees) {
        this.attendees = attendees;
    }

    /**
     * Gets the notes.
     *
     * @return the notes
     */
    public Set<Note> getNotes() {
        return notes;
    }

    /**
     * Notes.
     *
     * @param notes the notes
     * @return the appointment
     */
    public Appointment notes(Set<Note> notes) {
        this.notes = notes;
        return this;
    }

    /**
     * Adds the note.
     *
     * @param note the note
     * @return the appointment
     */
    public Appointment addNote(Note note) {
        this.notes.add(note);
        note.setAppointment(this);
        return this;
    }

    /**
     * Removes the note.
     *
     * @param note the note
     * @return the appointment
     */
    public Appointment removeNote(Note note) {
        this.notes.remove(note);
        note.setAppointment(null);
        return this;
    }

    /**
     * Sets the notes.
     *
     * @param notes the new notes
     */
    public void setNotes(Set<Note> notes) {
        this.notes = notes;
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
        Appointment appointment = (Appointment) o;
        if (appointment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appointment.getId());
    }

    /* (non-Javadoc)
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
        StringBuilder builder = new StringBuilder();
        builder.append("Appointment [id=").append(id).append(", name=").append(name)
                .append(", description=").append(description).append(", repeatType=")
                .append(repeatType).append(", customUser=").append(customUser).append(", team=")
                .append(team).append(", time=").append(time).append(", attendees=")
                .append(attendees).append(", notes=").append(notes).append("]");
        return builder.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Appointment o) {
        return this.getTime().compareTo(o.getTime());
    }
}
