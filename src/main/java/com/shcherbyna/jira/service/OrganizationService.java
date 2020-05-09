package com.shcherbyna.jira.service;

import com.shcherbyna.jira.domain.Organization;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Organization}.
 */
public interface      OrganizationService {

    /**
     * Save a organization.
     *
     * @param organization the entity to save.
     * @return the persisted entity.
     */
    Organization save(Organization organization);

    /**
     * Get all the organizations.
     *
     * @return the list of entities.
     */
    List<Organization> findAll();

    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Organization> findOne(Long id);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
