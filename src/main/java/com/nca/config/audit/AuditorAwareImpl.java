package com.nca.config.audit;

import com.nca.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * {@code AuditorAwareImpl} for implementing Auditing logic.
 * It helps in generating fields depended on time of persistence or updating
 * and also to include person who did this
 */
@Component
public class AuditorAwareImpl implements AuditorAware<User> {

    @Override
    public User getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((User) authentication.getPrincipal());
    }
}