/*
 * 
 */
package com.ttth.teamcaring.web.rest.errors;

import static org.zalando.problem.Status.BAD_REQUEST;

import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;

/**
 * Custom, parameterized exception, which can be translated on the client side.
 * For example:
 * 
 * <pre>
 * throw new CustomParameterizedException(&quot;myCustomError&quot;, &quot;hello&quot;, &quot;world&quot;);
 * </pre>
 * 
 * Can be translated with:
 * 
 * <pre>
 * "error.myCustomError" :  "The server says {{param0}} to {{param1}}"
 * </pre>
 *
 * @author Dai Mai
 */
public class CustomParameterizedException extends AbstractThrowableProblem {

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = 1L;

    /** The Constant PARAM. */
    private static final String PARAM            = "param";

    /**
     * Instantiates a new custom parameterized exception.
     *
     * @param message
     *        the message
     * @param params
     *        the params
     */
    public CustomParameterizedException(String message, String... params) {
        this(message, toParamMap(params));
    }

    /**
     * Instantiates a new custom parameterized exception.
     *
     * @param message
     *        the message
     * @param paramMap
     *        the param map
     */
    public CustomParameterizedException(String message, Map<String, Object> paramMap) {
        super(ErrorConstants.PARAMETERIZED_TYPE, "Parameterized Exception", BAD_REQUEST, null, null,
                null, toProblemParameters(message, paramMap));
    }

    /**
     * To param map.
     *
     * @param params
     *        the params
     * @return the map
     */
    public static Map<String, Object> toParamMap(String... params) {
        Map<String, Object> paramMap = new HashMap<>();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                paramMap.put(PARAM + i, params[i]);
            }
        }
        return paramMap;
    }

    /**
     * To problem parameters.
     *
     * @param message
     *        the message
     * @param paramMap
     *        the param map
     * @return the map
     */
    public static Map<String, Object> toProblemParameters(String message,
            Map<String, Object> paramMap) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", message);
        parameters.put("params", paramMap);
        return parameters;
    }
}
