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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Team.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "team")
public class Team implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long  serialVersionUID  = 1L;

    /** The Constant SEARCH_FIELD_NAME. */
    public static final String SEARCH_FIELD_NAME = "name";

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long               id;

    /** The name. */
    @Column(name = "name")
    private String             name;

    /** The description. */
    @Column(name = "description")
    private String             description;

    /** The level. */
    @Column(name = "jhi_level")
    private Integer            level;

    /** The total member. */
    @Column(name = "total_member")
    private Integer            totalMember;

    /** The extra group name. */
    @Column(name = "extra_group_name")
    private String             extraGroupName;

    /** The extra group description. */
    @Column(name = "extra_group_description")
    private String             extraGroupDescription;

    /** The extra group total member. */
    @Column(name = "extra_group_total_member")
    private Integer            extraGroupTotalMember;

    /** The groups. */
    @OneToMany(mappedBy = "team")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Groups>        groups            = new HashSet<>();

    /** The appointments. */
    @OneToMany(mappedBy = "team")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Appointment>   appointments      = new HashSet<>();

    /** The owner. */
    @ManyToOne
    private CustomUser         owner;

    /** The icon. */
    @ManyToOne
    private Icon               icon;

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
     * Id.
     *
     * @param id the id
     * @return the team
     */
    public Team id(Long id) {
        this.setId(id);
        return this;
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
     * @return the team
     */
    public Team name(String name) {
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
     * @return the team
     */
    public Team description(String description) {
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
     * Gets the level.
     *
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Level.
     *
     * @param level the level
     * @return the team
     */
    public Team level(Integer level) {
        this.level = level;
        return this;
    }

    /**
     * Sets the level.
     *
     * @param level the new level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * Gets the total member.
     *
     * @return the total member
     */
    public Integer getTotalMember() {
        return totalMember;
    }

    /**
     * Total member.
     *
     * @param totalMember the total member
     * @return the team
     */
    public Team totalMember(Integer totalMember) {
        this.totalMember = totalMember;
        return this;
    }

    /**
     * Sets the total member.
     *
     * @param totalMember the new total member
     */
    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
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
     * @return the team
     */
    public Team extraGroupName(String extraGroupName) {
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
     * @return the team
     */
    public Team extraGroupDescription(String extraGroupDescription) {
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
     * @return the team
     */
    public Team extraGroupTotalMember(Integer extraGroupTotalMember) {
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
     * Gets the groups.
     *
     * @return the groups
     */
    public Set<Groups> getGroups() {
        return groups;
    }

    /**
     * Groups.
     *
     * @param groups the groups
     * @return the team
     */
    public Team groups(Set<Groups> groups) {
        this.groups = groups;
        return this;
    }

    /**
     * Adds the group.
     *
     * @param groups the groups
     * @return the team
     */
    public Team addGroup(Groups groups) {
        this.groups.add(groups);
        groups.setTeam(this);
        return this;
    }

    /**
     * Removes the group.
     *
     * @param groups the groups
     * @return the team
     */
    public Team removeGroup(Groups groups) {
        this.groups.remove(groups);
        groups.setTeam(null);
        return this;
    }

    /**
     * Sets the groups.
     *
     * @param groups the new groups
     */
    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
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
     * @return the team
     */
    public Team appointments(Set<Appointment> appointments) {
        this.appointments = appointments;
        return this;
    }

    /**
     * Adds the appointment.
     *
     * @param appointment the appointment
     * @return the team
     */
    public Team addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setTeam(this);
        return this;
    }

    /**
     * Removes the appointment.
     *
     * @param appointment the appointment
     * @return the team
     */
    public Team removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setTeam(null);
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
     * Gets the owner.
     *
     * @return the owner
     */
    public CustomUser getOwner() {
        return owner;
    }

    /**
     * Owner.
     *
     * @param customUser the custom user
     * @return the team
     */
    public Team owner(CustomUser customUser) {
        this.owner = customUser;
        return this;
    }

    /**
     * Sets the owner.
     *
     * @param customUser the new owner
     */
    public void setOwner(CustomUser customUser) {
        this.owner = customUser;
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Icon.
     *
     * @param icon the icon
     * @return the team
     */
    public Team icon(Icon icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Sets the icon.
     *
     * @param icon the new icon
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
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
        Team team = (Team) o;
        if (team.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), team.getId());
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
        return "Team{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='"
                + getDescription() + "'" + ", level='" + getLevel() + "'" + ", totalMember='"
                + getTotalMember() + "'" + ", extraGroupName='" + getExtraGroupName() + "'"
                + ", extraGroupDescription='" + getExtraGroupDescription() + "'"
                + ", extraGroupTotalMember='" + getExtraGroupTotalMember() + "'" + "}";
    }
}
