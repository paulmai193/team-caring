package com.ttth.teamcaring.web.rest;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.UserRepository;
import com.ttth.teamcaring.security.AuthoritiesConstants;
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.MailService;
import com.ttth.teamcaring.service.UserService;
import com.ttth.teamcaring.service.dto.ProfileDTO;
import com.ttth.teamcaring.service.dto.UserDTO;
import com.ttth.teamcaring.web.rest.errors.EmailAlreadyUsedException;
import com.ttth.teamcaring.web.rest.errors.EmailNotFoundException;
import com.ttth.teamcaring.web.rest.errors.InternalServerErrorException;
import com.ttth.teamcaring.web.rest.errors.InvalidPasswordException;
import com.ttth.teamcaring.web.rest.errors.LoginAlreadyUsedException;
import com.ttth.teamcaring.web.rest.vm.KeyAndPasswordVM;
import com.ttth.teamcaring.web.rest.vm.ManagedUserVM;

/**
* REST controller for managing the current user's account.
*/
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }
    
    /********************************************************************
     *                        REGISTER NEW ACCOUNT                      * 
     ********************************************************************/

    /**
    * POST  /register : register the user.
    *
    * @param managedUserVM the managed user View Model
    * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
    * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
    * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already used
    */
    @PostMapping("/register")
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(AuthoritiesConstants.ADMIN)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).ifPresent(u -> {throw new LoginAlreadyUsedException();});
        userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).ifPresent(u -> {throw new EmailAlreadyUsedException();});
        User user = userService.registerUser(managedUserVM);
        mailService.sendActivationEmail(user);
    }

    /**
    * GET  /activate : activate the registered user.
    *
    * @param key the activation key
    * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
    */
    @GetMapping("/activate")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        };
    }

    /********************************************************************
     *                           MANAGE ACCOUNT                         * 
     ********************************************************************/
    
    /**
    * GET  /authenticate : check if the user is authenticated, and return its login.
    *
    * @param request the HTTP request
    * @return the login if the user is authenticated
    */
    @GetMapping("/authenticate")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
    * GET  /account : get the current user.
    *
    * @return the current user
    * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
    */
    @GetMapping("/account")
    @Timed
    public UserDTO getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
    }

    /**
    * POST  /account : update the current user information.
    *
    * @param userDTO the current user information
    * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
    * @throws RuntimeException 500 (Internal Server Error) if the user login wasn't found
    */
    @PostMapping("/account")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
    }
    
    /**
     * POST  /update-profile : Update basic profile information (full name, nickname, push token) 
     * and anonymous group information for the CURRENT user.
     * (get by current JWT)
     *
     * @param profileDTO the profile DTO
     * @throws RuntimeException 500 (Internal Server Error) if the user login wasn't found
     */
    @PostMapping("/update-profile")
    public void updateProfile(@Valid @RequestBody ProfileDTO profileDTO) {
    	final String userLogin = SecurityUtils.getCurrentUserLogin();
    	Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUserProfile(profileDTO.getProfile(), profileDTO.getAnonymousGroup());
    }
    
    /**
     * GET  /get-profile : Get basic profile information (full name, nickname, push token) of the CURRENT user.
     * (get by current JWT)
     *
     * @param userId the user id
     * @return the login if the user is authenticated
     */
    @GetMapping("/get-profile/{id}")
    public ProfileDTO getProfile(@PathVariable("id") Long userId) {
    	return Optional.ofNullable(userService.getUserProfileByUserId(userId))
                .orElseThrow(() -> new InternalServerErrorException("User profile could not be found"));
    }

    /**
    * POST  /account/change-password : changes the current user's password
    *
    * @param password the new password
    * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
    */
    @PostMapping(path = "/account/change-password")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public void changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(password);
   }

    /**
    * POST   /account/reset-password/init : Send an email to reset the password of the user
    *
    * @param mail the mail of the user
    * @throws EmailNotFoundException 400 (Bad Request) if the email address is not registered
    */
    @PostMapping(path = "/account/reset-password/init")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public void requestPasswordReset(@RequestBody String mail) {
       mailService.sendPasswordResetMail(
           userService.requestPasswordReset(mail)
               .orElseThrow(EmailNotFoundException::new)
       );
    }

    /**
    * POST   /account/reset-password/finish : Finish to reset the password of the user
    *
    * @param keyAndPassword the generated key and the new password
    * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
    * @throws RuntimeException 500 (Internal Server Error) if the password could not be reset
    */
    @PostMapping(path = "/account/reset-password/finish")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
