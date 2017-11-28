package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.Groups;

/**
 * Spring Data Elasticsearch repository for the Groups entity.
 */
public interface GroupsSearchRepository extends ElasticsearchRepository<Groups, Long> {
}
