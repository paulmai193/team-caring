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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Icon.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "icon")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "icon")
public class Icon implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              id;

    /** The name. */
    @Column(name = "name")
    private String            name;

    /** The teams. */
    @OneToMany(mappedBy = "icon")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    private Set<Team>         teams            = new HashSet<>();

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
     * @param name
     *        the name
     * @return the icon
     */
    public Icon name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the name.
     *
     * @param name
     *        the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the teams.
     *
     * @return the teams
     */
    public Set<Team> getTeams() {
        return teams;
    }

    /**
     * Teams.
     *
     * @param teams
     *        the teams
     * @return the icon
     */
    public Icon teams(Set<Team> teams) {
        this.teams = teams;
        return this;
    }

    /**
     * Adds the team.
     *
     * @param team
     *        the team
     * @return the icon
     */
    public Icon addTeam(Team team) {
        this.teams.add(team);
        team.setIcon(this);
        return this;
    }

    /**
     * Removes the team.
     *
     * @param team
     *        the team
     * @return the icon
     */
    public Icon removeTeam(Team team) {
        this.teams.remove(team);
        team.setIcon(null);
        return this;
    }

    /**
     * Sets the teams.
     *
     * @param teams
     *        the new teams
     */
    public void setTeams(Set<Team> teams) {
        this.teams = teams;
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
        Icon icon = (Icon) o;
        if (icon.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), icon.getId());
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
        return "Icon{" + "id=" + getId() + ", name='" + getName() + "'" + "}";
    }
}
