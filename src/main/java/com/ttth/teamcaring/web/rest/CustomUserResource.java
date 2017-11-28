package com.ttth.teamcaring.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.util.HeaderUtil;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing CustomUser.
 */
@RestController
@RequestMapping("/api")
public class CustomUserResource {

    private final Logger log = LoggerFactory.getLogger(CustomUserResource.class);

    private static final String ENTITY_NAME = "customUser";

    private final CustomUserService customUserService;

    public CustomUserResource(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    /**
     * POST  /custom-users : Create a new customUser.
     *
     * @param customUserDTO the customUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customUserDTO, or with status 400 (Bad Request) if the customUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/custom-users")
    @Timed
    public ResponseEntity<CustomUserDTO> createCustomUser(@RequestBody CustomUserDTO customUserDTO) throws URISyntaxException {
        log.debug("REST request to save CustomUser : {}", customUserDTO);
        if (customUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new customUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomUserDTO result = customUserService.save(customUserDTO);
        return ResponseEntity.created(new URI("/api/custom-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /custom-users : Updates an existing customUser.
     *
     * @param customUserDTO the customUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customUserDTO,
     * or with status 400 (Bad Request) if the customUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the customUserDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/custom-users")
    @Timed
    public ResponseEntity<CustomUserDTO> updateCustomUser(@RequestBody CustomUserDTO customUserDTO) throws URISyntaxException {
        log.debug("REST request to update CustomUser : {}", customUserDTO);
        if (customUserDTO.getId() == null) {
            return createCustomUser(customUserDTO);
        }
        CustomUserDTO result = customUserService.save(customUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /custom-users : get all the customUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customUsers in body
     */
    @GetMapping("/custom-users")
    @Timed
    public ResponseEntity<List<CustomUserDTO>> getAllCustomUsers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CustomUsers");
        Page<CustomUserDTO> page = customUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/custom-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /custom-users/:id : get the "id" customUser.
     *
     * @param id the id of the customUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/custom-users/{id}")
    @Timed
    public ResponseEntity<CustomUserDTO> getCustomUser(@PathVariable Long id) {
        log.debug("REST request to get CustomUser : {}", id);
        CustomUserDTO customUserDTO = customUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customUserDTO));
    }

    /**
     * DELETE  /custom-users/:id : delete the "id" customUser.
     *
     * @param id the id of the customUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/custom-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomUser(@PathVariable Long id) {
        log.debug("REST request to delete CustomUser : {}", id);
        customUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/custom-users?query=:query : search for the customUser corresponding
     * to the query.
     *
     * @param query the query of the customUser search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/custom-users")
    @Timed
    public ResponseEntity<List<CustomUserDTO>> searchCustomUsers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CustomUsers for query {}", query);
        Page<CustomUserDTO> page = customUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/custom-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
