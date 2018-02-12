/*
 * 
 */
package com.ttth.teamcaring.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A CustomUser.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "custom_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customuser")
public class CustomUser implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant SEARCH_FIELD_NICK_NAME. */
    public static final String SEARCH_FIELD_NICK_NAME = "nickname";

    /** The Constant SEARCH_FIELD_FULL_NAME. */
    public static final String SEARCH_FIELD_FULL_NAME = "fullName";

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The full name. */
    @Column(name = "full_name")
    private String fullName;

    /** The nickname. */
    @Column(name = "nickname")
    private String nickname;

    /** The push token. */
    @Column(name = "push_token")
    private String pushToken;

    /** The extra group name. */
    @Column(name = "extra_group_name")
    private String extraGroupName;

    /** The extra group description. */
    @Column(name = "extra_group_description")
    private String extraGroupDescription;

    /** The extra group total member. */
    @Column(name = "extra_group_total_member")
    private Integer extraGroupTotalMember;

    /** The user. */
    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    /** The leaders. */
    @OneToMany(mappedBy = "leader")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Groups> leaders = new HashSet<>();

    /** The owners. */
    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Team> owners = new HashSet<>();

    /** The members. */
    @OneToMany(mappedBy = "customUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GroupsMember> members = new HashSet<>();

    /** The notifications. */
    @OneToMany(mappedBy = "customUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> notifications = new HashSet<>();

    /** The appointments. */
    @OneToMany(mappedBy = "customUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Appointment> appointments = new HashSet<>();

    /** The attendees. */
    @OneToMany(mappedBy = "customUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attendee> attendees = new HashSet<>();

    /** The notes. */
    @OneToMany(mappedBy = "customUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Note> notes = new HashSet<>();

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
     * Id.
     *
     * @param l the l
     * @return the custom user
     */
    public CustomUser id(long l) {
        this.setId(id);
        return this;
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
     * Full name.
     *
     * @param fullName the full name
     * @return the custom user
     */
    public CustomUser fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    /**
     * Sets the full name.
     *
     * @param fullName the new full name
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
     * Nickname.
     *
     * @param nickname the nickname
     * @return the custom user
     */
    public CustomUser nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    /**
     * Sets the nickname.
     *
     * @param nickname the new nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
     * Push token.
     *
     * @param pushToken the push token
     * @return the custom user
     */
    public CustomUser pushToken(String pushToken) {
        this.pushToken = pushToken;
        return this;
    }

    /**
     * Sets the push token.
     *
     * @param pushToken the new push token
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
     * Extra group name.
     *
     * @param extraGroupName the extra group name
     * @return the custom user
     */
    public CustomUser extraGroupName(String extraGroupName) {
        this.extraGroupName = extraGroupName;
        return this;
    }

    /**
     * Sets the extra group name.
     *
     * @param extraGroupName the new extra group name
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
     * Extra group description.
     *
     * @param extraGroupDescription the extra group description
     * @return the custom user
     */
    public CustomUser extraGroupDescription(String extraGroupDescription) {
        this.extraGroupDescription = extraGroupDescription;
        return this;
    }

    /**
     * Sets the extra group description.
     *
     * @param extraGroupDescription the new extra group description
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
     * Extra group total member.
     *
     * @param extraGroupTotalMember the extra group total member
     * @return the custom user
     */
    public CustomUser extraGroupTotalMember(Integer extraGroupTotalMember) {
        this.extraGroupTotalMember = extraGroupTotalMember;
        return this;
    }

    /**
     * Sets the extra group total member.
     *
     * @param extraGroupTotalMember the new extra group total member
     */
    public void setExtraGroupTotalMember(Integer extraGroupTotalMember) {
        this.extraGroupTotalMember = extraGroupTotalMember;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * User.
     *
     * @param user the user
     * @return the custom user
     */
    public CustomUser user(User user) {
        this.user = user;
        return this;
    }

    /**
     * Sets the user.
     *
     * @param user the new user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the leaders.
     *
     * @return the leaders
     */
    public Set<Groups> getLeaders() {
        return leaders;
    }

    /**
     * Leaders.
     *
     * @param groups the groups
     * @return the custom user
     */
    public CustomUser leaders(Set<Groups> groups) {
        this.leaders = groups;
        return this;
    }

    /**
     * Adds the leader.
     *
     * @param groups the groups
     * @return the custom user
     */
    public CustomUser addLeader(Groups groups) {
        this.leaders.add(groups);
        groups.setLeader(this);
        return this;
    }

    /**
     * Removes the leader.
     *
     * @param groups the groups
     * @return the custom user
     */
    public CustomUser removeLeader(Groups groups) {
        this.leaders.remove(groups);
        groups.setLeader(null);
        return this;
    }

    /**
     * Sets the leaders.
     *
     * @param groups the new leaders
     */
    public void setLeaders(Set<Groups> groups) {
        this.leaders = groups;
    }

    /**
     * Gets the owners.
     *
     * @return the owners
     */
    public Set<Team> getOwners() {
        return owners;
    }

    /**
     * Owners.
     *
     * @param teams the teams
     * @return the custom user
     */
    public CustomUser owners(Set<Team> teams) {
        this.owners = teams;
        return this;
    }

    /**
     * Adds the owner.
     *
     * @param team the team
     * @return the custom user
     */
    public CustomUser addOwner(Team team) {
        this.owners.add(team);
        team.setOwner(this);
        return this;
    }

    /**
     * Removes the owner.
     *
     * @param team the team
     * @return the custom user
     */
    public CustomUser removeOwner(Team team) {
        this.owners.remove(team);
        team.setOwner(null);
        return this;
    }

    /**
     * Sets the owners.
     *
     * @param teams the new owners
     */
    public void setOwners(Set<Team> teams) {
        this.owners = teams;
    }

    /**
     * Gets the members.
     *
     * @return the members
     */
    public Set<GroupsMember> getMembers() {
        return members;
    }

    /**
     * Members.
     *
     * @param groupsMembers the groups members
     * @return the custom user
     */
    public CustomUser members(Set<GroupsMember> groupsMembers) {
        this.members = groupsMembers;
        return this;
    }

    /**
     * Adds the member.
     *
     * @param groupsMember the groups member
     * @return the custom user
     */
    public CustomUser addMember(GroupsMember groupsMember) {
        this.members.add(groupsMember);
        groupsMember.setCustomUser(this);
        return this;
    }

    /**
     * Removes the member.
     *
     * @param groupsMember the groups member
     * @return the custom user
     */
    public CustomUser removeMember(GroupsMember groupsMember) {
        this.members.remove(groupsMember);
        groupsMember.setCustomUser(null);
        return this;
    }

    /**
     * Sets the members.
     *
     * @param groupsMembers the new members
     */
    public void setMembers(Set<GroupsMember> groupsMembers) {
        this.members = groupsMembers;
    }

    /**
     * Gets the notifications.
     *
     * @return the notifications
     */
    public Set<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Notifications.
     *
     * @param notifications the notifications
     * @return the custom user
     */
    public CustomUser notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    /**
     * Adds the notification.
     *
     * @param notification the notification
     * @return the custom user
     */
    public CustomUser addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setCustomUser(this);
        return this;
    }

    /**
     * Removes the notification.
     *
     * @param notification the notification
     * @return the custom user
     */
    public CustomUser removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setCustomUser(null);
        return this;
    }

    /**
     * Sets the notifications.
     *
     * @param notifications the new notifications
     */
    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * Gets the appointments.
     *
     * @return the appointments
     */
    public Set<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Appointments.
     *
     * @param appointments the appointments
     * @return the custom user
     */
    public CustomUser appointments(Set<Appointment> appointments) {
        this.appointments = appointments;
        return this;
    }

    /**
     * Adds the appointment.
     *
     * @param appointment the appointment
     * @return the custom user
     */
    public CustomUser addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setCustomUser(this);
        return this;
    }

    /**
     * Removes the appointment.
     *
     * @param appointment the appointment
     * @return the custom user
     */
    public CustomUser removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setCustomUser(null);
        return this;
    }

    /**
     * Sets the appointments.
     *
     * @param appointments the new appointments
     */
    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
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
     * @return the custom user
     */
    public CustomUser attendees(Set<Attendee> attendees) {
        this.attendees = attendees;
        return this;
    }

    /**
     * Adds the attendee.
     *
     * @param attendee the attendee
     * @return the custom user
     */
    public CustomUser addAttendee(Attendee attendee) {
        this.attendees.add(attendee);
        attendee.setCustomUser(this);
        return this;
    }

    /**
     * Removes the attendee.
     *
     * @param attendee the attendee
     * @return the custom user
     */
    public CustomUser removeAttendee(Attendee attendee) {
        this.attendees.remove(attendee);
        attendee.setCustomUser(null);
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
     * @return the custom user
     */
    public CustomUser notes(Set<Note> notes) {
        this.notes = notes;
        return this;
    }

    /**
     * Adds the note.
     *
     * @param note the note
     * @return the custom user
     */
    public CustomUser addNote(Note note) {
        this.notes.add(note);
        note.setCustomUser(this);
        return this;
    }

    /**
     * Removes the note.
     *
     * @param note the note
     * @return the custom user
     */
    public CustomUser removeNote(Note note) {
        this.notes.remove(note);
        note.setCustomUser(null);
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
        CustomUser customUser = (CustomUser) o;
        if (customUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customUser.getId());
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
        return "CustomUser{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", pushToken='" + getPushToken() + "'" +
            ", extraGroupName='" + getExtraGroupName() + "'" +
            ", extraGroupDescription='" + getExtraGroupDescription() + "'" +
            ", extraGroupTotalMember='" + getExtraGroupTotalMember() + "'" +
            "}";
    }
}
