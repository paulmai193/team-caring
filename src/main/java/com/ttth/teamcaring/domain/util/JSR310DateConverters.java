/*
 * 
 */
package com.ttth.teamcaring.domain.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * The Class JSR310DateConverters.
 *
 * @author Dai Mai
 */
public final class JSR310DateConverters {

    /**
     * Instantiates a new JSR 310 date converters.
     */
    private JSR310DateConverters() {
    }

    /**
     * The Class LocalDateToDateConverter.
     *
     * @author Dai Mai
     */
    public static class LocalDateToDateConverter implements Converter<LocalDate, Date> {

        /** The Constant INSTANCE. */
        public static final LocalDateToDateConverter INSTANCE = new LocalDateToDateConverter();

        /**
         * Instantiates a new local date to date converter.
         */
        private LocalDateToDateConverter() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.core.convert.converter.Converter#convert(java.
         * lang.Object)
         */
        @Override
        public Date convert(LocalDate source) {
            return source == null ? null
                    : Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    /**
     * The Class DateToLocalDateConverter.
     *
     * @author Dai Mai
     */
    public static class DateToLocalDateConverter implements Converter<Date, LocalDate> {

        /** The Constant INSTANCE. */
        public static final DateToLocalDateConverter INSTANCE = new DateToLocalDateConverter();

        /**
         * Instantiates a new date to local date converter.
         */
        private DateToLocalDateConverter() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.core.convert.converter.Converter#convert(java.
         * lang.Object)
         */
        @Override
        public LocalDate convert(Date source) {
            return source == null ? null
                    : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault())
                            .toLocalDate();
        }
    }

    /**
     * The Class ZonedDateTimeToDateConverter.
     *
     * @author Dai Mai
     */
    public static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

        /** The Constant INSTANCE. */
        public static final ZonedDateTimeToDateConverter INSTANCE = new ZonedDateTimeToDateConverter();

        /**
         * Instantiates a new zoned date time to date converter.
         */
        private ZonedDateTimeToDateConverter() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.core.convert.converter.Converter#convert(java.
         * lang.Object)
         */
        @Override
        public Date convert(ZonedDateTime source) {
            return source == null ? null : Date.from(source.toInstant());
        }
    }

    /**
     * The Class DateToZonedDateTimeConverter.
     *
     * @author Dai Mai
     */
    public static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

        /** The Constant INSTANCE. */
        public static final DateToZonedDateTimeConverter INSTANCE = new DateToZonedDateTimeConverter();

        /**
         * Instantiates a new date to zoned date time converter.
         */
        private DateToZonedDateTimeConverter() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.core.convert.converter.Converter#convert(java.
         * lang.Object)
         */
        @Override
        public ZonedDateTime convert(Date source) {
            return source == null ? null
                    : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    /**
     * The Class LocalDateTimeToDateConverter.
     *
     * @author Dai Mai
     */
    public static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

        /** The Constant INSTANCE. */
        public static final LocalDateTimeToDateConverter INSTANCE = new LocalDateTimeToDateConverter();

        /**
         * Instantiates a new local date time to date converter.
         */
        private LocalDateTimeToDateConverter() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.core.convert.converter.Converter#convert(java.
         * lang.Object)
         */
        @Override
        public Date convert(LocalDateTime source) {
            return source == null ? null
                    : Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    /**
     * The Class DateToLocalDateTimeConverter.
     *
     * @author Dai Mai
     */
    public static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

        /** The Constant INSTANCE. */
        public static final DateToLocalDateTimeConverter INSTANCE = new DateToLocalDateTimeConverter();

        /**
         * Instantiates a new date to local date time converter.
         */
        private DateToLocalDateTimeConverter() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.springframework.core.convert.converter.Converter#convert(java.
         * lang.Object)
         */
        @Override
        public LocalDateTime convert(Date source) {
            return source == null ? null
                    : LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }
}
