/*
 * 
 */
package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.Notification;

/**
 * Spring Data Elasticsearch repository for the Notification entity.
 *
 * @author Dai Mai
 */
public interface NotificationSearchRepository extends ElasticsearchRepository<Notification, Long> {
}
