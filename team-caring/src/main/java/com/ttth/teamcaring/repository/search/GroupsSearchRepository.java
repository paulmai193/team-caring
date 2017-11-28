package com.ttth.teamcaring.repository.search;

import com.ttth.teamcaring.domain.Groups;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Groups entity.
 */
public interface GroupsSearchRepository extends ElasticsearchRepository<Groups, Long> {
}
