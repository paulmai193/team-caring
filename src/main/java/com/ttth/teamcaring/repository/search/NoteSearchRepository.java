/*
 * 
 */
package com.ttth.teamcaring.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ttth.teamcaring.domain.Note;

/**
 * Spring Data Elasticsearch repository for the Note entity.
 *
 * @author Dai Mai
 */
public interface NoteSearchRepository extends ElasticsearchRepository<Note, Long> {
}
