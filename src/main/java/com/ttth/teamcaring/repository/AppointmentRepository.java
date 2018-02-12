/*
 * 
 */
package com.ttth.teamcaring.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.CustomUser;

/**
 * Spring Data JPA repository for the Appointment entity.
 *
 * @author Dai Mai
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Find by custom user and time between.
     *
     * @param customUser the custom user
     * @param fromDate the from date
     * @param toDate the to date
     * @return the list
     */
    List<Appointment> findByCustomUserAndTimeBetween(CustomUser customUser, Instant fromDate,
            Instant toDate);

    /**
     * Find by attendee and time between.
     *
     * @param customUser the custom user
     * @param fromDate the from date
     * @param toDate the to date
     * @return the list
     */
    @Query("select app from Appointment app join Attendee att on app.id = att.appointment.id "
            + "where att.customUser = ?1 and att.appointment.time between ?2 and ?3")
    List<Appointment> findByAttendeeAndTimeBetween(CustomUser customUser, Instant fromDate,
            Instant toDate);

    /**
     * Find by attendee.
     *
     * @param customUser the custom user
     * @return the list
     */
    @Query("select app from Appointment app join Attendee att on app.id = att.appointment.id "
            + "where att.customUser = ?1")
    List<Appointment> findByAttendee(CustomUser customUser);

}
