package com.nca.config.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * {@code AuditorAwareImpl} for implementing.
 * It helps in generating fields depended on time of persistence or updating
 * and also to include person who did this
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        return String.valueOf(Optional.of("System"));
    }
}