/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;

/**
 * The Class TimeMapper.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring")
public interface TimeMapper {

    /** The default timezone. */
    static ZoneId defaultTimezone   = ZoneId.of("Asia/Ho_Chi_Minh");

    /** The default time format. */
    static String defaultTimeFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * Map time instant to string.
     *
     * @param timeInstant
     *        the time instant
     * @return the time string
     */
    default String mapInstantToString(Instant timeInstant) {
        return this.getDateTimeFormatter(defaultTimeFormat, defaultTimezone).format(timeInstant);
    }

    /**
     * Map time string to instant.
     *
     * @param timeString
     *        the time string
     * @return the time instant
     */
    default Instant mapStringToInstant(String timeString) {
        LocalDateTime localDate = LocalDateTime.parse(timeString,
                this.getDateTimeFormatter(defaultTimeFormat));
        return localDate.atZone(defaultTimezone).toInstant();
    }

    /**
     * Gets the date time formatter.
     *
     * @param timeFormat
     *        the time format
     * @param timezone
     *        the timezone
     * @return the date time formatter
     */
    default DateTimeFormatter getDateTimeFormatter(String timeFormat, ZoneId timezone) {
        return DateTimeFormatter.ofPattern(timeFormat).withZone(timezone);
    }

    /**
     * Gets the date time formatter.
     *
     * @param timeFormat
     *        the time format
     * @return the date time formatter
     */
    default DateTimeFormatter getDateTimeFormatter(String timeFormat) {
        return DateTimeFormatter.ofPattern(timeFormat);
    }
}
