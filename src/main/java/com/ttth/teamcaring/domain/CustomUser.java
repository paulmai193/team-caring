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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "push_token")
    private String pushToken;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "customUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Groups> leaders = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GroupsMember> members = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public CustomUser fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickname() {
        return nickname;
    }

    public CustomUser nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPushToken() {
        return pushToken;
    }

    public CustomUser pushToken(String pushToken) {
        this.pushToken = pushToken;
        return this;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
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

    public Set<Groups> getLeaders() {
        return leaders;
    }

    public CustomUser leaders(Set<Groups> groups) {
        this.leaders = groups;
        return this;
    }

    public CustomUser addLeader(Groups groups) {
        this.leaders.add(groups);
        groups.setCustomUser(this);
        return this;
    }

    public CustomUser removeLeader(Groups groups) {
        this.leaders.remove(groups);
        groups.setCustomUser(null);
        return this;
    }

    public void setLeaders(Set<Groups> groups) {
        this.leaders = groups;
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
            ", fullName='" + getFullName() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", pushToken='" + getPushToken() + "'" +
            "}";
    }
}
