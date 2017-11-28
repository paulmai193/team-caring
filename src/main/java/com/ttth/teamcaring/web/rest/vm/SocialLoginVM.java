package com.ttth.teamcaring.web.rest.vm;

/**
 * View Model object for storing a user's credentials.
 *
 * @author Dai Mai
 */
public class SocialLoginVM {

    /** The social token. */
    private String socialToken;
    
    /** The push token. */
    private String pushToken;

    /**
     * Gets the social token.
     *
     * @return the socialToken
     */
    public String getSocialToken() {
        return socialToken;
    }

    /**
     * Sets the social token.
     *
     * @param token the new social token
     */
    public void setSocialToken(String token) {
        this.socialToken = token;
    }

	/**
	 * Gets the push token.
	 *
	 * @return the pushToken
	 */
	public String getPushToken() {
		return pushToken;
	}

	/**
	 * Sets the push token.
	 *
	 * @param pushToken the pushToken to set
	 */
	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}    
    
}
