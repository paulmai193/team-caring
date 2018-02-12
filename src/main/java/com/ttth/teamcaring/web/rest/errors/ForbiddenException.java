/*
 * 
 */
package com.ttth.teamcaring.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Forbidden error exception with a message, that returns an Forbidden Error
 * code.
 *
 * @author Dai Mai
 */
public class ForbiddenException extends AbstractThrowableProblem {

    /**
     * Instantiates a new forbidden exception.
     *
     * @param message
     *        the message
     */
    public ForbiddenException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.FORBIDDEN);
    }
}
