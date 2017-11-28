package com.ttth.teamcaring.repository;

import com.ttth.teamcaring.domain.CustomUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CustomUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

}
