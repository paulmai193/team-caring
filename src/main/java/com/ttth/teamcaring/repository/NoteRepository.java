/*
 * 
 */
package com.ttth.teamcaring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.Note;

/**
 * Spring Data JPA repository for the Note entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}
