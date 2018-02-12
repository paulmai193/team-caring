/*
 * 
 */
package com.ttth.teamcaring.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ttth.teamcaring.security.AuthoritiesConstants;
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.TeamService;
import com.ttth.teamcaring.service.dto.PageDTO;
import com.ttth.teamcaring.service.dto.RequestJoinTeamDTO;
import com.ttth.teamcaring.service.dto.RequestUpdateLevelDTO;
import com.ttth.teamcaring.service.dto.ResponseJoinTeamDTO;
import com.ttth.teamcaring.service.dto.TeamDTO;
import com.ttth.teamcaring.service.dto.TeamDetailDTO;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.util.HeaderUtil;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Team.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class TeamResource {

    /** The Constant ENTITY_NAME. */
    public static final String ENTITY_NAME = "team";

    /** The log. */
    private final Logger       log         = LoggerFactory.getLogger(TeamResource.class);

    /** The team service. */
    private final TeamService  teamService;

    /**
     * Instantiates a new team resource.
     *
     * @param teamService
     *        the team service
     */
    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * POST /team : Create a new team.
     *
     * @param teamDTO
     *        the teamDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     *         new teamDTO, or with status 400 (Bad Request) if the team has
     *         already an ID
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PostMapping("/team")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO)
            throws URISyntaxException {
        log.debug("REST request to save Team : {}", teamDTO);
        if (teamDTO.getId() != null) {
            throw new BadRequestAlertException("A new team cannot already have an ID", ENTITY_NAME,
                    "idexists");
        }
        TeamDTO result = teamService.save(teamDTO);
        return ResponseEntity
                .created(new URI("/api/team/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * POST /create-team : Create a new team for the CURRENT user. (get by
     * current JWT)
     *
     * @param teamDTO
     *        the team DTO
     * @return the ResponseEntity with status 201 (Created) and with body the
     *         new teamDTO, or with status 400 (Bad Request) if the team has
     *         already an ID
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PostMapping("/create-team")
    @Timed
    @ResponseStatus(code = HttpStatus.CREATED)
    public TeamDTO createTeamForCurrentUser(@RequestBody TeamDTO teamDTO)
            throws URISyntaxException {
        log.debug("REST request to save Team : {}", teamDTO);
        if (Objects.nonNull(teamDTO.getId())) {
            throw new BadRequestAlertException("A new team or new group cannot already have an ID",
                    ENTITY_NAME, "idexists");
        }
        TeamDTO result = teamService.create(teamDTO);
        return result;
    }

    /**
     * PUT /team : Updates an existing team.
     *
     * @param teamDTO
     *        the teamDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         teamDTO, or with status 400 (Bad Request) if the teamDTO is not
     *         valid, or with status 500 (Internal Server Error) if the teamDTO
     *         couldn't be updated
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PutMapping("/team")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<TeamDTO> updateTeam(@RequestBody TeamDTO teamDTO)
            throws URISyntaxException {
        log.debug("REST request to update Team : {}", teamDTO);
        if (teamDTO.getId() == null) {
            throw new BadRequestAlertException("A ID of team is required", ENTITY_NAME,
                    "idinvalid");
        }
        TeamDTO result = teamService.save(teamDTO);
        return ResponseEntity.ok()
                .headers(
                        HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teamDTO.getId().toString()))
                .body(result);
    }

    /**
     * PUT /update-team : Updates an existing team for the CURRENT user. (get by
     * current JWT)
     *
     * @param teamDTO
     *        the teamDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         teamDTO, or with status 400 (Bad Request) if the teamDTO is not
     *         valid, or with status 500 (Internal Server Error) if the teamDTO
     *         couldn't be updated
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PutMapping("/update-team")
    @Timed
    @ResponseStatus(code = HttpStatus.OK)
    public TeamDTO updateTeamForCurrentUser(@RequestBody TeamDTO teamDTO)
            throws URISyntaxException {
        log.debug("REST request to update Team : {}", teamDTO);
        if (Objects.isNull(teamDTO.getId())) {
            throw new BadRequestAlertException("A ID of team is required", ENTITY_NAME,
                    "idinvalid");
        }
        TeamDTO result = teamService.update(teamDTO);
        return result;
    }

    /**
     * POST /join-team : The CURRENT user send request join team. (get by
     * current JWT)
     *
     * @param requestJoinTeamDTO
     *        the request join team DTO
     * @return the ResponseEntity with status 200 if success, or with status 400
     *         (Bad Request) if some validation fail
     */
    @PostMapping("/join-team")
    @Timed
    public void requestJoinTeams(@Valid @RequestBody RequestJoinTeamDTO requestJoinTeamDTO) {
        log.debug("REST request to join Team : {}", requestJoinTeamDTO);
        this.teamService.requestJoin(SecurityUtils.getCurrentUserLogin(),
                requestJoinTeamDTO.getTeamId(), requestJoinTeamDTO.getUserId());
    }

    /**
     * POST /join-team/response : The CURRENT user send response join team. (get
     * by current JWT)
     *
     * @param responseJoinTeamDTO
     *        the response join team DTO
     * @return the ResponseEntity with status 200 if success, or with status 400
     *         (Bad Request) if some validation fail
     */
    @PostMapping("/join-team/response")
    @Timed
    public void responseJoinTeams(@Valid @RequestBody ResponseJoinTeamDTO responseJoinTeamDTO) {
        log.debug("REST response join Team : {}", responseJoinTeamDTO);
        this.teamService.responseJoin(SecurityUtils.getCurrentUserLogin(),
                responseJoinTeamDTO.getGroupsMemberId(), responseJoinTeamDTO.getResponse());
    }

    /**
     * GET /team : get all the teams.
     *
     * @param pageable
     *        the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of teams in
     *         body
     */
    @GetMapping("/team")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<TeamDTO>> getAllTeams(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Teams");
        Page<TeamDTO> page = teamService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/team");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /team : search the teams of CURRENT user is joined or is leader.
     *
     * @param query
     *        the query
     * @param pageable
     *        the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of teams in
     *         body
     */
    @GetMapping("/_search/my-team")
    @Timed
    public ResponseEntity<PageDTO<TeamDTO>> searchMyTeams(@RequestParam String query,
            @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of current user teams");
        Page<TeamDTO> page = teamService.searchOfAuthorizedUser(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/team");
        PageDTO<TeamDTO> result = PaginationUtil.generateSearchPaginationDto(page);
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    /**
     * GET /team/:id : get the "id" team.
     *
     * @param id
     *        the id of the teamDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     *         teamDTO, or with status 404 (Not Found)
     */
    @GetMapping("/team/{id}")
    @Timed
    public ResponseEntity<TeamDTO> getTeam(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);
        TeamDTO teamDTO = teamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teamDTO));
    }

    /**
     * GET /team/detail/:id : get the "id" team detail, include all member of
     * this CURRENT user.
     *
     * @param id
     *        the id of the teamDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     *         teamDTO, or with status 404 (Not Found)
     */
    @GetMapping("/team/detail/{id}")
    @Timed
    public ResponseEntity<TeamDetailDTO> getTeamDetailOfAuthorized(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);
        TeamDetailDTO teamDetailDTO = teamService.getTeamDetailOfAuthorizedUser(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teamDetailDTO));
    }

    /**
     * DELETE /team/:id : delete the "id" team.
     *
     * @param id
     *        the id of the teamDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/team/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);
        teamService.delete(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH /_search/team?query=:query : search for the team corresponding to
     * the query.
     *
     * @param query
     *        the query of the team search
     * @param pageable
     *        the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/team")
    @Timed
    public ResponseEntity<List<TeamDTO>> searchTeams(@RequestParam String query,
            @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Teams for query {}", query);
        Page<TeamDTO> page = teamService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
                "/api/_search/team");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * SEARCH /_search/new-team?query=:query : search for the new team
     * corresponding to the query.
     *
     * @param query
     *        the query of the team search
     * @param pageable
     *        the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/new-team")
    @Timed
    public ResponseEntity<PageDTO<TeamDTO>> searchNewTeams(@RequestParam String query,
            @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Teams for query {}", query);
        Page<TeamDTO> page = teamService.searchNewTeam(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
                "/api/_search/new-team");
        PageDTO<TeamDTO> result = PaginationUtil.generateSearchPaginationDto(page);
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    /**
     * PUT /update-level/member : The CURRENT leader update level of a member.
     * (get by current JWT)
     *
     * @param memberDTO
     *        the member DTO
     * @return the ResponseEntity with status 200 if success, or with status 400
     *         (Bad Request) if some validation fail
     */
    @PutMapping("/update-level/member")
    @Timed
    public void updateLevelForMember(@Valid @RequestBody RequestUpdateLevelDTO memberDTO) {
        log.debug("REST request update member level in Team : {}", memberDTO);
        this.teamService.updateMemberLevel(SecurityUtils.getCurrentUserLogin(), memberDTO.getId(),
                memberDTO.getLevel());
    }

    /**
     * PUT /update-level/team : The CURRENT leader update level of the team.
     * (get by current JWT)
     *
     * @param memberDTO
     *        the member DTO
     * @return the ResponseEntity with status 200 if success, or with status 400
     *         (Bad Request) if some validation fail
     */
    @PutMapping("/update-level/team")
    @Timed
    public void updateLevelForTeam(@Valid @RequestBody RequestUpdateLevelDTO memberDTO) {
        log.debug("REST request update member level in Team : {}", memberDTO);
        this.teamService.updateTeamLevel(SecurityUtils.getCurrentUserLogin(), memberDTO.getId(),
                memberDTO.getLevel());
    }

}
