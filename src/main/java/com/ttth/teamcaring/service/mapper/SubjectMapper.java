package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.Subject;
import com.ttth.teamcaring.service.dto.SubjectDTO;

/**
 * Mapper for the entity Subject and its DTO SubjectDTO.
 */
@Mapper(componentModel = "spring", uses = {IconMapper.class})
public interface SubjectMapper extends EntityMapper<SubjectDTO, Subject> {

    @Mapping(source = "icon.id", target = "iconId")
    SubjectDTO toDto(Subject subject); 

    @Mapping(source = "iconId", target = "icon")
    Subject toEntity(SubjectDTO subjectDTO);

    default Subject fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subject subject = new Subject();
        subject.setId(id);
        return subject;
    }
}
