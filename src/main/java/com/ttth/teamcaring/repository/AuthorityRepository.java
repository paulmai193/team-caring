/*
 * 
 */
package com.ttth.teamcaring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ttth.teamcaring.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 *
 * @author Dai Mai
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
