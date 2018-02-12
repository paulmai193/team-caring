/*
 * 
 */
package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.CustomUser;

/**
 * Spring Data Elasticsearch repository for the CustomUser entity.
 *
 * @author Dai Mai
 */
public interface CustomUserSearchRepository extends ElasticsearchRepository<CustomUser, Long> {
}
