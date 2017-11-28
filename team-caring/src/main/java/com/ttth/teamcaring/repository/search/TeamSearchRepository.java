package com.ttth.teamcaring.repository.search;

import com.ttth.teamcaring.domain.Team;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Team entity.
 */
public interface TeamSearchRepository extends ElasticsearchRepository<Team, Long> {
}
