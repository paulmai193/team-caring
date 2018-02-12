/*
 * 
 */
package com.ttth.teamcaring.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Simple exception with a message, that returns an Internal Server Error code.
 *
 * @author Dai Mai
 */
public class InternalServerErrorException extends AbstractThrowableProblem {

    /**
     * Instantiates a new internal server error exception.
     *
     * @param message
     *        the message
     */
    public InternalServerErrorException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.INTERNAL_SERVER_ERROR);
    }
}
