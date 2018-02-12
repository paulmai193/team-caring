/*
 * 
 */
package com.ttth.teamcaring.client;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ttth.teamcaring.service.dto.push.FcmResponseDTO;
import com.ttth.teamcaring.service.dto.push.PayloadDTO;

/**
 * The Interface SendPushNotificationClient.
 *
 * @author Dai Mai
 */
@FeignClient(name = SendPushNotificationClient.PROVIDER_NAME, url = "${application.fcm.url}")
public interface SendPushNotificationClient {

    /** The Constant PROVIDER_NAME. */
    static final String PROVIDER_NAME = "fcm";

    /**
     * Send.
     *
     * @param keyToken        the key token
     * @param payload        the payload
     * @return the fcm response DTO
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FcmResponseDTO send(@RequestHeader("Authorization") String keyToken,
            @Valid @RequestBody PayloadDTO payload);
}
