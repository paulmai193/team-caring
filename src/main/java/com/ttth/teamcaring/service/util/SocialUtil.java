/*
 * 
 */
package com.ttth.teamcaring.service.util;

import org.springframework.social.connect.Connection;

/**
 * The Class SocialUtil.
 *
 * @author Dai Mai
 */
public final class SocialUtil {

    /**
     * Fetch user image url.
     *
     * @param connection
     *        the connection
     * @param providerId
     *        the provider id
     * @return the string
     */
    public static String fetchUserImageUrl(Connection<?> connection, String providerId) {
        String imageUrl = connection.getImageUrl();
        if ("facebook".equals(providerId)) {
            imageUrl = imageUrl + "?type=large";
        }
        return imageUrl;
    }

}
