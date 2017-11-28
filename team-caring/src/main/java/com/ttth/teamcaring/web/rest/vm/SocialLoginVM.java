package com.ttth.teamcaring.web.rest.vm;

/**
 * View Model object for storing a user's credentials.
 */
public class SocialLoginVM {

    private String token;

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
    
}
