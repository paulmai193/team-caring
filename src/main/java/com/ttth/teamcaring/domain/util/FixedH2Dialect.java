/*
 * 
 */
package com.ttth.teamcaring.domain.util;

import java.sql.Types;

import org.hibernate.dialect.H2Dialect;

/**
 * The Class FixedH2Dialect.
 *
 * @author Dai Mai
 */
public class FixedH2Dialect extends H2Dialect {

    /**
     * Instantiates a new fixed H 2 dialect.
     */
    public FixedH2Dialect() {
        super();
        registerColumnType(Types.FLOAT, "real");
    }
}
