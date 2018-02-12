/*
 * 
 */
package com.ttth.teamcaring.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ttth.teamcaring.TeamCaringApp;
import com.ttth.teamcaring.web.rest.vm.LoggerVM;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;

/**
 * Test class for the LogsResource REST controller.
 *
 * @see LogsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Ignore
public class LogsResourceIntTest {

    /** The rest logs mock mvc. */
    private MockMvc restLogsMockMvc;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        LogsResource logsResource = new LogsResource();
        this.restLogsMockMvc = MockMvcBuilders.standaloneSetup(logsResource).build();
    }

    /**
     * Gets the all logs.
     *
     * @return the all logs
     * @throws Exception
     *         the exception
     */
    @Test
    public void getAllLogs() throws Exception {
        restLogsMockMvc.perform(get("/management/logs")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    /**
     * Change logs.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void changeLogs() throws Exception {
        LoggerVM logger = new LoggerVM();
        logger.setLevel("INFO");
        logger.setName("ROOT");

        restLogsMockMvc
                .perform(put("/management/logs").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(logger)))
                .andExpect(status().isNoContent());
    }

    /**
     * Test logstash appender.
     */
    @Test
    public void testLogstashAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        assertThat(context.getLogger("ROOT").getAppender("ASYNC_LOGSTASH"))
                .isInstanceOf(AsyncAppender.class);
    }
}
