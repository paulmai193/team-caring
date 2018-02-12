/*
 * 
 */
package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.repository.AppointmentRepository;
import com.ttth.teamcaring.repository.search.AppointmentSearchRepository;
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.AppointmentService;
import com.ttth.teamcaring.service.AttendeeService;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.GroupsMemberService;
import com.ttth.teamcaring.service.NoteService;
import com.ttth.teamcaring.service.TeamService;
import com.ttth.teamcaring.service.dto.AnwserResponseStatus;
import com.ttth.teamcaring.service.dto.AppointmentDTO;
import com.ttth.teamcaring.service.dto.AppointmentRepeatType;
import com.ttth.teamcaring.service.dto.CreateAppointmentDTO;
import com.ttth.teamcaring.service.dto.DetailAppointmentDTO;
import com.ttth.teamcaring.service.dto.GroupOfAppointmentDTO;
import com.ttth.teamcaring.service.dto.TeamDTO;
import com.ttth.teamcaring.service.dto.UpdateAppointmentDTO;
import com.ttth.teamcaring.service.mapper.AppointmentMapper;
import com.ttth.teamcaring.service.mapper.TimeMapper;
import com.ttth.teamcaring.web.rest.AppointmentResource;
import com.ttth.teamcaring.web.rest.AttendeeResource;
import com.ttth.teamcaring.web.rest.TeamResource;
import com.ttth.teamcaring.web.rest.UserResource;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.errors.ForbiddenException;
import com.ttth.teamcaring.web.rest.errors.InternalServerErrorException;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

