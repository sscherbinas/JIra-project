package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.domain.Defect;
import com.shcherbyna.jira.service.DefectService;
import com.shcherbyna.jira.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.shcherbyna.jira.domain.Defect}.
 */
@RestController
@RequestMapping("/api")
public class DefectResource {

    private final Logger log = LoggerFactory.getLogger(DefectResource.class);

    private static final String ENTITY_NAME = "defect";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DefectService defectService;

    public DefectResource(DefectService defectService) {
        this.defectService = defectService;
    }

    /**
     * {@code POST  /defects} : Create a new defect.
     *
     * @param defect the defect to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new defect, or with status {@code 400 (Bad Request)} if the defect has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/defects")
    public ResponseEntity<Defect> createDefect(@Valid @RequestBody Defect defect) throws URISyntaxException {
        log.debug("REST request to save Defect : {}", defect);
        if (defect.getId() != null) {
            throw new BadRequestAlertException("A new defect cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Defect result = defectService.save(defect);
        return ResponseEntity.created(new URI("/api/defects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /defects} : Updates an existing defect.
     *
     * @param defect the defect to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated defect,
     * or with status {@code 400 (Bad Request)} if the defect is not valid,
     * or with status {@code 500 (Internal Server Error)} if the defect couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/defects")
    public ResponseEntity<Defect> updateDefect(@Valid @RequestBody Defect defect) throws URISyntaxException {
        log.debug("REST request to update Defect : {}", defect);
        if (defect.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Defect result = defectService.save(defect);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, defect.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /defects} : get all the defects.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of defects in body.
     */
    @GetMapping("/defects")
    public ResponseEntity<List<Defect>> getAllDefects(Pageable pageable) {
        log.debug("REST request to get a page of Defects");
        Page<Defect> page = defectService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /defects/:id} : get the "id" defect.
     *
     * @param id the id of the defect to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the defect, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/defects/{id}")
    public ResponseEntity<Defect> getDefect(@PathVariable Long id) {
        log.debug("REST request to get Defect : {}", id);
        Optional<Defect> defect = defectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(defect);
    }

    /**
     * {@code DELETE  /defects/:id} : delete the "id" defect.
     *
     * @param id the id of the defect to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/defects/{id}")
    public ResponseEntity<Void> deleteDefect(@PathVariable Long id) {
        log.debug("REST request to delete Defect : {}", id);
        defectService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
