/*
 * 
 */
package com.ttth.teamcaring.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.ttth.teamcaring.config.Constants;

/**
 * Implementation of AuditorAware based on Spring Security.
 *
 * @author Dai Mai
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.AuditorAware#getCurrentAuditor()
     */
    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        return userName != null ? userName : Constants.SYSTEM_ACCOUNT;
    }
}
