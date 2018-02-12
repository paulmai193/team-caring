/*
 * 
 */
package com.ttth.teamcaring.repository;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.TeamCaringApp;
import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.Team;

/**
 * The Class AppointmentRepositoryTest.
 *
 * @author Dai Mai
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Transactional
public class AppointmentRepositoryTest {
    
    /** The appointment repository. */
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    /** The team repository. */
    @Autowired
    private TeamRepository teamRepository;
    
    /** The attendee repository. */
    @Autowired
    private AttendeeRepository attendeeRepository;
    
    /** The custom user repository. */
    @Autowired
    private CustomUserRepository customUserRepository;
    
    /** The custom user 1. */
    private CustomUser customUser1;
    
    /** The custom user 2. */
    private CustomUser customUser2;
    
    /** The from. */
    private Instant from = Instant.now().minus(Duration.ofDays(10));
    
    /** The to. */
    private Instant to = Instant.now().plus(Duration.ofDays(10));    
    
    /**
     * Before.
     */
    @Before
    public void before() {
        // Setup custom user
        this.customUser1 = this.customUserRepository.getOne(1L);
        this.customUser2 = this.customUserRepository.getOne(2L);
        
        // Setup a team
        Team team = new Team().name("Team");
        team = this.teamRepository.save(team);
        
        // Setup 2 appointments
        Appointment app1 = new Appointment().customUser(customUser1).name("App 1").team(team).time(Instant.now());
        Appointment app2 = new Appointment().customUser(customUser2).name("App 2").team(team).time(Instant.now());
        app1 = this.appointmentRepository.save(app1);
        app2 = this.appointmentRepository.save(app2);
        
        // Setup 2 attendees of customUser 1 for appointment 2
        Attendee att1 = new Attendee().appointment(app2).customUser(customUser1).status(Attendee.STATUS_ACCEPTED);
        att1 = this.attendeeRepository.save(att1);
        Attendee att2 = new Attendee().appointment(app2).customUser(customUser1).status(Attendee.STATUS_ACCEPTED);
        att2 = this.attendeeRepository.save(att2);
    }
    
    /**
     * Test setup success.
     */
    @Test
    public void testSetupSuccess() {
        List<Appointment> apps = this.appointmentRepository.findAll();
        assertEquals(2, apps.size());
        
        List<Attendee> atts = this.attendeeRepository.findAll();
        assertEquals(2, atts.size());
    }

    /**
     * Test find creator.
     */
    @Test
    public void testFindCreator() {        
        List<Appointment> list = this.appointmentRepository.findByCustomUserAndTimeBetween(customUser1, from, to);
        assertEquals(1, list.size());
    }
    
    /**
     * Test find attendee.
     */
    @Test
    public void testFindAttendee() {        
        List<Appointment> list = this.appointmentRepository.findByAttendeeAndTimeBetween(customUser1, from, to);
        assertEquals(2, list.size());
    }

}
