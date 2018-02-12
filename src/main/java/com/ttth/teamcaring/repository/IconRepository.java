/*
 * 
 */
package com.ttth.teamcaring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.Icon;

/**
 * Spring Data JPA repository for the Icon entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
@Repository
public interface IconRepository extends JpaRepository<Icon, Long> {

}
