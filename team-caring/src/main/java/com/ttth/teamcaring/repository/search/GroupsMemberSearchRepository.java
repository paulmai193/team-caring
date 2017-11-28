package com.ttth.teamcaring.repository.search;

import com.ttth.teamcaring.domain.GroupsMember;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GroupsMember entity.
 */
public interface GroupsMemberSearchRepository extends ElasticsearchRepository<GroupsMember, Long> {
}
