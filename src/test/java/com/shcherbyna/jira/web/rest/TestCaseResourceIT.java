package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.JiraApp;
import com.shcherbyna.jira.domain.TestCase;
import com.shcherbyna.jira.repository.TestCaseRepository;
import com.shcherbyna.jira.service.TestCaseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TestCaseResource} REST controller.
 */
@SpringBootTest(classes = JiraApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class TestCaseResourceIT {

    private static final String DEFAULT_TEST_CASE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEST_CASE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseMockMvc;

    private TestCase testCase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createEntity(EntityManager em) {
        TestCase testCase = new TestCase()
            .testCaseName(DEFAULT_TEST_CASE_NAME)
            .description(DEFAULT_DESCRIPTION);
        return testCase;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createUpdatedEntity(EntityManager em) {
        TestCase testCase = new TestCase()
            .testCaseName(UPDATED_TEST_CASE_NAME)
            .description(UPDATED_DESCRIPTION);
        return testCase;
    }

    @BeforeEach
    public void initTest() {
        testCase = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestCase() throws Exception {
        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();

        // Create the TestCase
        restTestCaseMockMvc.perform(post("/api/test-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(testCase)))
            .andExpect(status().isCreated());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate + 1);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getTestCaseName()).isEqualTo(DEFAULT_TEST_CASE_NAME);
        assertThat(testTestCase.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTestCaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testCaseRepository.findAll().size();

        // Create the TestCase with an existing ID
        testCase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseMockMvc.perform(post("/api/test-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(testCase)))
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTestCaseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCaseRepository.findAll().size();
        // set the field null
        testCase.setTestCaseName(null);

        // Create the TestCase, which fails.

        restTestCaseMockMvc.perform(post("/api/test-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(testCase)))
            .andExpect(status().isBadRequest());

        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTestCases() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList
        restTestCaseMockMvc.perform(get("/api/test-cases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].testCaseName").value(hasItem(DEFAULT_TEST_CASE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getTestCase() throws Exception {
        // Initialize the database
        testCaseRepository.saveAndFlush(testCase);

        // Get the testCase
        restTestCaseMockMvc.perform(get("/api/test-cases/{id}", testCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCase.getId().intValue()))
            .andExpect(jsonPath("$.testCaseName").value(DEFAULT_TEST_CASE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingTestCase() throws Exception {
        // Get the testCase
        restTestCaseMockMvc.perform(get("/api/test-cases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestCase() throws Exception {
        // Initialize the database
        testCaseService.save(testCase);

        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Update the testCase
        TestCase updatedTestCase = testCaseRepository.findById(testCase.getId()).get();
        // Disconnect from session so that the updates on updatedTestCase are not directly saved in db
        em.detach(updatedTestCase);
        updatedTestCase
            .testCaseName(UPDATED_TEST_CASE_NAME)
            .description(UPDATED_DESCRIPTION);

        restTestCaseMockMvc.perform(put("/api/test-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTestCase)))
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
        TestCase testTestCase = testCaseList.get(testCaseList.size() - 1);
        assertThat(testTestCase.getTestCaseName()).isEqualTo(UPDATED_TEST_CASE_NAME);
        assertThat(testTestCase.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingTestCase() throws Exception {
        int databaseSizeBeforeUpdate = testCaseRepository.findAll().size();

        // Create the TestCase

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseMockMvc.perform(put("/api/test-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(testCase)))
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTestCase() throws Exception {
        // Initialize the database
        testCaseService.save(testCase);

        int databaseSizeBeforeDelete = testCaseRepository.findAll().size();

        // Delete the testCase
        restTestCaseMockMvc.perform(delete("/api/test-cases/{id}", testCase.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCase> testCaseList = testCaseRepository.findAll();
        assertThat(testCaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
