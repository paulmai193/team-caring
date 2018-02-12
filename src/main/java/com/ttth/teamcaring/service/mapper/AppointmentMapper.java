/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.service.dto.AppointmentDTO;

/**
 * Mapper for the entity Appointment and its DTO AppointmentDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { CustomUserMapper.class, TeamMapper.class,
        TimeMapper.class })
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "customUser.id", target = "customUserId")
    @Mapping(source = "team.id", target = "teamId")
    AppointmentDTO toDto(Appointment appointment);

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toEntity(java.lang.Object)
     */
    @Mapping(source = "customUserId", target = "customUser")
    @Mapping(source = "teamId", target = "team")
    @Mapping(target = "attendees", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Appointment toEntity(AppointmentDTO appointmentDTO);

    /**
     * From id.
     *
     * @param id the id
     * @return the appointment
     */
    default Appointment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        appointment.setId(id);
        return appointment;
    }
}
