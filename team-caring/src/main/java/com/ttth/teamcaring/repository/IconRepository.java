package com.ttth.teamcaring.repository;

import com.ttth.teamcaring.domain.Icon;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Icon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IconRepository extends JpaRepository<Icon, Long> {

}
