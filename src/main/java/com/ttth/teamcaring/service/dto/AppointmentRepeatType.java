/*
 * 
 */
package com.ttth.teamcaring.service.dto;

/**
 * The Enum AppointmentRepeatType.
 *
 * @author Dai Mai
 */
public enum AppointmentRepeatType {

    /** The no repeat. */
    no_repeat(0),

    /** The one week. */
    one_week(1),

    /** The two week. */
    two_week(2),

    /** The one month. */
    one_month(3);

    /** The value. */
    private int value;

    /**
     * Instantiates a new appointment repeat type.
     *
     * @param value
     *        the value
     */
    AppointmentRepeatType(int value) {
        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Gets the type by value.
     *
     * @param value the value
     * @return the type by value
     */
    public static AppointmentRepeatType getTypeByValue(int value) {
        if (value == AppointmentRepeatType.no_repeat.getValue()) {
            return AppointmentRepeatType.no_repeat;
        } else if (value == AppointmentRepeatType.one_month.getValue()) {
            return AppointmentRepeatType.one_month;
        } else if (value == AppointmentRepeatType.one_week.getValue()) {
            return AppointmentRepeatType.one_week;
        } else if (value == AppointmentRepeatType.two_week.getValue()) {
            return AppointmentRepeatType.two_week;
        } else {
            return null;
        }
    }

}
