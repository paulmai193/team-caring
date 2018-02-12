/*
 * 
 */
package com.ttth.teamcaring.web.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.ServerException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.security.jwt.JWTConfigurer;
import com.ttth.teamcaring.security.jwt.TokenProvider;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.SocialService;
import com.ttth.teamcaring.service.dto.ProfileDTO;
import com.ttth.teamcaring.web.rest.util.HeaderUtil;
import com.ttth.teamcaring.web.rest.vm.LoginVM;
import com.ttth.teamcaring.web.rest.vm.SocialLoginVM;

/**
 * Controller to authenticate users.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    /** The log. */
    private final Logger                log = LoggerFactory.getLogger(UserJWTController.class);

    /** The token provider. */
    private final TokenProvider         tokenProvider;

    /** The authentication manager. */
    private final AuthenticationManager authenticationManager;

    /** The connection factory locator. */
    @Inject
    private ConnectionFactoryLocator    connectionFactoryLocator;

    /** The social service. */
    @Inject
    private SocialService               socialService;

    /** The custom user service. */
    @Inject
    private CustomUserService           customUserService;

    /**
     * Instantiates a new user JWT controller.
     *
     * @param tokenProvider
     *        the token provider
     * @param authenticationManager
     *        the authentication manager
     */
    public UserJWTController(TokenProvider tokenProvider,
            AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authorize.
     *
     * @param loginVM
     *        the login VM
     * @return the response entity
     */
    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        String jwt = this.doAuthorize(loginVM);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Authorize facebook.
     *
     * @param socialLoginVM
     *        the login VM
     * @param request
     *        the request
     * @param response
     *        the response
     * @return the response entity
     */
    @PostMapping("/authenticate/facebook")
    @Timed
    public ResponseEntity<SocialJWTToken> authorizeFacebook(
            @Valid @RequestBody SocialLoginVM socialLoginVM, NativeWebRequest request,
            HttpServletResponse response) {
        try {
            OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator
                    .getConnectionFactory("facebook");
            AccessGrant accessGrant = new AccessGrant(socialLoginVM.getSocialToken());
            Connection<?> connection = connectionFactory.createConnection(accessGrant);
            User user = this.socialService.createSocialUser(connection);
            this.customUserService.findOneByUserId(user.getId()).ifPresent(customUserDTO -> {
                customUserDTO.setPushToken(socialLoginVM.getPushToken());
                if (StringUtils.isEmpty(customUserDTO.getFullName()))
                    customUserDTO.setFullName(connection.getDisplayName());
                if (StringUtils.isEmpty(customUserDTO.getNickname()))
                    customUserDTO.setNickname(connection.getDisplayName());
                this.customUserService.save(customUserDTO);
            });
            ProfileDTO profileDTO = this.customUserService.findOneByUserId(user.getId()).get()
                    .castToSimpleProfileDto();

            // Authenticate
            String jwt = this.doAuthorize(new LoginVM(user.getLogin(), user.getPassword()));
            this.log.debug("JWT for authorized user: {}", jwt);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(
                    new SocialJWTToken(jwt,
                            !StringUtils.equalsIgnoreCase(user.getActivationKey(),
                                    connection.getKey().getProviderId()),
                            profileDTO),
                    httpHeaders, HttpStatus.OK);
        }
        catch (ServerException e) {
            this.log.error("Cannot sign-in", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(
                            HeaderUtil.createFailureAlert("error", "social-signin", e.getMessage()))
                    .body(null);
        }
        catch (Exception exc) {
            this.log.error("Cannot sign-in", exc);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED).headers(HeaderUtil.createFailureAlert("error",
                            "social-signin", "Cannot sign-in with given facebook access token"))
                    .body(null);
        }
    }

    /**
     * Do authorize.
     *
     * @param loginVM
     *        the login VM
     * @return the jwt token string
     */
    private String doAuthorize(LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = this.authenticationManager
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        return tokenProvider.createToken(authentication, rememberMe);
    }

    /**
     * Object to return as body in JWT Authentication.
     *
     * @author Dai Mai
     */
    static class JWTToken {

        /** The id token. */
        private String idToken;

        /**
         * Instantiates a new JWT token.
         *
         * @param idToken
         *        the id token
         */
        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        /**
         * Gets the id token.
         *
         * @return the id token
         */
        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        /**
         * Sets the id token.
         *
         * @param idToken
         *        the new id token
         */
        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    /**
     * The Class SocialJWTToken.
     *
     * @author Dai Mai
     */
    static class SocialJWTToken extends JWTToken {

        /** The activated. */
        private boolean    activated;

        /** The profile. */
        private ProfileDTO profile;

        /**
         * Instantiates a new social JWT token.
         *
         * @param idToken
         *        the id token
         * @param activated
         *        the activated
         * @param profile
         *        the profile
         */
        public SocialJWTToken(String idToken, boolean activated, ProfileDTO profile) {
            super(idToken);
            this.activated = activated;
            this.profile = profile;
        }

        /**
         * Checks if is activated.
         *
         * @return the activated
         */
        public boolean isActivated() {
            return activated;
        }

        /**
         * Sets the activated.
         *
         * @param activated
         *        the activated to set
         */
        public void setActivated(boolean activated) {
            this.activated = activated;
        }

        /**
         * Gets the profile.
         *
         * @return the profile
         */
        public ProfileDTO getProfile() {
            return profile;
        }

        /**
         * Sets the profile.
         *
         * @param profile
         *        the profile to set
         */
        public void setProfile(ProfileDTO profile) {
            this.profile = profile;
        }

    }
}
