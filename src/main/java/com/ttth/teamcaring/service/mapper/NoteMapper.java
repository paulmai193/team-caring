/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.Note;
import com.ttth.teamcaring.service.dto.NoteDTO;

/**
 * Mapper for the entity Note and its DTO NoteDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = {CustomUserMapper.class, AppointmentMapper.class, TimeMapper.class})
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "customUser.id", target = "customUserId")
    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "customUser.user.imageUrl", target = "imageUrl")
    NoteDTO toDto(Note note); 

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toEntity(java.lang.Object)
     */
    @Mapping(source = "customUserId", target = "customUser")
    @Mapping(source = "appointmentId", target = "appointment")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Note toEntity(NoteDTO noteDTO);

    /**
     * From id.
     *
     * @param id the id
     * @return the note
     */
    default Note fromId(Long id) {
        if (id == null) {
            return null;
        }
        Note note = new Note();
        note.setId(id);
        return note;
    }
}
