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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Groups.
 */
@Entity
@Table(name = "groups")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "groups")
public class Groups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "offical")
    private Boolean offical;

    @Column(name = "total_member")
    private Integer totalMember;

    @ManyToOne
    private CustomUser customUser;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "groups_members",
               joinColumns = @JoinColumn(name="groups_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="members_id", referencedColumnName="id"))
    private Set<GroupsMember> members = new HashSet<>();

    @ManyToOne
    private Team team;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Groups description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isOffical() {
        return offical;
    }

    public Groups offical(Boolean offical) {
        this.offical = offical;
        return this;
    }

    public void setOffical(Boolean offical) {
        this.offical = offical;
    }

    public Integer getTotalMember() {
        return totalMember;
    }

    public Groups totalMember(Integer totalMember) {
        this.totalMember = totalMember;
        return this;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    public CustomUser getCustomUser() {
        return customUser;
    }

    public Groups customUser(CustomUser customUser) {
        this.customUser = customUser;
        return this;
    }

    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
    }

    public Set<GroupsMember> getMembers() {
        return members;
    }

    public Groups members(Set<GroupsMember> groupsMembers) {
        this.members = groupsMembers;
        return this;
    }

    public Groups addMembers(GroupsMember groupsMember) {
        this.members.add(groupsMember);
        groupsMember.getGroups().add(this);
        return this;
    }

    public Groups removeMembers(GroupsMember groupsMember) {
        this.members.remove(groupsMember);
        groupsMember.getGroups().remove(this);
        return this;
    }

    public void setMembers(Set<GroupsMember> groupsMembers) {
        this.members = groupsMembers;
    }

    public Team getTeam() {
        return team;
    }

    public Groups team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Groups{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", offical='" + isOffical() + "'" +
            ", totalMember='" + getTotalMember() + "'" +
            "}";
    }
}