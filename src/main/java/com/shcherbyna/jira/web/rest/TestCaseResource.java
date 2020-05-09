package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.domain.TestCase;
import com.shcherbyna.jira.service.TestCaseService;
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
 * REST controller for managing {@link com.shcherbyna.jira.domain.TestCase}.
 */
@RestController
@RequestMapping("/api")
public class TestCaseResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseResource.class);

    private static final String ENTITY_NAME = "testCase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseService testCaseService;

    public TestCaseResource(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    /**
     * {@code POST  /test-cases} : Create a new testCase.
     *
     * @param testCase the testCase to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCase, or with status {@code 400 (Bad Request)} if the testCase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-cases")
    public ResponseEntity<TestCase> createTestCase(@Valid @RequestBody TestCase testCase) throws URISyntaxException {
        log.debug("REST request to save TestCase : {}", testCase);
        if (testCase.getId() != null) {
            throw new BadRequestAlertException("A new testCase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCase result = testCaseService.save(testCase);
        return ResponseEntity.created(new URI("/api/test-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-cases} : Updates an existing testCase.
     *
     * @param testCase the testCase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCase,
     * or with status {@code 400 (Bad Request)} if the testCase is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-cases")
    public ResponseEntity<TestCase> updateTestCase(@Valid @RequestBody TestCase testCase) throws URISyntaxException {
        log.debug("REST request to update TestCase : {}", testCase);
        if (testCase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TestCase result = testCaseService.save(testCase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCase.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /test-cases} : get all the testCases.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCases in body.
     */
    @GetMapping("/test-cases")
    public List<TestCase> getAllTestCases() {
        log.debug("REST request to get all TestCases");
        return testCaseService.findAll();
    }

    /**
     * {@code GET  /test-cases/:id} : get the "id" testCase.
     *
     * @param id the id of the testCase to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCase, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-cases/{id}")
    public ResponseEntity<TestCase> getTestCase(@PathVariable Long id) {
        log.debug("REST request to get TestCase : {}", id);
        Optional<TestCase> testCase = testCaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCase);
    }

    /**
     * {@code DELETE  /test-cases/:id} : delete the "id" testCase.
     *
     * @param id the id of the testCase to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-cases/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        log.debug("REST request to delete TestCase : {}", id);
        testCaseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
