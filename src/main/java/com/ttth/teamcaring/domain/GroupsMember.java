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
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A GroupsMember.
 */
@Entity
@Table(name = "groups_member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "groupsmember")
public class GroupsMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_level")
    private Integer level;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "groups_member_members",
               joinColumns = @JoinColumn(name="groups_members_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="members_id", referencedColumnName="id"))
    private Set<CustomUser> members = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Groups> groups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public GroupsMember level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Set<CustomUser> getMembers() {
        return members;
    }

    public GroupsMember members(Set<CustomUser> customUsers) {
        this.members = customUsers;
        return this;
    }

    public GroupsMember addMembers(CustomUser customUser) {
        this.members.add(customUser);
        customUser.getMembers().add(this);
        return this;
    }

    public GroupsMember removeMembers(CustomUser customUser) {
        this.members.remove(customUser);
        customUser.getMembers().remove(this);
        return this;
    }

    public void setMembers(Set<CustomUser> customUsers) {
        this.members = customUsers;
    }

    public Set<Groups> getGroups() {
        return groups;
    }

    public GroupsMember groups(Set<Groups> groups) {
        this.groups = groups;
        return this;
    }

    public GroupsMember addGroups(Groups groups) {
        this.groups.add(groups);
        groups.getMembers().add(this);
        return this;
    }

    public GroupsMember removeGroups(Groups groups) {
        this.groups.remove(groups);
        groups.getMembers().remove(this);
        return this;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
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
        GroupsMember groupsMember = (GroupsMember) o;
        if (groupsMember.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupsMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroupsMember{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
