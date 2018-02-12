/*
 * 
 */
package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.Appointment;

/**
 * Spring Data Elasticsearch repository for the Appointment entity.
 *
 * @author Dai Mai
 */
public interface AppointmentSearchRepository extends ElasticsearchRepository<Appointment, Long> {
}
