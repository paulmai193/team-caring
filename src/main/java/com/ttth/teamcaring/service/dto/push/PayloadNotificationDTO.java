/*
 * 
 */
package com.ttth.teamcaring.service.dto.push;

import java.io.Serializable;

/**
 * The Class PayloadNotificationDTO.
 *
 * @author Dai Mai
 */
public class PayloadNotificationDTO implements Serializable {

    /** The title. */
    private String title;

    /** The body. */
    private String body;

    /** The sound. */
    private String sound;

    /** The icon. */
    private String icon;

    /**
     * Instantiates a new payload notification DTO.
     *
     * @param title
     *        the title
     * @param body
     *        the body
     * @param icon
     *        the icon
     */
    public PayloadNotificationDTO(String title, String body, String icon) {
        super();
        this.title = title;
        this.body = body;
        this.sound = "default";
        this.icon = icon;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *        the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body.
     *
     * @param body
     *        the new body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Gets the sound.
     *
     * @return the sound
     */
    public String getSound() {
        return sound;
    }

    /**
     * Sets the sound.
     *
     * @param sound
     *        the new sound
     */
    public void setSound(String sound) {
        this.sound = sound;
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon
     *        the new icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PayloadNotificationDTO [title=" + title + ", body=" + body + ", sound=" + sound
                + ", icon=" + icon + "]";
    }

}
