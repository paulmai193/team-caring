/*
 * 
 */
package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.Icon;

/**
 * Spring Data Elasticsearch repository for the Icon entity.
 *
 * @author Dai Mai
 */
public interface IconSearchRepository extends ElasticsearchRepository<Icon, Long> {
}
