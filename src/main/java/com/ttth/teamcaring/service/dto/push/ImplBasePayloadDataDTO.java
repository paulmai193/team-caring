/*
 * 
 */
package com.ttth.teamcaring.service.dto.push;

import java.io.Serializable;

/**
 * The Class ImplBasePayloadDataDTO.
 *
 * @author Dai Mai
 */
public class ImplBasePayloadDataDTO implements BasePayloadDataDTO, Serializable {

    /** The type. */
    private Integer type;

    /** The target id. */
    private Long    targetId;

    /**
     * Instantiates a new impl base payload data DTO.
     *
     * @param type
     *        the type
     * @param targetId
     *        the target id
     */
    public ImplBasePayloadDataDTO(Integer type, Long targetId) {
        super();
        this.type = type;
        this.targetId = targetId;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *        the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * Gets the target id.
     *
     * @return the targetId
     */
    public Long getTargetId() {
        return targetId;
    }

    /**
     * Sets the target id.
     *
     * @param targetId
     *        the targetId to set
     */
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ImplBasePayloadDataDTO [type=" + type + ", targetId=" + targetId + "]";
    }

}
