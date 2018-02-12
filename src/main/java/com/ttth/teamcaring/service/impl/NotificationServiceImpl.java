/*
 * 
 */
package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.client.SendPushNotificationClient;
import com.ttth.teamcaring.config.ApplicationProperties;
import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.domain.Notification;
import com.ttth.teamcaring.domain.Team;
import com.ttth.teamcaring.repository.NotificationRepository;
import com.ttth.teamcaring.repository.search.NotificationSearchRepository;
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.NotificationService;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;
import com.ttth.teamcaring.service.dto.NotificationDTO;
import com.ttth.teamcaring.service.dto.push.BasePayloadDataDTO;
import com.ttth.teamcaring.service.dto.push.FcmResponseDTO;
import com.ttth.teamcaring.service.dto.push.ImplBasePayloadDataDTO;
import com.ttth.teamcaring.service.dto.push.PayloadDTO;
import com.ttth.teamcaring.service.dto.push.PayloadNotificationDTO;
import com.ttth.teamcaring.service.mapper.NotificationMapper;
import com.ttth.teamcaring.service.mapper.TimeMapper;
import com.ttth.teamcaring.web.rest.errors.InternalServerErrorException;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

/**
 * Service Implementation for managing Notification.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    /** The log. */
    private final Logger                       log = LoggerFactory
            .getLogger(NotificationServiceImpl.class);

    /** The notification repository. */
    private final NotificationRepository       notificationRepository;

    /** The notification mapper. */
    private final NotificationMapper           notificationMapper;

    /** The notification search repository. */
    private final NotificationSearchRepository notificationSearchRepository;

    /** The custom user service. */
    @Inject
    private CustomUserService                  customUserService;

    /** The message source. */
    @Inject
    private MessageSource                      messageSource;

    /** The application properties. */
    @Inject
    private ApplicationProperties              applicationProperties;

    /** The push notification client. */
    @Inject
    private SendPushNotificationClient         pushNotificationClient;

    /** The time mapper. */
    @Inject
    private TimeMapper                         timeMapper;

    /**
     * Instantiates a new notification service impl.
     *
     * @param notificationRepository
     *        the notification repository
     * @param notificationMapper
     *        the notification mapper
     * @param notificationSearchRepository
     *        the notification search repository
     */
    public NotificationServiceImpl(NotificationRepository notificationRepository,
            NotificationMapper notificationMapper,
            NotificationSearchRepository notificationSearchRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.notificationSearchRepository = notificationSearchRepository;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO
     *        the entity to save
     * @return the persisted entity
     */
    @Override
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        NotificationDTO result = notificationMapper.toDto(notification);
        notificationSearchRepository.save(notification);
        return result;
    }

    /**
     * Get all the notifications.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable).map(notificationMapper::toDto);
    }

    /**
     * Get one notification by id.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NotificationDTO findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        Notification notification = notificationRepository.findOne(id);
        return notificationMapper.toDto(notification);
    }

    /**
     * Delete the notification by id.
     *
     * @param id
     *        the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.delete(id);
        notificationSearchRepository.delete(id);
    }

    /**
     * Search for the notification corresponding to the query.
     *
     * @param query
     *        the query of the search
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notifications for query {}", query);
        Page<Notification> result = notificationSearchRepository.search(queryStringQuery(query),
                pageable);
        return result.map(notificationMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.NotificationService#send(com.ttth.teamcaring.
     * service.dto.NotificationDTO, java.lang.String)
     */
    @Override
    public FcmResponseDTO send(NotificationDTO notificationDTO, String registrationId) {
        // Build payload
        PayloadNotificationDTO payloadNotificationDTO = new PayloadNotificationDTO(
                notificationDTO.getTitle(), notificationDTO.getMessage(),
                this.applicationProperties.getFcm().getIcon());
        BasePayloadDataDTO payloadDataDTO = new ImplBasePayloadDataDTO(notificationDTO.getType(),
                notificationDTO.getTargetId());
        PayloadDTO payload = new PayloadDTO().notification(payloadNotificationDTO)
                .data(payloadDataDTO).to(registrationId);
        this.log.debug("Push notification payload: {}", payload);

        // Call client to send
        String keyToken = "key=" + this.applicationProperties.getFcm().getKey();
        FcmResponseDTO resultPush = this.pushNotificationClient.send(keyToken, payload);

        this.log.debug("Result send push: {}", resultPush);
        return resultPush;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.NotificationService#sendRequestJoinTeam(com.
     * ttth.teamcaring.domain.CustomUser, com.ttth.teamcaring.domain.CustomUser,
     * com.ttth.teamcaring.domain.Team,
     * com.ttth.teamcaring.service.dto.GroupsMemberDTO)
     */
    @Override
    @Async
    public void sendRequestJoinTeam(CustomUser customUser, CustomUser leaderCustomUser, Team team,
            GroupsMemberDTO groupsMemberDTO) {
        Object[] args = { customUser.getNickname(), team.getName() };
        Notification notification = this.notificationRepository
                .findOneByCustomUserAndTypeAndTargetId(leaderCustomUser,
                        Notification.TYPE_REQUEST_TEAM, groupsMemberDTO.getId());
        NotificationDTO requestNotiDTO;
        if (Objects.isNull(notification)) {
            requestNotiDTO = new NotificationDTO().customUserId(leaderCustomUser.getId())
                    .type(Notification.TYPE_REQUEST_TEAM)
                    .title(this.messageSource.getMessage("noti.title.team.join.request", null,
                            null))
                    .message(this.messageSource.getMessage("noti.message.team.join.request", args,
                            null))
                    .targetId(groupsMemberDTO.getId());
            this.save(requestNotiDTO);
        }
        else {
            requestNotiDTO = this.notificationMapper.toDto(notification);
        }
        this.send(requestNotiDTO, leaderCustomUser.getPushToken());
    }

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.NotificationService#sendRequestAppointment(com.ttth.teamcaring.domain.Attendee)
     */
    @Override
    @Async
    public void sendRequestAppointment(Attendee attendee) {
        CustomUser creatorCustomUser = this.customUserService.findOneEntity(attendee.getAppointment().getCustomUser().getId());
        CustomUser attendeeCustomUser = attendee.getCustomUser();
        Appointment appointment = attendee.getAppointment();
        DateTimeFormatter dateTimeFormatter = this.timeMapper
                .getDateTimeFormatter("HH:mm dd/MM/yyyy", TimeMapper.defaultTimezone);
        Object[] args = { creatorCustomUser.getNickname(), appointment.getName(), dateTimeFormatter.format(appointment.getTime()) };
        Notification notification = this.notificationRepository
                .findOneByCustomUserAndTypeAndTargetId(attendeeCustomUser,
                        Notification.TYPE_REQUEST_APPOINTMENT, attendee.getId());
        NotificationDTO requestNotiDTO;
        if (Objects.isNull(notification)) {
            requestNotiDTO = new NotificationDTO().customUserId(attendeeCustomUser.getId())
                    .type(Notification.TYPE_REQUEST_APPOINTMENT)
                    .title(this.messageSource.getMessage("noti.title.appointment.request", null,
                            null))
                    .message(this.messageSource.getMessage("noti.message.appointment.request", args,
                            null))
                    .targetId(attendee.getId());
            this.save(requestNotiDTO);
        }
        else {
            requestNotiDTO = this.notificationMapper.toDto(notification);
        }
        this.send(requestNotiDTO, attendeeCustomUser.getPushToken());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.NotificationService#sendResponseJoinTeam(com.
     * ttth.teamcaring.domain.GroupsMember)
     */
    @Override
    @Async
    public void sendResponseJoinTeam(GroupsMember groupsMember) {
        // Find & delete request notification
        this.deleteByCustomUserAndTypeAndTargetId(groupsMember.getGroups().getLeader(), Notification.TYPE_REQUEST_TEAM, groupsMember.getId());

        // Get or create response join team
        String title, message;
        if (groupsMember.getStatus().equals(GroupsMember.STATUS_REJECTED)) {
            title = this.messageSource.getMessage("noti.title.team.join.response.reject", null,
                    null);
            Object[] args = { groupsMember.getGroups().getLeader().getNickname(),
                    groupsMember.getGroups().getTeam().getName() };
            message = this.messageSource.getMessage("noti.message.team.join.response.reject", args,
                    null);
        }
        else {
            title = this.messageSource.getMessage("noti.title.team.join.response.accept", null,
                    null);
            Object[] args = { groupsMember.getGroups().getLeader().getNickname(),
                    groupsMember.getGroups().getTeam().getName() };
            message = this.messageSource.getMessage("noti.message.team.join.response.accept", args,
                    null);
        }
        Notification responseNoti = this.notificationRepository
                .findOneByCustomUserAndTypeAndTargetId(groupsMember.getCustomUser(),
                        Notification.TYPE_RESPONSE_TEAM, groupsMember.getId());
        NotificationDTO responseNotiDTO;
        if (Objects.isNull(responseNoti)) {
            responseNotiDTO = new NotificationDTO()
                    .customUserId(groupsMember.getCustomUser().getId())
                    .type(Notification.TYPE_RESPONSE_TEAM).title(title).message(message)
                    .targetId(groupsMember.getId());
            responseNotiDTO = this.save(responseNotiDTO);
        }
        else {
            responseNotiDTO = this.notificationMapper.toDto(responseNoti);
        }
        this.send(responseNotiDTO, groupsMember.getCustomUser().getPushToken());
    }

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.NotificationService#sendResponseAppointment(com.ttth.teamcaring.domain.Attendee)
     */
    @Override
    @Async
    public void sendResponseAppointment(Attendee attendee) {
        // Find & delete request notification
        this.deleteByCustomUserAndTypeAndTargetId(attendee.getCustomUser(),
                Notification.TYPE_REQUEST_APPOINTMENT, attendee.getId());

        // Get or create response appointment
        String title, message;
        DateTimeFormatter dateTimeFormatter = this.timeMapper
                .getDateTimeFormatter("HH:mm dd/MM/yyyy", TimeMapper.defaultTimezone);
        if (attendee.getStatus().equals(Attendee.STATUS_REJECTED)) {
            title = this.messageSource.getMessage("noti.title.appointment.response.reject", null,
                    null);
            Object[] args = { attendee.getCustomUser().getNickname(),
                    dateTimeFormatter.format(attendee.getAppointment().getTime()) };
            message = this.messageSource.getMessage("noti.message.appointment.response.reject",
                    args, null);
        }
        else {
            title = this.messageSource.getMessage("noti.title.appointment.response.accept", null,
                    null);
            Object[] args = { attendee.getCustomUser().getNickname(),
                    dateTimeFormatter.format(attendee.getAppointment().getTime()) };
            message = this.messageSource.getMessage("noti.message.appointment.response.accept",
                    args, null);
        }
        Notification responseNoti = this.notificationRepository
                .findOneByCustomUserAndTypeAndTargetId(attendee.getAppointment().getCustomUser(),
                        Notification.TYPE_RESPONSE_APPOINTMENT, attendee.getId());
        NotificationDTO responseNotiDTO;
        if (Objects.isNull(responseNoti)) {
            responseNotiDTO = new NotificationDTO()
                    .customUserId(attendee.getAppointment().getCustomUser().getId())
                    .type(Notification.TYPE_RESPONSE_APPOINTMENT).title(title).message(message)
                    .targetId(attendee.getId());
            responseNotiDTO = this.save(responseNotiDTO);
        }
        else {
            responseNotiDTO = this.notificationMapper.toDto(responseNoti);
        }
        this.send(responseNotiDTO, attendee.getAppointment().getCustomUser().getPushToken());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.NotificationService#findAllOfAuthorizedUser(
     * org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<NotificationDTO> findAllOfAuthorizedUser(Pageable pageable) {
        String login = SecurityUtils.getCurrentUserLogin();
        CustomUser customUser = this.customUserService.findOneEntityByUserLogin(login);
        if (Objects.nonNull(customUser)) {
            List<NotificationDTO> notifications = this.notificationMapper
                    .toDto(new ArrayList<>(customUser.getNotifications()).stream()
                            .sorted(Collections.reverseOrder()).collect(Collectors.toList()));

            return PaginationUtil.pagingList(notifications, pageable);
        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot get team detail, user {0} not exist", login));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.NotificationService#
     * deleteAllRequestJoinTeamNotifications(java.util.List)
     */
    @Override
    public void deleteAllRequestJoinTeamNotifications(List<GroupsMember> deleted) {
        deleted.forEach(groupsMember -> {
            this.deleteByCustomUserAndTypeAndTargetId(groupsMember.getGroups().getLeader(),
                    Notification.TYPE_REQUEST_TEAM, groupsMember.getId());
        });
    }

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.NotificationService#deleteAllRequestAppointmentNotifications(java.util.List)
     */
    @Override
    public void deleteAllRequestAppointmentNotifications(List<Attendee> deleted) {
        deleted.forEach(att -> {
            this.deleteByCustomUserAndTypeAndTargetId(att.getCustomUser(),
                    Notification.TYPE_REQUEST_APPOINTMENT, att.getId());
        });
    }

    /**
     * Delete by custom user and type and target id.
     *
     * @param customUserReceiver the custom user receiver
     * @param type the type
     * @param targetId the target id
     */
    private void deleteByCustomUserAndTypeAndTargetId(CustomUser customUserReceiver, int type,
            Long targetId) {
        Long requestNotiId = this.notificationRepository
                .deleteByCustomUserAndTypeAndTargetId(customUserReceiver, type, targetId);
        if (requestNotiId.compareTo(0L) > 0) {
            this.log.debug("Delete notification {} successfully", requestNotiId);
        }
    }

}
