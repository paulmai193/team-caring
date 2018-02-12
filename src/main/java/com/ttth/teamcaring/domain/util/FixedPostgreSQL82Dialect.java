/*
 * 
 */
package com.ttth.teamcaring.domain.util;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

/**
 * The Class FixedPostgreSQL82Dialect.
 *
 * @author Dai Mai
 */
public class FixedPostgreSQL82Dialect extends PostgreSQL82Dialect {

    /**
     * Instantiates a new fixed postgre SQL 82 dialect.
     */
    public FixedPostgreSQL82Dialect() {
        super();
        registerColumnType(Types.BLOB, "bytea");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.hibernate.dialect.Dialect#remapSqlTypeDescriptor(org.hibernate.type.
     * descriptor.sql.SqlTypeDescriptor)
     */
    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) {
            return BinaryTypeDescriptor.INSTANCE;
        }
        return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }
}
