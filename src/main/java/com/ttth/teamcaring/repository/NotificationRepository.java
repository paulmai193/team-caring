/*
 * 
 */
package com.ttth.teamcaring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.Notification;

/**
 * Spring Data JPA repository for the Notification entity.
 *
 * @author Dai Mai
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find one by custom user and type and meta data.
     *
     * @param customUser
     *        the custom user
     * @param type
     *        the type
     * @param targetId
     *        the target id
     * @return the notification
     */
    Notification findOneByCustomUserAndTypeAndTargetId(CustomUser customUser, Integer type,
            Long targetId);

    /**
     * Delete by custom user and type and meta data.
     *
     * @param customUser
     *        the custom user
     * @param type
     *        the type
     * @param targetId
     *        the target id
     * @return the notification ID was deleted
     */
    Long deleteByCustomUserAndTypeAndTargetId(CustomUser customUser, Integer type, Long targetId);

}
