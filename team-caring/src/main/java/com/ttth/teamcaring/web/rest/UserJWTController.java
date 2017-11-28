package com.ttth.teamcaring.web.rest;

import com.ttth.teamcaring.security.jwt.JWTConfigurer;
import com.ttth.teamcaring.security.jwt.TokenProvider;
import com.ttth.teamcaring.service.SocialService;
import com.ttth.teamcaring.service.dto.UserDTO;
import com.ttth.teamcaring.web.rest.util.HeaderUtil;
import com.ttth.teamcaring.web.rest.vm.LoginVM;
import com.ttth.teamcaring.web.rest.vm.SocialLoginVM;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Controller to authenticate users.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    /** The token provider. */
    private final TokenProvider tokenProvider;

    /** The authentication manager. */
    private final AuthenticationManager authenticationManager;

    /** The connection factory locator. */
    @Inject
    private ConnectionFactoryLocator connectionFactoryLocator;
    
    /** The social service. */
    @Inject
    private SocialService socialService;

     /**
      * Instantiates a new user JWT controller.
      *
      * @param tokenProvider the token provider
      * @param authenticationManager the authentication manager
      */
     public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authorize.
     *
     * @param loginVM the login VM
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
     * @param socialLoginVM the login VM
     * @param request the request
     * @param response the response
     * @return the response entity
     */
    @PostMapping("/authenticate/facebook")
    @Timed
    public ResponseEntity<SocialJWTToken> authorizeFacebook(@Valid @RequestBody SocialLoginVM socialLoginVM, NativeWebRequest request, HttpServletResponse response) {
        try {
            OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory("facebook");
            AccessGrant accessGrant = new AccessGrant(socialLoginVM.getToken());
            Connection<?> connection = connectionFactory.createConnection(accessGrant);
            UserDTO userDTO = this.socialService.createSocialUser(connection);
            
            // Authenticate
            String jwt = this.doAuthorize(new LoginVM(userDTO.getLogin(), null));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new SocialJWTToken(jwt, userDTO.isActivated()), httpHeaders, HttpStatus.OK);
        }
        catch (Exception exc) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(HeaderUtil.createFailureAlert("error", "social-signin", "Cannot sign-in with given social access token")).body(null);
        }
    }
    
    /**
     * Do authorize.
     *
     * @param loginVM the login VM
     * @return the jwt token string
     */
    private String doAuthorize(LoginVM loginVM) {
    	UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
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
         * @param idToken the id token
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
         * @param idToken the new id token
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
	    private boolean activated;

		/**
		 * Instantiates a new social JWT token.
		 *
		 * @param idToken the id token
		 * @param activated the activated
		 */
		public SocialJWTToken(String idToken, boolean activated) {
			super(idToken);
			this.activated = activated;
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
		 * @param activated the activated to set
		 */
		public void setActivated(boolean activated) {
			this.activated = activated;
		}
    	
    }
}