/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.Notification;
import com.ttth.teamcaring.service.dto.NotificationDTO;

/**
 * Mapper for the entity Notification and its DTO NotificationDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { CustomUserMapper.class, TimeMapper.class })
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "customUser.id", target = "customUserId")
    NotificationDTO toDto(Notification notification);

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toEntity(java.lang.
     * Object)
     */
    @Mapping(source = "customUserId", target = "customUser")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Notification toEntity(NotificationDTO notificationDTO);

    /**
     * From id.
     *
     * @param id
     *        the id
     * @return the notification
     */
    default Notification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setId(id);
        return notification;
    }

}
