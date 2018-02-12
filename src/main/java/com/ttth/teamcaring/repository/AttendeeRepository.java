/*
 * 
 */
package com.ttth.teamcaring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.domain.CustomUser;

/**
 * Spring Data JPA repository for the Attendee entity.
 *
 * @author Dai Mai
 */
@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    /**
     * Find one by appointment and custom user.
     *
     * @param appointment
     *        the appointment
     * @param customUser
     *        the custom user
     * @return the optional
     */
    Optional<Attendee> findOneByAppointmentAndCustomUser(Appointment appointment,
            CustomUser customUser);

    /**
     * Delete by appointment id and status not.
     *
     * @param appointmentId the appointment id
     * @param status the status
     * @return the list
     */
    List<Attendee> deleteByAppointmentIdAndStatusNot(Long appointmentId, int status);
    
}
