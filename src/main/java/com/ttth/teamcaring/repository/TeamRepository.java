/*
 * 
 */
package com.ttth.teamcaring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.Team;

/**
 * Spring Data JPA repository for the Team entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
