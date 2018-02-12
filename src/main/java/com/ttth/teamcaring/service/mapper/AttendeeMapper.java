/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.service.dto.AttendeeDTO;

/**
 * Mapper for the entity Attendee and its DTO AttendeeDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { CustomUserMapper.class, AppointmentMapper.class })
public interface AttendeeMapper extends EntityMapper<AttendeeDTO, Attendee> {

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "customUser.id", target = "customUserId")
    @Mapping(source = "appointment.id", target = "appointmentId")
    AttendeeDTO toDto(Attendee attendee);

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toEntity(java.lang.Object)
     */
    @Mapping(source = "customUserId", target = "customUser")
    @Mapping(source = "appointmentId", target = "appointment")
    Attendee toEntity(AttendeeDTO attendeeDTO);

    /**
     * From id.
     *
     * @param id the id
     * @return the attendee
     */
    default Attendee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attendee attendee = new Attendee();
        attendee.setId(id);
        return attendee;
    }
}
