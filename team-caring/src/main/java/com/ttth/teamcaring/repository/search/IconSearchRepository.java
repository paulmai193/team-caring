package com.ttth.teamcaring.repository.search;

import com.ttth.teamcaring.domain.Icon;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Icon entity.
 */
public interface IconSearchRepository extends ElasticsearchRepository<Icon, Long> {
}
