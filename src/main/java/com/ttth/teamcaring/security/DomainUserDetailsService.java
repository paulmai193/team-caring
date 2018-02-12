/*
 * 
 */
package com.ttth.teamcaring.security;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.UserRepository;

/**
 * Authenticate a user from the database.
 *
 * @author Dai Mai
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    /** The log. */
    private final Logger         log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    /** The user repository. */
    private final UserRepository userRepository;

    /**
     * Instantiates a new domain user details service.
     *
     * @param userRepository
     *        the user repository
     */
    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.core.userdetails.UserDetailsService#
     * loadUserByUsername(java.lang.String)
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase = userRepository
                .findOneWithAuthoritiesByLogin(lowercaseLogin);
        return userFromDatabase.map(user -> {
            if (!user.getActivated()) {
                throw new UserNotActivatedException(
                        "User " + lowercaseLogin + " was not activated");
            }
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                    user.getPassword(), grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException(
                "User " + lowercaseLogin + " was not found in the " + "database"));
    }
}
