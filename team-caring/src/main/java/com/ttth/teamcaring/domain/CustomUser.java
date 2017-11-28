package com.ttth.teamcaring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CustomUser.
 */
@Entity
@Table(name = "custom_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customuser")
public class CustomUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GroupsMember> members = new HashSet<>();

    @ManyToOne
    private Groups groups;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public CustomUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<GroupsMember> getMembers() {
        return members;
    }

    public CustomUser members(Set<GroupsMember> groupsMembers) {
        this.members = groupsMembers;
        return this;
    }

    public CustomUser addMembers(GroupsMember groupsMember) {
        this.members.add(groupsMember);
        groupsMember.getMembers().add(this);
        return this;
    }

    public CustomUser removeMembers(GroupsMember groupsMember) {
        this.members.remove(groupsMember);
        groupsMember.getMembers().remove(this);
        return this;
    }

    public void setMembers(Set<GroupsMember> groupsMembers) {
        this.members = groupsMembers;
    }

    public Groups getGroups() {
        return groups;
    }

    public CustomUser groups(Groups groups) {
        this.groups = groups;
        return this;
    }

    public void setGroups(Groups groups) {
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
        CustomUser customUser = (CustomUser) o;
        if (customUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomUser{" +
            "id=" + getId() +
            "}";
    }
}
