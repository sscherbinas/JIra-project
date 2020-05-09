package com.shcherbyna.jira.service;

import com.shcherbyna.jira.domain.TestCase;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link TestCase}.
 */
public interface TestCaseService {

    /**
     * Save a testCase.
     *
     * @param testCase the entity to save.
     * @return the persisted entity.
     */
    TestCase save(TestCase testCase);

    /**
     * Get all the testCases.
     *
     * @return the list of entities.
     */
    List<TestCase> findAll();

    /**
     * Get the "id" testCase.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestCase> findOne(Long id);

    /**
     * Delete the "id" testCase.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
