/*
 * 
 */
package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.Team;

/**
 * Spring Data Elasticsearch repository for the Team entity.
 *
 * @author Dai Mai
 */
public interface TeamSearchRepository extends ElasticsearchRepository<Team, Long> {
}
