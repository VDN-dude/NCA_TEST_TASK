package com.nca.generator;

import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * {@code LongIdGenerator} to generate long value for id.
 */

public class LongIdGenerator implements IdentifierGenerator {

    /**
     * {@code generate} to generate long for id value depended on max id value in the table which bounded with provided entity.
     *
     * @param session
     * @param object
     * @return {@code Serializable} value as Long.
     */
    @SneakyThrows
    @Override
    public Serializable generate(SessionImplementor session, Object object)
            throws HibernateException {

        Connection connection = session.connection();

        String table = String.valueOf(
                session.getEntityPersister(object.getClass().getName(), object)
                        .getQuerySpaces()[0]);

        PreparedStatement ps = connection.prepareStatement("select max(id) from " + table);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getLong(1) + 1;
        }

        return 1;
    }
}
