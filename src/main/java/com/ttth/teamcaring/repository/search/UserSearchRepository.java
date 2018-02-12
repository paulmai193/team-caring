/*
 * 
 */
package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.User;

/**
 * Spring Data Elasticsearch repository for the User entity.
 *
 * @author Dai Mai
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
}
