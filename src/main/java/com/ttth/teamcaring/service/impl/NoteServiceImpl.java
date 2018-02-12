/*
 * 
 */
package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.Note;
import com.ttth.teamcaring.repository.NoteRepository;
import com.ttth.teamcaring.repository.search.NoteSearchRepository;
import com.ttth.teamcaring.service.AppointmentService;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.NoteService;
import com.ttth.teamcaring.service.dto.CreateNoteDTO;
import com.ttth.teamcaring.service.dto.NoteDTO;
import com.ttth.teamcaring.service.mapper.NoteMapper;
import com.ttth.teamcaring.web.rest.NoteResource;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.errors.ForbiddenException;

/**
 * Service Implementation for managing Note.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    /** The log. */
    private final Logger               log = LoggerFactory.getLogger(NoteServiceImpl.class);

    /** The note repository. */
    private final NoteRepository       noteRepository;

    /** The note mapper. */
    private final NoteMapper           noteMapper;

    /** The note search repository. */
    private final NoteSearchRepository noteSearchRepository;

    /** The custom user service. */
    @Inject
    private CustomUserService          customUserService;

    /** The appointment service. */
    @Inject
    private AppointmentService         appointmentService;

    /**
     * Instantiates a new note service impl.
     *
     * @param noteRepository the note repository
     * @param noteMapper the note mapper
     * @param noteSearchRepository the note search repository
     */
    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper,
            NoteSearchRepository noteSearchRepository) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.noteSearchRepository = noteSearchRepository;
    }

    /**
     * Save a note.
     *
     * @param noteDTO
     *        the entity to save
     * @return the persisted entity
     */
    @Override
    public NoteDTO save(NoteDTO noteDTO) {
        log.debug("Request to save Note : {}", noteDTO);
        Note note = noteMapper.toEntity(noteDTO);
        note = noteRepository.save(note);
        NoteDTO result = noteMapper.toDto(note);
        noteSearchRepository.save(note);
        return result;
    }

    /**
     * Get all the notes.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notes");
        return noteRepository.findAll(pageable).map(noteMapper::toDto);
    }

    /**
     * Get one note by id.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NoteDTO findOne(Long id) {
        return noteMapper.toDto(this.findOneEntity(id));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.NoteService#findOneEntity(java.lang.Long)
     */
    @Override
    public Note findOneEntity(Long id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findOne(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.NoteService#getOneEntity(java.lang.Long)
     */
    @Override
    public Note getOneEntity(Long id) {
        Note note = this.findOneEntity(id);
        if (Objects.isNull(note))
            throw new BadRequestAlertException(
                    MessageFormat.format("Cannot find note with ID {0}", id),
                    NoteResource.ENTITY_NAME, "noteNotExist");
        return note;
    }

    /**
     * Delete the note by id.
     *
     * @param id
     *        the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.delete(id);
        noteSearchRepository.delete(id);
    }

    /**
     * Search for the note corresponding to the query.
     *
     * @param query
     *        the query of the search
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notes for query {}", query);
        Page<Note> result = noteSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(noteMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.NoteService#create(java.lang.String,
     * com.ttth.teamcaring.service.dto.CreateNoteDTO)
     */
    @Override
    public NoteDTO create(String login, CreateNoteDTO createNoteDTO) {
        CustomUser customUser = this.customUserService.getOneEntityByUserLogin(login);
        Appointment appointment = this.appointmentService
                .getOneEntity(createNoteDTO.getAppointmentId());
        if (!this.appointmentService.validateCreator(appointment, customUser)
                && !this.appointmentService.validateAttendee(appointment, customUser))
            throw new ForbiddenException(MessageFormat.format(
                    "Cannot create note for appointment {0}, user {1} not the joined",
                    NoteResource.ENTITY_NAME, "userNotJoinedAppointment"));

        createNoteDTO.setCustomUserId(customUser.getId());
        return this.save(createNoteDTO.castToNoteDTO());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.NoteService#update(java.lang.String,
     * com.ttth.teamcaring.service.dto.NoteDTO)
     */
    @Override
    @Deprecated
    public NoteDTO update(String login, NoteDTO noteDTO) {
//        CustomUser customUser = this.customUserService.getOneEntityByUserLogin(login);
//        Appointment appointment = this.appointmentService.getOneEntity(noteDTO.getAppointmentId());
//        if (!this.appointmentService.validateCreator(appointment, customUser)
//                && !this.appointmentService.validateAttendee(appointment, customUser))
//            throw new ForbiddenException(MessageFormat.format(
//                    "Cannot update note for appointment {0}, user {1} not the joined",
//                    NoteResource.ENTITY_NAME, "userNotJoinedAppointment"));
//
//        NoteDTO existNoteDTO = this.findOne(noteDTO.getId());
//
//        // Validate this note must exist
//        if (Objects.isNull(existNoteDTO))
//            throw new BadRequestAlertException(
//                    MessageFormat.format("Cannot update, note ID {0} not exist", noteDTO.getId()),
//                    NoteResource.ENTITY_NAME, "noteNotExist");
//        
//        // Validate creator of this note
//        if (!existNoteDTO.getCustomUserId().equals(customUser.getId()))
//        return this.save(noteDTO);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.NoteService#mapToDto(java.util.List)
     */
    @Override
    public List<NoteDTO> mapToDto(List<Note> listNotes) {
        return this.noteMapper.toDto(listNotes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.NoteService#mapToDto(com.ttth.teamcaring.domain.Note)
     */
    @Override
    public NoteDTO mapToDto(Note note) {
        return this.noteMapper.toDto(note);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.NoteService#mapToDtoBaseOnCustomUser(java.util.List,
     * com.ttth.teamcaring.domain.CustomUser)
     */
    @Override
    public List<NoteDTO> mapToDtoBaseOnCustomUser(Collection<Note> listNotes,
            CustomUser customUser) {
        return listNotes.stream().map(tmpNote -> {
            Note note = this.getOneEntity(tmpNote.getId());
            NoteDTO noteDTO = this.mapToDto(note);
            Appointment appointment = note.getAppointment();
            if (!this.validateCreator(note, customUser))
                noteDTO.setReminder("");
            if (!this.appointmentService.validateCreator(appointment, customUser)
                    && !this.appointmentService.validateAttendee(appointment, customUser))
                noteDTO.setSeparate("");

            return noteDTO;
        }).collect(Collectors.toList());
    }

    /**
     * Validate creator.
     *
     * @param note the note
     * @param creatorCustomUser the creator custom user
     * @return true, if successful
     */
    private boolean validateCreator(Note note, CustomUser creatorCustomUser) {
        return note.getCustomUser().equals(creatorCustomUser);
    }

}
