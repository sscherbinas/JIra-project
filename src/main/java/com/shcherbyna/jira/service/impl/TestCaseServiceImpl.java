package com.shcherbyna.jira.service.impl;

import com.shcherbyna.jira.service.TestCaseService;
import com.shcherbyna.jira.domain.TestCase;
import com.shcherbyna.jira.repository.TestCaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link TestCase}.
 */
@Service
@Transactional
public class TestCaseServiceImpl implements TestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCaseServiceImpl.class);

    private final TestCaseRepository testCaseRepository;

    public TestCaseServiceImpl(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    /**
     * Save a testCase.
     *
     * @param testCase the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TestCase save(TestCase testCase) {
        log.debug("Request to save TestCase : {}", testCase);
        return testCaseRepository.save(testCase);
    }

    /**
     * Get all the testCases.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TestCase> findAll() {
        log.debug("Request to get all TestCases");
        return testCaseRepository.findAll();
    }

    /**
     * Get one testCase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TestCase> findOne(Long id) {
        log.debug("Request to get TestCase : {}", id);
        return testCaseRepository.findById(id);
    }

    /**
     * Delete the testCase by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestCase : {}", id);
        testCaseRepository.deleteById(id);
    }
}
