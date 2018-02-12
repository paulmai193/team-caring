/*
 * 
 */
package com.ttth.teamcaring.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.TeamCaringApp;
import com.ttth.teamcaring.config.Constants;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.UserRepository;
import com.ttth.teamcaring.service.dto.UserDTO;
import com.ttth.teamcaring.service.util.RandomUtil;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Transactional
public class UserServiceIntTest {

    /** The user repository. */
    @Autowired
    private UserRepository userRepository;

    /** The user service. */
    @Autowired
    private UserService    userService;

    /** The user. */
    private User           user;

    /**
     * Inits the.
     */
    @Before
    public void init() {
        user = new User();
        user.setLogin("johndoe");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("johndoe@localhost");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("en");
    }

    /**
     * Assert that user must exist to reset password.
     */
    @Test
    @Transactional
    public void assertThatUserMustExistToResetPassword() {
        userRepository.saveAndFlush(user);
        Optional<User> maybeUser = userService.requestPasswordReset("invalid.login@localhost");
        assertThat(maybeUser).isNotPresent();

        maybeUser = userService.requestPasswordReset(user.getEmail());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getEmail()).isEqualTo(user.getEmail());
        assertThat(maybeUser.orElse(null).getResetDate()).isNotNull();
        assertThat(maybeUser.orElse(null).getResetKey()).isNotNull();
    }

    /**
     * Assert that only activated user can request password reset.
     */
    @Test
    @Transactional
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        user.setActivated(false);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.requestPasswordReset(user.getLogin());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }

    /**
     * Assert that reset key must not be older than 24 hours.
     */
    @Test
    @Transactional
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2",
                user.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }

    /**
     * Assert that reset key must be valid.
     */
    @Test
    @Transactional
    public void assertThatResetKeyMustBeValid() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey("1234");
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2",
                user.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }

    /**
     * Assert that user can reset password.
     */
    @Test
    @Transactional
    public void assertThatUserCanResetPassword() {
        String oldPassword = user.getPassword();
        Instant daysAgo = Instant.now().minus(2, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2",
                user.getResetKey());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getResetDate()).isNull();
        assertThat(maybeUser.orElse(null).getResetKey()).isNull();
        assertThat(maybeUser.orElse(null).getPassword()).isNotEqualTo(oldPassword);

        userRepository.delete(user);
    }

    /**
     * Test find not activated users by creation date before.
     */
    @Test
    @Transactional
    public void testFindNotActivatedUsersByCreationDateBefore() {
        Instant now = Instant.now();
        user.setActivated(false);
        User dbUser = userRepository.saveAndFlush(user);
        dbUser.setCreatedDate(now.minus(4, ChronoUnit.DAYS));
        userRepository.saveAndFlush(user);
        List<User> users = userRepository
                .findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
        assertThat(users).isNotEmpty();
        userService.removeNotActivatedUsers();
        users = userRepository
                .findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
        assertThat(users).isEmpty();
    }

    /**
     * Assert that anonymous user is not get.
     */
    @Test
    @Transactional
    public void assertThatAnonymousUserIsNotGet() {
        user.setLogin(Constants.ANONYMOUS_USER);
        if (!userRepository.findOneByLogin(Constants.ANONYMOUS_USER).isPresent()) {
            userRepository.saveAndFlush(user);
        }
        final PageRequest pageable = new PageRequest(0, (int) userRepository.count());
        final Page<UserDTO> allManagedUsers = userService.getAllManagedUsers(pageable);
        assertThat(allManagedUsers.getContent().stream()
                .noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin()))).isTrue();
    }

    /**
     * Test remove not activated users.
     */
    @Test
    @Transactional
    public void testRemoveNotActivatedUsers() {
        user.setActivated(false);
        userRepository.saveAndFlush(user);
        // Let the audit first set the creation date but then update it
        user.setCreatedDate(Instant.now().minus(30, ChronoUnit.DAYS));
        userRepository.saveAndFlush(user);

        assertThat(userRepository.findOneByLogin("johndoe")).isPresent();
        userService.removeNotActivatedUsers();
        assertThat(userRepository.findOneByLogin("johndoe")).isNotPresent();
    }

}
