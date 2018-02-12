/*
 * 
 */
package com.ttth.teamcaring.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
 * A Groups.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "groups")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "groups")
public class Groups extends AbstractAuditingEntity implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              id;

    /** The members. */
    @OneToMany(mappedBy = "groups")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GroupsMember> members          = new HashSet<>();

    /** The leader. */
    @ManyToOne
    private CustomUser        leader;

    /** The team. */
    @ManyToOne
    private Team              team;

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
     * @param groupsMembers
     *        the groups members
     * @return the groups
     */
    public Groups members(Set<GroupsMember> groupsMembers) {
        this.members = groupsMembers;
        return this;
    }

    /**
     * Adds the member.
     *
     * @param groupsMember
     *        the groups member
     * @return the groups
     */
    public Groups addMember(GroupsMember groupsMember) {
        this.members.add(groupsMember);
        groupsMember.setGroups(this);
        return this;
    }

    /**
     * Removes the member.
     *
     * @param groupsMember
     *        the groups member
     * @return the groups
     */
    public Groups removeMember(GroupsMember groupsMember) {
        this.members.remove(groupsMember);
        groupsMember.setGroups(null);
        return this;
    }

    /**
     * Sets the members.
     *
     * @param groupsMembers
     *        the new members
     */
    public void setMembers(Set<GroupsMember> groupsMembers) {
        this.members = groupsMembers;
    }

    /**
     * Gets the leader.
     *
     * @return the leader
     */
    public CustomUser getLeader() {
        return leader;
    }

    /**
     * Leader.
     *
     * @param customUser
     *        the custom user
     * @return the groups
     */
    public Groups leader(CustomUser customUser) {
        this.leader = customUser;
        return this;
    }

    /**
     * Sets the leader.
     *
     * @param customUser
     *        the new leader
     */
    public void setLeader(CustomUser customUser) {
        this.leader = customUser;
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
     * @param team
     *        the team
     * @return the groups
     */
    public Groups team(Team team) {
        this.team = team;
        return this;
    }

    /**
     * Sets the team.
     *
     * @param team
     *        the new team
     */
    public void setTeam(Team team) {
        this.team = team;
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
        Groups groups = (Groups) o;
        if (groups.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groups.getId());
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
        return "Groups{" + "id=" + getId() + "}";
    }
}
