/*
 * 
 */
package com.ttth.teamcaring.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.Note;
import com.ttth.teamcaring.service.dto.CreateNoteDTO;
import com.ttth.teamcaring.service.dto.NoteDTO;

/**
 * Service Interface for managing Note.
 *
 * @author Dai Mai
 */
public interface NoteService {

    /**
     * Save a note.
     *
     * @param noteDTO
     *        the entity to save
     * @return the persisted entity
     */
    NoteDTO save(NoteDTO noteDTO);

    /**
     * Get all the notes.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<NoteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" note.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    NoteDTO findOne(Long id);
    
    /**
     * Find one entity.
     *
     * @param id the id
     * @return the note
     */
    Note findOneEntity(Long id);
    
    /**
     * Gets the one entity.
     *
     * @param id the id
     * @return the one entity
     */
    Note getOneEntity(Long id);

    /**
     * Delete the "id" note.
     *
     * @param id
     *        the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the note corresponding to the query.
     *
     * @param query
     *        the query of the search
     * 
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<NoteDTO> search(String query, Pageable pageable);

    /**
     * Creates the note for login user.
     *
     * @param login the login
     * @param createNoteDTO the create note DTO
     * @return the note DTO
     */
    NoteDTO create(String login, CreateNoteDTO createNoteDTO);

    /**
     * Update the note for login user.
     *
     * @param login the login
     * @param noteDTO the note DTO
     * @return the note DTO
     */
    @Deprecated
    NoteDTO update(String login, NoteDTO noteDTO);

    /**
     * Map to dto.
     *
     * @param listNotes the list notes
     * @return the list
     */
    List<NoteDTO> mapToDto(List<Note> listNotes);
    
    /**
     * Map to dto.
     *
     * @param note the note
     * @return the note DTO
     */
    NoteDTO mapToDto(Note note);
    
    /**
     * Map to dto base on custom user.
     *
     * @param listNotes the list notes
     * @param customUser the custom user
     * @return the list
     */
    List<NoteDTO> mapToDtoBaseOnCustomUser(Collection<Note> listNotes, CustomUser customUser);
}
