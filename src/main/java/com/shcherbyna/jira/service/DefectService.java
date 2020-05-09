package com.shcherbyna.jira.service;

import com.shcherbyna.jira.domain.Defect;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Defect}.
 */
public interface DefectService {

    /**
     * Save a defect.
     *
     * @param defect the entity to save.
     * @return the persisted entity.
     */
    Defect save(Defect defect);

    /**
     * Get all the defects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Defect> findAll(Pageable pageable);

    /**
     * Get the "id" defect.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Defect> findOne(Long id);

    /**
     * Delete the "id" defect.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
