package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.domain.Sprint;
import com.shcherbyna.jira.service.SprintService;
import com.shcherbyna.jira.web.rest.errors.BadRequestAlertException;
import com.shcherbyna.jira.service.dto.SprintCriteria;
import com.shcherbyna.jira.service.SprintQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.shcherbyna.jira.domain.Sprint}.
 */
@RestController
@RequestMapping("/api")
public class SprintResource {

    private final Logger log = LoggerFactory.getLogger(SprintResource.class);

    private static final String ENTITY_NAME = "sprint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SprintService sprintService;

    private final SprintQueryService sprintQueryService;

    public SprintResource(SprintService sprintService, SprintQueryService sprintQueryService) {
        this.sprintService = sprintService;
        this.sprintQueryService = sprintQueryService;
    }

    /**
     * {@code POST  /sprints} : Create a new sprint.
     *
     * @param sprint the sprint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sprint, or with status {@code 400 (Bad Request)} if the sprint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sprints")
    public ResponseEntity<Sprint> createSprint(@Valid @RequestBody Sprint sprint) throws URISyntaxException {
        log.debug("REST request to save Sprint : {}", sprint);
        if (sprint.getId() != null) {
            throw new BadRequestAlertException("A new sprint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sprint result = sprintService.save(sprint);
        return ResponseEntity.created(new URI("/api/sprints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sprints} : Updates an existing sprint.
     *
     * @param sprint the sprint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sprint,
     * or with status {@code 400 (Bad Request)} if the sprint is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sprint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sprints")
    public ResponseEntity<Sprint> updateSprint(@Valid @RequestBody Sprint sprint) throws URISyntaxException {
        log.debug("REST request to update Sprint : {}", sprint);
        if (sprint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sprint result = sprintService.save(sprint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sprint.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sprints} : get all the sprints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sprints in body.
     */
    @GetMapping("/sprints")
    public ResponseEntity<List<Sprint>> getAllSprints(SprintCriteria criteria) {
        log.debug("REST request to get Sprints by criteria: {}", criteria);
        List<Sprint> entityList = sprintQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sprints/count} : count all the sprints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sprints/count")
    public ResponseEntity<Long> countSprints(SprintCriteria criteria) {
        log.debug("REST request to count Sprints by criteria: {}", criteria);
        return ResponseEntity.ok().body(sprintQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sprints/:id} : get the "id" sprint.
     *
     * @param id the id of the sprint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sprint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sprints/{id}")
    public ResponseEntity<Sprint> getSprint(@PathVariable Long id) {
        log.debug("REST request to get Sprint : {}", id);
        Optional<Sprint> sprint = sprintService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sprint);
    }

    /**
     * {@code DELETE  /sprints/:id} : delete the "id" sprint.
     *
     * @param id the id of the sprint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sprints/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        log.debug("REST request to delete Sprint : {}", id);
        sprintService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
