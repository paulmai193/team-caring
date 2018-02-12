/*
 * 
 */
package com.ttth.teamcaring.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.domain.Team;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;
import com.ttth.teamcaring.service.dto.NotificationDTO;
import com.ttth.teamcaring.service.dto.push.FcmResponseDTO;

/**
 * Service Interface for managing Notification.
 *
 * @author Dai Mai
 */
public interface NotificationService {

    /**
     * Save a notification.
     *
     * @param notificationDTO
     *        the entity to save
     * @return the persisted entity
     */
    NotificationDTO save(NotificationDTO notificationDTO);

    /**
     * Get all the notifications.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<NotificationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" notification.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    NotificationDTO findOne(Long id);

    /**
     * Delete the "id" notification.
     *
     * @param id
     *        the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the notification corresponding to the query.
     *
     * @param query
     *        the query of the search
     * 
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<NotificationDTO> search(String query, Pageable pageable);

    /**
     * Save and send notification.
     *
     * @param notificationDTO
     *        the notification DTO
     * @param registrationId
     *        the registration id
     * @return the fcm response DTO
     */
    FcmResponseDTO send(NotificationDTO notificationDTO, String registrationId);

    /**
     * Send request join team.
     *
     * @param customUser
     *        the request custom user
     * @param leaderCustomUser
     *        the leader custom user of team
     * @param team
     *        the team
     * @param groupsMemberDTO
     *        the groups member DTO
     */
    void sendRequestJoinTeam(CustomUser customUser, CustomUser leaderCustomUser, Team team,
            GroupsMemberDTO groupsMemberDTO);

    /**
     * Send response join team.
     *
     * @param groupsMember
     *        the groups member response
     */
    void sendResponseJoinTeam(GroupsMember groupsMember);

    /**
     * Find all of authorized user.
     *
     * @param pageable
     *        the pageable
     * @return the page
     */
    Page<NotificationDTO> findAllOfAuthorizedUser(Pageable pageable);

    /**
     * Delete all request join team notifications.
     *
     * @param deleted
     *        the deleted
     */
    void deleteAllRequestJoinTeamNotifications(List<GroupsMember> deleted);

    /**
     * Delete all request appointment notifications.
     *
     * @param deleted the deleted
     */
    void deleteAllRequestAppointmentNotifications(List<Attendee> deleted);

    /**
     * Send request appointment.
     *
     * @param attendee the attendee
     */
    void sendRequestAppointment(Attendee attendee);

    /**
     * Send response appointment.
     *
     * @param attendee the attendee
     */
    void sendResponseAppointment(Attendee attendee);
}
