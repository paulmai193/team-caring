/*
 * 
 */
package com.ttth.teamcaring.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.User;

/**
 * Spring Data JPA repository for the User entity.
 *
 * @author Dai Mai
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find one by activation key.
     *
     * @param activationKey
     *        the activation key
     * @return the optional
     */
    Optional<User> findOneByActivationKey(String activationKey);

    /**
     * Find all by activated is false and created date before.
     *
     * @param dateTime
     *        the date time
     * @return the list
     */
    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    /**
     * Find one by reset key.
     *
     * @param resetKey
     *        the reset key
     * @return the optional
     */
    Optional<User> findOneByResetKey(String resetKey);

    /**
     * Find one by email ignore case.
     *
     * @param email
     *        the email
     * @return the optional
     */
    Optional<User> findOneByEmailIgnoreCase(String email);

    /**
     * Find one by login.
     *
     * @param login
     *        the login
     * @return the optional
     */
    Optional<User> findOneByLogin(String login);

    /**
     * Find one with authorities by id.
     *
     * @param id
     *        the id
     * @return the user
     */
    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);

    /**
     * Find one with authorities by login.
     *
     * @param login
     *        the login
     * @return the optional
     */
    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = "users")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    /**
     * Find all by login not.
     *
     * @param pageable
     *        the pageable
     * @param login
     *        the login
     * @return the page
     */
    Page<User> findAllByLoginNot(Pageable pageable, String login);

    /**
     * Find one by id.
     *
     * @param id
     *        the id
     * @return the optional
     */
    Optional<User> findOneById(Long id);
}