/**
 * Service Implementation for managing Appointment.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    /** The log. */
    private final Logger                      log = LoggerFactory
            .getLogger(AppointmentServiceImpl.class);

    /** The appointment repository. */
    private final AppointmentRepository       appointmentRepository;

    /** The appointment mapper. */
    private final AppointmentMapper           appointmentMapper;

    /** The appointment search repository. */
    private final AppointmentSearchRepository appointmentSearchRepository;

    /** The custom user service. */
    @Inject
    private CustomUserService                 customUserService;

    /** The attendee service. */
    @Inject
    private AttendeeService                   attendeeService;

    /** The time mapper. */
    @Inject
    private TimeMapper                        timeMapper;

    /** The team service. */
    @Inject
    private TeamService                       teamService;

    /** The note service. */
    @Inject
    private NoteService                       noteService;

    /** The groups member service. */
    @Inject
    private GroupsMemberService               groupsMemberService;

    /**
     * Instantiates a new appointment service impl.
     *
     * @param appointmentRepository the appointment repository
     * @param appointmentMapper the appointment mapper
     * @param appointmentSearchRepository the appointment search repository
     */
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
            AppointmentMapper appointmentMapper,
            AppointmentSearchRepository appointmentSearchRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.appointmentSearchRepository = appointmentSearchRepository;
    }

    /**
     * Save a appointment.
     *
     * @param appointmentDTO
     *        the entity to save
     * @return the persisted entity
     */
    @Override
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        log.debug("Request to save Appointment : {}", appointmentDTO);
        Appointment appointment = this.saveEntity(appointmentDTO);
        AppointmentDTO result = appointmentMapper.toDto(appointment);
        appointmentSearchRepository.save(appointment);
        return result;
    }

    /**
     * Save entity.
     *
     * @param appointmentDTO the appointment DTO
     * @return the appointment
     */
    private Appointment saveEntity(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        return appointmentRepository.save(appointment);
    }

    /**
     * Get all the appointments.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appointments");
        return appointmentRepository.findAll(pageable).map(appointmentMapper::toDto);
    }

    /**
     * Get one appointment by id.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO findOne(Long id) {
        return appointmentMapper.toDto(this.findOneEntity(id));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AppointmentService#getOneEntity(java.lang.Long)
     */
    @Override
    public Appointment getOneEntity(Long appointmentId) throws BadRequestAlertException {
        Appointment appointment = this.findOneEntity(appointmentId);
        if (Objects.isNull(appointment))
            throw new BadRequestAlertException(
                    MessageFormat.format("Cannot get appointment by ID {0}", appointmentId),
                    AppointmentResource.ENTITY_NAME, "appointmentNotExists");
        return appointment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AppointmentService#findOneEntity(java.lang.Long)
     */
    @Override
    public Appointment findOneEntity(Long appointmentId) {
        log.debug("Request to get Appointment : {}", appointmentId);
        return appointmentRepository.findOne(appointmentId);
    }

    /**
     * Delete the appointment by id.
     *
     * @param id
     *        the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Appointment : {}", id);
        appointmentRepository.delete(id);
        appointmentSearchRepository.delete(id);
    }

    /**
     * Search for the appointment corresponding to the query.
     *
     * @param query
     *        the query of the search
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Appointments for query {}", query);
        Page<Appointment> result = appointmentSearchRepository.search(queryStringQuery(query),
                pageable);
        return result.map(appointmentMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AppointmentService#create(com.ttth.teamcaring.service.dto.
     * CreateAppointmentDTO)
     */
    @Override
    public UpdateAppointmentDTO create(CreateAppointmentDTO createAppointmentDTO) {
        String login = SecurityUtils.getCurrentUserLogin();

        // Some security check
        CustomUser creatorCustomUser = this.customUserService.findOneEntityByUserLogin(login);
        if (Objects.isNull(creatorCustomUser)) {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot create appointment, user {0} not exist", login));
        }
        CustomUser attendeeCustomUser = this.customUserService
                .findOneEntityByUserId(createAppointmentDTO.getUserId());
        if (Objects.isNull(attendeeCustomUser)) {
            throw new BadRequestAlertException(
                    MessageFormat.format("Cannot create appointment, attendee {0} not exist",
                            createAppointmentDTO.getUserId()),
                    UserResource.ENTITY_NAME, "userNotExists");
        }
        TeamDTO team = this.teamService.findOne(createAppointmentDTO.getTeamId());
        if (Objects.isNull(team)) {
            throw new BadRequestAlertException(
                    MessageFormat.format("Cannot create appointment, team {0} not exist",
                            createAppointmentDTO.getTeamId()),
                    TeamResource.ENTITY_NAME, "teamNotExists");
        }
        // Check if this user sent request create appointment this attendee in
        // this day :D
        Optional<Appointment> optApp = creatorCustomUser.getAppointments().stream().filter(app -> {
            String a = this.timeMapper.mapInstantToString(app.getTime());
            String b = createAppointmentDTO.getCreatedDate();
            if (a.contains(b)) {
                return app.getAttendees().stream()
                        .filter(att -> att.getCustomUser().equals(attendeeCustomUser)).findAny()
                        .isPresent();
            }
            else {
                return false;
            }
        }).findAny();

        UpdateAppointmentDTO resultDTO = createAppointmentDTO.castToUpdateAppointmentDTO();

        // Create appointment entity if non exist
        if (!optApp.isPresent()) {
            AppointmentDTO appointmentDTO = createAppointmentDTO
                    .castToAppointmentDTO(creatorCustomUser);
            Appointment appointment = this.saveEntity(appointmentDTO);

            // Create attendee entity
            this.attendeeService.create(appointment, attendeeCustomUser);

            resultDTO.setId(appointment.getId());
        }
        else {
            resultDTO.setId(optApp.get().getId());
        }

        return resultDTO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AppointmentService#findAllByLogin(java.lang.String,
     * java.time.Instant, java.time.Instant)
     */
    @Override
    public List<DetailAppointmentDTO> findAllByLogin(String login, Instant fromDate,
            Instant toDate) {
        CustomUser customUser = this.customUserService.getOneEntityByUserLogin(login);
        List<DetailAppointmentDTO> results = new ArrayList<>();

        // Get all appointments this user create
        results.addAll(this.mapToListDetails(this.appointmentRepository
                .findByCustomUserAndTimeBetween(customUser, fromDate, toDate), customUser));

        // Get all appointments this user is attendee
        results.addAll(this.mapToListDetails(this.appointmentRepository
                .findByAttendeeAndTimeBetween(customUser, fromDate, toDate), customUser));

        Collections.sort(results, new Comparator<UpdateAppointmentDTO>() {

            @Override
            public int compare(UpdateAppointmentDTO o1, UpdateAppointmentDTO o2) {
                return o1.getCreatedDate().compareTo(o2.getCreatedDate());
            }
        });

        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AppointmentService#findOfMemberInTeam(java.lang.String,
     * java.lang.Long, java.lang.Long, org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<GroupOfAppointmentDTO> findOfMemberInTeam(String currentUserLogin, Long userId,
            Long teamId, Pageable pageable) {
        CustomUser leaderCustomUser = this.customUserService
                .getOneEntityByUserLogin(currentUserLogin);

        // Paging
        return PaginationUtil
                .pagingList(this.findAllOfMemberInTeam(leaderCustomUser, userId, teamId), pageable);
    }

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.AppointmentService#findAllOfMemberInTeam(com.ttth.teamcaring.domain.CustomUser, java.lang.Long, java.lang.Long)
     */
    @Override
    public Set<GroupOfAppointmentDTO> findAllOfMemberInTeam(CustomUser leaderCustomUser,
            Long userId, Long teamId) {
        GroupsMember groupsMember = this.groupsMemberService.getOneEntityByMemberAndTeam(userId,
                teamId);
        if (!groupsMember.getGroups().getLeader().equals(leaderCustomUser)
                && !groupsMember.getGroups().getTeam().getOwner().equals(leaderCustomUser))
            throw new ForbiddenException(
                    MessageFormat.format("User {0} not leader of user {1} in team {2}",
                            leaderCustomUser.getId(), userId, teamId));

        Map<Long, GroupOfAppointmentDTO> mapOfGroup = new HashMap<>(); // Key is custom user ID

        // When is creator
        mapOfGroup = this.mapToGroupOfAppointment(groupsMember.getCustomUser(), true,
                groupsMember.getCustomUser().getAppointments(), mapOfGroup);

        // When is attendee
        mapOfGroup = this.mapToGroupOfAppointment(
                groupsMember.getCustomUser(), false, groupsMember.getCustomUser().getAttendees()
                        .stream().map(att -> att.getAppointment()).collect(Collectors.toSet()),
                mapOfGroup);

        return new TreeSet<>(mapOfGroup.values());
    }

    /**
     * Map to group of appointment. Make sure the appointment was accepted
     *
     * @param viewerCustomUser the viewer custom user
     * @param isCreator the is creator
     * @param appointments the appointments
     * @param tmpMapOfGroup the tmp map of group
     * @return the map
     */
    private Map<Long, GroupOfAppointmentDTO> mapToGroupOfAppointment(CustomUser viewerCustomUser,
            boolean isCreator, Collection<Appointment> appointments,
            Map<Long, GroupOfAppointmentDTO> tmpMapOfGroup) {
        Map<Long, GroupOfAppointmentDTO> mapOfGroup = new HashMap<>(); // Key is custom user ID
        appointments.stream().forEach(app -> {
            Attendee att = app.getAttendees().iterator().next();
            if (att.getStatus().equals(Attendee.STATUS_ACCEPTED)) {
                CustomUser groupCustomUser;
                if (isCreator) {
                    // Mean viewer is creator, group custom user is attendee
                    groupCustomUser = att.getCustomUser();
                }
                else {
                    // Mean viewer is attendee, group custom user is creator
                    groupCustomUser = app.getCustomUser();
                }
                GroupOfAppointmentDTO groupApp = tmpMapOfGroup.getOrDefault(groupCustomUser.getId(),
                        new GroupOfAppointmentDTO(
                                this.customUserService.mapFromEntity(groupCustomUser)));
                groupApp.addList(this.mapToDetail(app, viewerCustomUser));
                mapOfGroup.put(groupCustomUser.getId(), groupApp);
            }
        });
        return mapOfGroup;
    }

    /**
     * Map to list details. Make sure the appointment was accepted
     *
     * @param appointments the appointments
     * @param viewerCustomUser the viewer custom user
     * @return the list
     */
    private List<DetailAppointmentDTO> mapToListDetails(List<Appointment> appointments,
            CustomUser viewerCustomUser) {
        return appointments.stream().filter(app -> app.getAttendees().iterator().next().getStatus()
                .equals(Attendee.STATUS_ACCEPTED)).map(app -> {
                    return this.mapToDetail(app, viewerCustomUser);
                }).collect(Collectors.toList());
    }

    /**
     * Map to detail.
     *
     * @param appointment the appointment
     * @param viewerCustomUser the viewer custom user
     * @return the detail appointment DTO
     */
    private DetailAppointmentDTO mapToDetail(Appointment appointment, CustomUser viewerCustomUser) {
        DetailAppointmentDTO dto = new DetailAppointmentDTO();
        dto.setDescription(appointment.getDescription());
        dto.setId(appointment.getId());
        dto.setCreatedDate(this.timeMapper.mapInstantToString(appointment.getTime()));
        dto.setName(appointment.getName());
        dto.setRepeatType(AppointmentRepeatType.getTypeByValue(appointment.getRepeatType()));
        dto.setUserId(appointment.getCustomUser().getUser().getId());
        dto.setTeamId(appointment.getTeam().getId());
        dto.setTeam(appointment.getTeam().getName());
        dto.setNotes(this.noteService.mapToDtoBaseOnCustomUser(appointment.getNotes(),
                viewerCustomUser));

        // Set info base on viewer customUser
        if (this.validateCreator(appointment, viewerCustomUser)) {
            // Viewer is creator, get attendee info
            if (appointment.getAttendees().size() > 0) {
                Attendee attendee = appointment.getAttendees().iterator().next();
                CustomUser attendeeCustomUser = attendee.getCustomUser();
                dto.setImageUrl(attendeeCustomUser.getUser().getImageUrl());
                dto.setMember(attendeeCustomUser.getNickname());
                dto.setStatus(attendee.getStatus());
            }
        }
        else if (this.validateAttendee(appointment, viewerCustomUser)) {
            // Viewer is attendee, get creator info
            dto.setImageUrl(appointment.getCustomUser().getUser().getImageUrl());
            dto.setMember(appointment.getCustomUser().getNickname());
            dto.setStatus(viewerCustomUser.getAttendees().stream()
                    .filter(att -> att.getAppointment().equals(appointment)).findAny().get()
                    .getStatus());
        }

        return dto;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AppointmentService#response(java.lang.String,
     * java.lang.Long, com.ttth.teamcaring.service.dto.AnwserResponseStatus)
     */
    @Override
    public void response(String currentUserLogin, Long attendeeId, AnwserResponseStatus response) {
        CustomUser responseCustomUser = this.customUserService
                .getOneEntityByUserLogin(currentUserLogin);

        // Some security check
        Attendee requestAttendee = this.attendeeService.getOneEntity(attendeeId);
        if (!requestAttendee.getCustomUser().equals(responseCustomUser)) {
            throw new BadRequestAlertException(
                    MessageFormat.format("Cannot response appointment, user ID {0} not attendee",
                            responseCustomUser.getUser().getId()),
                    AttendeeResource.ENTITY_NAME, "notAttendee");
        }

        // Process response
        if (requestAttendee.getStatus().equals(Attendee.STATUS_PENDING)) {
            // Update response
            switch (response) {
                case accept:
                    this.attendeeService.accept(requestAttendee);

                    // Delete all pending request
                    this.attendeeService.removeAllPendingRequest(
                            requestAttendee.getCustomUser().getId(),
                            requestAttendee.getAppointment().getId());
                    break;

                case reject:
                    this.attendeeService.reject(requestAttendee);

                    // Delete relate appointment
                    this.delete(requestAttendee.getAppointment().getId());
                    break;

                default:
                    throw new BadRequestAlertException(
                            MessageFormat.format(
                                    "Cannot response appointment, status {0} not valid",
                                    responseCustomUser.getUser().getId()),
                            AttendeeResource.ENTITY_NAME, "wrongStatus");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.AppointmentService#validateCreator(com.ttth.teamcaring.domain.
     * Appointment, com.ttth.teamcaring.domain.CustomUser)
     */
    @Override
    @Transactional(readOnly = true)
    public boolean validateCreator(Appointment appointment, CustomUser creatorCustomUser) {
        return appointment.getCustomUser().equals(creatorCustomUser);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.AppointmentService#validateAttendee(com.ttth.teamcaring.domain.
     * Appointment, com.ttth.teamcaring.domain.CustomUser)
     */
    @Override
    @Transactional(readOnly = true)
    public boolean validateAttendee(Appointment appointment, CustomUser attendeeCustomUser) {
        Set<Attendee> tmpSet = new HashSet<>(appointment.getAttendees());
        tmpSet.retainAll(attendeeCustomUser.getAttendees());
        return tmpSet.size() > 0;
    }

}
