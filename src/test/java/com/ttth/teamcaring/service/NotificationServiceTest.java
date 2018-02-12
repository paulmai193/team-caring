/*
 * 
 */
package com.ttth.teamcaring.service;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttth.teamcaring.TeamCaringApp;
import com.ttth.teamcaring.service.dto.AnwserResponseStatus;
import com.ttth.teamcaring.service.dto.ResponseJoinTeamDTO;

/**
 * The Class NotificationServiceTest.
 *
 * @author Dai Mai
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
public class NotificationServiceTest {

    /** The custom user id. */
    public final Long          CUSTOM_USER_ID  = 1L;

    /** The group member id. */
    public final Long          GROUP_MEMBER_ID = 1L;

    /** The notification service. */
    @Autowired
    public NotificationService notificationService;

    /**
     * Generate request join team test.
     */
    @Test
    public void generateRequestJoinTeamTest() {
        String metaData;
        try {
            metaData = new ObjectMapper().writeValueAsString(
                    new ResponseJoinTeamDTO().groupsMemberId(this.GROUP_MEMBER_ID));
        }
        catch (JsonProcessingException e) {
            metaData = "";
        }
        System.out.println(metaData);
        assertNotEquals(metaData, "");
    }

    /**
     * Generate response join team test.
     */
    @Test
    public void generateResponseJoinTeamTest() {
        String metaData;
        try {
            metaData = new ObjectMapper().writeValueAsString(new ResponseJoinTeamDTO()
                    .groupsMemberId(this.GROUP_MEMBER_ID).response(AnwserResponseStatus.accept));
        }
        catch (JsonProcessingException e) {
            metaData = "";
        }
        System.out.println(metaData);
        assertNotEquals(metaData, "");
    }

}
