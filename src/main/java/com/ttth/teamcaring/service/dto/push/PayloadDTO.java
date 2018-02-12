/*
 * 
 */
package com.ttth.teamcaring.service.dto.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class PayloadDTO.
 *
 * @author Dai Mai
 */
public class PayloadDTO implements Serializable {

    /** The notification. */
    private PayloadNotificationDTO notification;

    /** The data. */
    private BasePayloadDataDTO     data;

    /** The registration ids. */
    private List<String>           registration_ids = new ArrayList<>();

    /** The priority. */
    private String                 priority;

    /**
     * Instantiates a new payload DTO.
     */
    public PayloadDTO() {
        this.priority = "high";
    }

    /**
     * Gets the registration ids.
     *
     * @return the registration ids
     */
    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    /**
     * Sets the registration ids.
     *
     * @param registration_ids
     *        the new registration ids
     */
    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    /**
     * Gets the notification.
     *
     * @return the notification
     */
    public PayloadNotificationDTO getNotification() {
        return notification;
    }

    /**
     * Sets the notification.
     *
     * @param notification
     *        the new notification
     */
    public void setNotification(PayloadNotificationDTO notification) {
        this.notification = notification;
    }

    /**
     * Notification.
     *
     * @param notification
     *        the notification
     * @return the payload DTO
     */
    public PayloadDTO notification(PayloadNotificationDTO notification) {
        this.setNotification(notification);
        return this;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public BasePayloadDataDTO getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *        the new data
     */
    public void setData(BasePayloadDataDTO data) {
        this.data = data;
    }

    /**
     * Data.
     *
     * @param data
     *        the data
     * @return the payload DTO
     */
    public PayloadDTO data(BasePayloadDataDTO data) {
        this.setData(data);
        return this;
    }

    /**
     * Sets the to.
     *
     * @param to
     *        the new to
     */
    public void setTo(String to) {
        registration_ids.add(to);
    }

    /**
     * To.
     *
     * @param to
     *        the to
     * @return the payload DTO
     */
    public PayloadDTO to(String to) {
        this.setTo(to);
        return this;
    }

    /**
     * Gets the priority.
     *
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the priority.
     *
     * @param priority
     *        the new priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Priority.
     *
     * @param priority
     *        the priority
     * @return the payload DTO
     */
    public PayloadDTO priority(String priority) {
        this.setPriority(priority);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PayloadDTO [notification=" + notification + ", data=" + data + ", registration_ids="
                + registration_ids + ", priority=" + priority + "]";
    }

}
