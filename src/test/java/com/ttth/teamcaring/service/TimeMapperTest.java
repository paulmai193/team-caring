/*
 * 
 */
package com.ttth.teamcaring.service;

import static org.junit.Assert.assertNotNull;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ttth.teamcaring.TeamCaringApp;
import com.ttth.teamcaring.service.mapper.TimeMapper;

/**
 * The Class TimeMapperTest.
 *
 * @author Dai Mai
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
public class TimeMapperTest {

    /** The time mapper. */
    @Autowired
    public TimeMapper timeMapper;

    /**
     * Map string to instant test.
     */
    @Test
    public void mapStringToInstantTest() {
        String timeString = "2017-12-20 10:50:49";
        Instant instant = this.timeMapper.mapStringToInstant(timeString);
        assertNotNull(instant);
        System.out.println(instant);
    }
}
