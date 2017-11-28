package com.ttth.teamcaring.repository.search;

import com.ttth.teamcaring.domain.CustomUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CustomUser entity.
 */
public interface CustomUserSearchRepository extends ElasticsearchRepository<CustomUser, Long> {
}
