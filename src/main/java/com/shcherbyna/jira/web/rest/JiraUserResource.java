package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.domain.JiraUser;
import com.shcherbyna.jira.service.JiraUserService;
import com.shcherbyna.jira.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.shcherbyna.jira.domain.JiraUser}.
 */
@RestController
@RequestMapping("/api")
public class JiraUserResource {

    private final Logger log = LoggerFactory.getLogger(JiraUserResource.class);

    private static final String ENTITY_NAME = "jiraUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JiraUserService jiraUserService;

    public JiraUserResource(JiraUserService jiraUserService) {
        this.jiraUserService = jiraUserService;
    }

    /**
     * {@code POST  /jira-users} : Create a new jiraUser.
     *
     * @param jiraUser the jiraUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jiraUser, or with status {@code 400 (Bad Request)} if the jiraUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/jira-users")
    public ResponseEntity<JiraUser> createJiraUser(@Valid @RequestBody JiraUser jiraUser) throws URISyntaxException {
        log.debug("REST request to save JiraUser : {}", jiraUser);
        if (jiraUser.getId() != null) {
            throw new BadRequestAlertException("A new jiraUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JiraUser result = jiraUserService.save(jiraUser);
        return ResponseEntity.created(new URI("/api/jira-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /jira-users} : Updates an existing jiraUser.
     *
     * @param jiraUser the jiraUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jiraUser,
     * or with status {@code 400 (Bad Request)} if the jiraUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jiraUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/jira-users")
    public ResponseEntity<JiraUser> updateJiraUser(@Valid @RequestBody JiraUser jiraUser) throws URISyntaxException {
        log.debug("REST request to update JiraUser : {}", jiraUser);
        if (jiraUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JiraUser result = jiraUserService.save(jiraUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jiraUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /jira-users} : get all the jiraUsers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jiraUsers in body.
     */
    @GetMapping("/jira-users")
    public List<JiraUser> getAllJiraUsers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all JiraUsers");
        return jiraUserService.findAll();
    }

    /**
     * {@code GET  /jira-users/:id} : get the "id" jiraUser.
     *
     * @param id the id of the jiraUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jiraUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jira-users/{id}")
    public ResponseEntity<JiraUser> getJiraUser(@PathVariable Long id) {
        log.debug("REST request to get JiraUser : {}", id);
        Optional<JiraUser> jiraUser = jiraUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jiraUser);
    }

    /**
     * {@code DELETE  /jira-users/:id} : delete the "id" jiraUser.
     *
     * @param id the id of the jiraUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/jira-users/{id}")
    public ResponseEntity<Void> deleteJiraUser(@PathVariable Long id) {
        log.debug("REST request to delete JiraUser : {}", id);
        jiraUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
