package com.nca.config.initializer;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <P>A {@code DataInitializer} intended to initialize data to connected relation database.
 * <P>A {@code TestDataInitializer} implementation to initialize necessary .sql script.
 * <P>Before using it, check existence of .sql files into resources package.
 * If it missed try to download project again.
 * <P>You can rewrite scripts inside .sql for inserting, but if you rewrite creation, this will affect how the API works.
 */

@Profile("test")
@Component("dataInitializer")
public class TestDataInitializer extends DataInitializer {

    /**
     * {@inheritDoc}
     */
    @Override
    public String path() {
        return Objects.requireNonNull(this.getClass().getClassLoader().getResource("data/data-test.sql")).getPath();
    }
}
