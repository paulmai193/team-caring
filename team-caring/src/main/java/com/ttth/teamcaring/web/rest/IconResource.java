package com.ttth.teamcaring.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ttth.teamcaring.service.IconService;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.util.HeaderUtil;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;
import com.ttth.teamcaring.service.dto.IconDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Icon.
 */
@RestController
@RequestMapping("/api")
public class IconResource {

    private final Logger log = LoggerFactory.getLogger(IconResource.class);

    private static final String ENTITY_NAME = "icon";

    private final IconService iconService;

    public IconResource(IconService iconService) {
        this.iconService = iconService;
    }

    /**
     * POST  /icons : Create a new icon.
     *
     * @param iconDTO the iconDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new iconDTO, or with status 400 (Bad Request) if the icon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/icons")
    @Timed
    public ResponseEntity<IconDTO> createIcon(@RequestBody IconDTO iconDTO) throws URISyntaxException {
        log.debug("REST request to save Icon : {}", iconDTO);
        if (iconDTO.getId() != null) {
            throw new BadRequestAlertException("A new icon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IconDTO result = iconService.save(iconDTO);
        return ResponseEntity.created(new URI("/api/icons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /icons : Updates an existing icon.
     *
     * @param iconDTO the iconDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated iconDTO,
     * or with status 400 (Bad Request) if the iconDTO is not valid,
     * or with status 500 (Internal Server Error) if the iconDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/icons")
    @Timed
    public ResponseEntity<IconDTO> updateIcon(@RequestBody IconDTO iconDTO) throws URISyntaxException {
        log.debug("REST request to update Icon : {}", iconDTO);
        if (iconDTO.getId() == null) {
            return createIcon(iconDTO);
        }
        IconDTO result = iconService.save(iconDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, iconDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /icons : get all the icons.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of icons in body
     */
    @GetMapping("/icons")
    @Timed
    public ResponseEntity<List<IconDTO>> getAllIcons(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Icons");
        Page<IconDTO> page = iconService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/icons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /icons/:id : get the "id" icon.
     *
     * @param id the id of the iconDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the iconDTO, or with status 404 (Not Found)
     */
    @GetMapping("/icons/{id}")
    @Timed
    public ResponseEntity<IconDTO> getIcon(@PathVariable Long id) {
        log.debug("REST request to get Icon : {}", id);
        IconDTO iconDTO = iconService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(iconDTO));
    }

    /**
     * DELETE  /icons/:id : delete the "id" icon.
     *
     * @param id the id of the iconDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/icons/{id}")
    @Timed
    public ResponseEntity<Void> deleteIcon(@PathVariable Long id) {
        log.debug("REST request to delete Icon : {}", id);
        iconService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/icons?query=:query : search for the icon corresponding
     * to the query.
     *
     * @param query the query of the icon search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/icons")
    @Timed
    public ResponseEntity<List<IconDTO>> searchIcons(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Icons for query {}", query);
        Page<IconDTO> page = iconService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/icons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
