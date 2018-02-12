/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import java.util.List;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @author Dai Mai
 * @param <D>
 *        - DTO type parameter.
 * @param <E>
 *        - Entity type parameter.
 */

public interface EntityMapper<D, E> {

    /**
     * To entity.
     *
     * @param dto
     *        the dto
     * @return the e
     */
    public E toEntity(D dto);

    /**
     * To dto.
     *
     * @param entity
     *        the entity
     * @return the d
     */
    public D toDto(E entity);

    /**
     * To entity.
     *
     * @param dtoList
     *        the dto list
     * @return the list
     */
    public List<E> toEntity(List<D> dtoList);

    /**
     * To dto.
     *
     * @param entityList
     *        the entity list
     * @return the list
     */
    public List<D> toDto(List<E> entityList);

}
