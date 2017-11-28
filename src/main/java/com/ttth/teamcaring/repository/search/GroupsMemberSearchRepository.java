package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.GroupsMember;

/**
 * Spring Data Elasticsearch repository for the GroupsMember entity.
 */
public interface GroupsMemberSearchRepository extends ElasticsearchRepository<GroupsMember, Long> {
}
