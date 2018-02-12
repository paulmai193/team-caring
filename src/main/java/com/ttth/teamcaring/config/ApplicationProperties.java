/*
 * 
 */
package com.ttth.teamcaring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Team Caring.
 * <p>
 * Properties are configured in the application.yml file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 *
 * @author Dai Mai
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    /** The fcm. */
    private final FcmProperties fcm = new FcmProperties();

    /**
     * Gets the fcm.
     *
     * @return the fcm
     */
    public FcmProperties getFcm() {
        return fcm;
    }
}
