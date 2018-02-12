/*
 * 
 */
package com.ttth.teamcaring.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ttth.teamcaring.config.Constants;

/**
 * View Model object for storing a user's credentials.
 *
 * @author Dai Mai
 */
public class LoginVM {

    /** The username. */
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    @Size(min = 1, max = 50)
    private String  username;

    /** The password. */
    @NotNull
    @Size(min = ManagedUserVM.PASSWORD_MIN_LENGTH, max = ManagedUserVM.PASSWORD_MAX_LENGTH)
    private String  password;

    /** The remember me. */
    private Boolean rememberMe;

    /**
     * Instantiates a new login VM.
     */
    public LoginVM() {
    }

    /**
     * Instantiates a new login VM.
     *
     * @param username
     *        the username
     * @param password
     *        the password
     */
    public LoginVM(String username, String password) {
        this.username = username;
        this.password = password;
        this.rememberMe = true;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username
     *        the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *        the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Checks if is remember me.
     *
     * @return the boolean
     */
    public Boolean isRememberMe() {
        return rememberMe;
    }

    /**
     * Sets the remember me.
     *
     * @param rememberMe
     *        the new remember me
     */
    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LoginVM{" + "username='" + username + '\'' + ", rememberMe=" + rememberMe + '}';
    }
}
