/*
 * 
 */
package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.Attendee;

/**
 * Spring Data Elasticsearch repository for the Attendee entity.
 *
 * @author Dai Mai
 */
public interface AttendeeSearchRepository extends ElasticsearchRepository<Attendee, Long> {
}
