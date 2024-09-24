package com.nca.config.initializer;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <P>A {@code DataInitializer} intended to initialize data to connected relation database.
 * <P>Before using it, check existence of .sql files into resources package.
 * If it missed try to download project again.
 * <P>You can rewrite scripts inside .sql for inserting, but if you rewrite creation, this will affect how the API works.
 */

public abstract class DataInitializer {

    @Autowired
    private DataSource dataSource;

    /**
     * Generate sql tables. Fulfill it with default values if profile is test.
     *
     * @throws FileNotFoundException if the named file does not exist,
     *                               is a directory rather than a regular file,
     *                               or for some other reason cannot be opened for
     *                               reading.
     * @throws SQLException          if a database access error occurs,
     *                               this method is called while participating in a distributed transaction,
     *                               this method is called on a closed connection.
     */
    public void initialize() throws SQLException, FileNotFoundException {
        String path = path();
        Connection connection = dataSource.getConnection();
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setSendFullScript(false);
        scriptRunner.setStopOnError(true);
        scriptRunner.runScript(new FileReader(path));
    }

    /**
     * Calculating the path to data-test.sql or data-prod.sql script.
     * Depends on profile by which API was started.
     *
     * @return Stringify path to file .sql script
     * @throws NullPointerException if the named file does not exist,
     *                              is a directory rather than a regular file,
     *                              or for some other reason cannot be opened for
     *                              reading.
     */
    protected abstract String path();

}
