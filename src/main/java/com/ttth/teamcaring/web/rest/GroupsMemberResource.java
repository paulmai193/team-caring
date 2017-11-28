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
import com.ttth.teamcaring.service.GroupsMemberService;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.util.HeaderUtil;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing GroupsMember.
 */
@RestController
@RequestMapping("/api")
public class GroupsMemberResource {

    private final Logger log = LoggerFactory.getLogger(GroupsMemberResource.class);

    private static final String ENTITY_NAME = "groupsMember";

    private final GroupsMemberService groupsMemberService;

    public GroupsMemberResource(GroupsMemberService groupsMemberService) {
        this.groupsMemberService = groupsMemberService;
    }

    /**
     * POST  /groups-members : Create a new groupsMember.
     *
     * @param groupsMemberDTO the groupsMemberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupsMemberDTO, or with status 400 (Bad Request) if the groupsMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groups-members")
    @Timed
    public ResponseEntity<GroupsMemberDTO> createGroupsMember(@RequestBody GroupsMemberDTO groupsMemberDTO) throws URISyntaxException {
        log.debug("REST request to save GroupsMember : {}", groupsMemberDTO);
        if (groupsMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new groupsMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GroupsMemberDTO result = groupsMemberService.save(groupsMemberDTO);
        return ResponseEntity.created(new URI("/api/groups-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups-members : Updates an existing groupsMember.
     *
     * @param groupsMemberDTO the groupsMemberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupsMemberDTO,
     * or with status 400 (Bad Request) if the groupsMemberDTO is not valid,
     * or with status 500 (Internal Server Error) if the groupsMemberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups-members")
    @Timed
    public ResponseEntity<GroupsMemberDTO> updateGroupsMember(@RequestBody GroupsMemberDTO groupsMemberDTO) throws URISyntaxException {
        log.debug("REST request to update GroupsMember : {}", groupsMemberDTO);
        if (groupsMemberDTO.getId() == null) {
            return createGroupsMember(groupsMemberDTO);
        }
        GroupsMemberDTO result = groupsMemberService.save(groupsMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, groupsMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups-members : get all the groupsMembers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of groupsMembers in body
     */
    @GetMapping("/groups-members")
    @Timed
    public ResponseEntity<List<GroupsMemberDTO>> getAllGroupsMembers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of GroupsMembers");
        Page<GroupsMemberDTO> page = groupsMemberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groups-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /groups-members/:id : get the "id" groupsMember.
     *
     * @param id the id of the groupsMemberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupsMemberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/groups-members/{id}")
    @Timed
    public ResponseEntity<GroupsMemberDTO> getGroupsMember(@PathVariable Long id) {
        log.debug("REST request to get GroupsMember : {}", id);
        GroupsMemberDTO groupsMemberDTO = groupsMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(groupsMemberDTO));
    }

    /**
     * DELETE  /groups-members/:id : delete the "id" groupsMember.
     *
     * @param id the id of the groupsMemberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groups-members/{id}")
    @Timed
    public ResponseEntity<Void> deleteGroupsMember(@PathVariable Long id) {
        log.debug("REST request to delete GroupsMember : {}", id);
        groupsMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/groups-members?query=:query : search for the groupsMember corresponding
     * to the query.
     *
     * @param query the query of the groupsMember search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/groups-members")
    @Timed
    public ResponseEntity<List<GroupsMemberDTO>> searchGroupsMembers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of GroupsMembers for query {}", query);
        Page<GroupsMemberDTO> page = groupsMemberService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/groups-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
