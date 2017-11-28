package com.ttth.teamcaring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.CustomUser;


/**
 * Spring Data JPA repository for the CustomUser entity.
 *
 * @author Dai Mai
 */
@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
	
	/**
	 * Find one by user.
	 *
	 * @param userId the user id
	 * @return the optional
	 */
	Optional<CustomUser> findOneByUserId(Long userId);
}
