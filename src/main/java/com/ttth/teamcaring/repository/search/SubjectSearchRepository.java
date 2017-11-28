package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.Subject;

/**
 * Spring Data Elasticsearch repository for the Subject entity.
 */
public interface SubjectSearchRepository extends ElasticsearchRepository<Subject, Long> {
}
