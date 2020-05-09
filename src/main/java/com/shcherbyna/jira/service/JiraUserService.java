package com.shcherbyna.jira.service;

import com.shcherbyna.jira.domain.JiraUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link JiraUser}.
 */
public interface JiraUserService {

    /**
     * Save a jiraUser.
     *
     * @param jiraUser the entity to save.
     * @return the persisted entity.
     */
    JiraUser save(JiraUser jiraUser);

    /**
     * Get all the jiraUsers.
     *
     * @return the list of entities.
     */
    List<JiraUser> findAll();

    /**
     * Get all the jiraUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<JiraUser> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" jiraUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JiraUser> findOne(Long id);

    /**
     * Delete the "id" jiraUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
