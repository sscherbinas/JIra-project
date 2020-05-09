package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.JiraApp;
import com.shcherbyna.jira.domain.Sprint;
import com.shcherbyna.jira.domain.Story;
import com.shcherbyna.jira.domain.Project;
import com.shcherbyna.jira.repository.SprintRepository;
import com.shcherbyna.jira.service.SprintService;
import com.shcherbyna.jira.service.dto.SprintCriteria;
import com.shcherbyna.jira.service.SprintQueryService;

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
 * Integration tests for the {@link SprintResource} REST controller.
 */
@SpringBootTest(classes = JiraApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SprintResourceIT {

    private static final String DEFAULT_SPRINT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SPRINT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SPRINT_COUNT = 1;
    private static final Integer UPDATED_SPRINT_COUNT = 2;
    private static final Integer SMALLER_SPRINT_COUNT = 1 - 1;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private SprintQueryService sprintQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSprintMockMvc;

    private Sprint sprint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sprint createEntity(EntityManager em) {
        Sprint sprint = new Sprint()
            .sprintName(DEFAULT_SPRINT_NAME)
            .sprintCount(DEFAULT_SPRINT_COUNT);
        return sprint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sprint createUpdatedEntity(EntityManager em) {
        Sprint sprint = new Sprint()
            .sprintName(UPDATED_SPRINT_NAME)
            .sprintCount(UPDATED_SPRINT_COUNT);
        return sprint;
    }

    @BeforeEach
    public void initTest() {
        sprint = createEntity(em);
    }

    @Test
    @Transactional
    public void createSprint() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // Create the Sprint
        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isCreated());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate + 1);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getSprintName()).isEqualTo(DEFAULT_SPRINT_NAME);
        assertThat(testSprint.getSprintCount()).isEqualTo(DEFAULT_SPRINT_COUNT);
    }

    @Test
    @Transactional
    public void createSprintWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // Create the Sprint with an existing ID
        sprint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSprintNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintRepository.findAll().size();
        // set the field null
        sprint.setSprintName(null);

        // Create the Sprint, which fails.

        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isBadRequest());

        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSprints() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList
        restSprintMockMvc.perform(get("/api/sprints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].sprintName").value(hasItem(DEFAULT_SPRINT_NAME)))
            .andExpect(jsonPath("$.[*].sprintCount").value(hasItem(DEFAULT_SPRINT_COUNT)));
    }
    
    @Test
    @Transactional
    public void getSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get the sprint
        restSprintMockMvc.perform(get("/api/sprints/{id}", sprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sprint.getId().intValue()))
            .andExpect(jsonPath("$.sprintName").value(DEFAULT_SPRINT_NAME))
            .andExpect(jsonPath("$.sprintCount").value(DEFAULT_SPRINT_COUNT));
    }


    @Test
    @Transactional
    public void getSprintsByIdFiltering() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        Long id = sprint.getId();

        defaultSprintShouldBeFound("id.equals=" + id);
        defaultSprintShouldNotBeFound("id.notEquals=" + id);

        defaultSprintShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSprintShouldNotBeFound("id.greaterThan=" + id);

        defaultSprintShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSprintShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSprintsBySprintNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintName equals to DEFAULT_SPRINT_NAME
        defaultSprintShouldBeFound("sprintName.equals=" + DEFAULT_SPRINT_NAME);

        // Get all the sprintList where sprintName equals to UPDATED_SPRINT_NAME
        defaultSprintShouldNotBeFound("sprintName.equals=" + UPDATED_SPRINT_NAME);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintName not equals to DEFAULT_SPRINT_NAME
        defaultSprintShouldNotBeFound("sprintName.notEquals=" + DEFAULT_SPRINT_NAME);

        // Get all the sprintList where sprintName not equals to UPDATED_SPRINT_NAME
        defaultSprintShouldBeFound("sprintName.notEquals=" + UPDATED_SPRINT_NAME);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintNameIsInShouldWork() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintName in DEFAULT_SPRINT_NAME or UPDATED_SPRINT_NAME
        defaultSprintShouldBeFound("sprintName.in=" + DEFAULT_SPRINT_NAME + "," + UPDATED_SPRINT_NAME);

        // Get all the sprintList where sprintName equals to UPDATED_SPRINT_NAME
        defaultSprintShouldNotBeFound("sprintName.in=" + UPDATED_SPRINT_NAME);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintName is not null
        defaultSprintShouldBeFound("sprintName.specified=true");

        // Get all the sprintList where sprintName is null
        defaultSprintShouldNotBeFound("sprintName.specified=false");
    }
                @Test
    @Transactional
    public void getAllSprintsBySprintNameContainsSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintName contains DEFAULT_SPRINT_NAME
        defaultSprintShouldBeFound("sprintName.contains=" + DEFAULT_SPRINT_NAME);

        // Get all the sprintList where sprintName contains UPDATED_SPRINT_NAME
        defaultSprintShouldNotBeFound("sprintName.contains=" + UPDATED_SPRINT_NAME);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintNameNotContainsSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintName does not contain DEFAULT_SPRINT_NAME
        defaultSprintShouldNotBeFound("sprintName.doesNotContain=" + DEFAULT_SPRINT_NAME);

        // Get all the sprintList where sprintName does not contain UPDATED_SPRINT_NAME
        defaultSprintShouldBeFound("sprintName.doesNotContain=" + UPDATED_SPRINT_NAME);
    }


    @Test
    @Transactional
    public void getAllSprintsBySprintCountIsEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintCount equals to DEFAULT_SPRINT_COUNT
        defaultSprintShouldBeFound("sprintCount.equals=" + DEFAULT_SPRINT_COUNT);

        // Get all the sprintList where sprintCount equals to UPDATED_SPRINT_COUNT
        defaultSprintShouldNotBeFound("sprintCount.equals=" + UPDATED_SPRINT_COUNT);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintCount not equals to DEFAULT_SPRINT_COUNT
        defaultSprintShouldNotBeFound("sprintCount.notEquals=" + DEFAULT_SPRINT_COUNT);

        // Get all the sprintList where sprintCount not equals to UPDATED_SPRINT_COUNT
        defaultSprintShouldBeFound("sprintCount.notEquals=" + UPDATED_SPRINT_COUNT);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintCountIsInShouldWork() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintCount in DEFAULT_SPRINT_COUNT or UPDATED_SPRINT_COUNT
        defaultSprintShouldBeFound("sprintCount.in=" + DEFAULT_SPRINT_COUNT + "," + UPDATED_SPRINT_COUNT);

        // Get all the sprintList where sprintCount equals to UPDATED_SPRINT_COUNT
        defaultSprintShouldNotBeFound("sprintCount.in=" + UPDATED_SPRINT_COUNT);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintCount is not null
        defaultSprintShouldBeFound("sprintCount.specified=true");

        // Get all the sprintList where sprintCount is null
        defaultSprintShouldNotBeFound("sprintCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintCount is greater than or equal to DEFAULT_SPRINT_COUNT
        defaultSprintShouldBeFound("sprintCount.greaterThanOrEqual=" + DEFAULT_SPRINT_COUNT);

        // Get all the sprintList where sprintCount is greater than or equal to UPDATED_SPRINT_COUNT
        defaultSprintShouldNotBeFound("sprintCount.greaterThanOrEqual=" + UPDATED_SPRINT_COUNT);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintCount is less than or equal to DEFAULT_SPRINT_COUNT
        defaultSprintShouldBeFound("sprintCount.lessThanOrEqual=" + DEFAULT_SPRINT_COUNT);

        // Get all the sprintList where sprintCount is less than or equal to SMALLER_SPRINT_COUNT
        defaultSprintShouldNotBeFound("sprintCount.lessThanOrEqual=" + SMALLER_SPRINT_COUNT);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintCountIsLessThanSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintCount is less than DEFAULT_SPRINT_COUNT
        defaultSprintShouldNotBeFound("sprintCount.lessThan=" + DEFAULT_SPRINT_COUNT);

        // Get all the sprintList where sprintCount is less than UPDATED_SPRINT_COUNT
        defaultSprintShouldBeFound("sprintCount.lessThan=" + UPDATED_SPRINT_COUNT);
    }

    @Test
    @Transactional
    public void getAllSprintsBySprintCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where sprintCount is greater than DEFAULT_SPRINT_COUNT
        defaultSprintShouldNotBeFound("sprintCount.greaterThan=" + DEFAULT_SPRINT_COUNT);

        // Get all the sprintList where sprintCount is greater than SMALLER_SPRINT_COUNT
        defaultSprintShouldBeFound("sprintCount.greaterThan=" + SMALLER_SPRINT_COUNT);
    }


    @Test
    @Transactional
    public void getAllSprintsByStoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);
        Story stories = StoryResourceIT.createEntity(em);
        em.persist(stories);
        em.flush();
        sprint.addStories(stories);
        sprintRepository.saveAndFlush(sprint);
        Long storiesId = stories.getId();

        // Get all the sprintList where stories equals to storiesId
        defaultSprintShouldBeFound("storiesId.equals=" + storiesId);

        // Get all the sprintList where stories equals to storiesId + 1
        defaultSprintShouldNotBeFound("storiesId.equals=" + (storiesId + 1));
    }


    @Test
    @Transactional
    public void getAllSprintsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);
        Project project = ProjectResourceIT.createEntity(em);
        em.persist(project);
        em.flush();
        sprint.setProject(project);
        sprintRepository.saveAndFlush(sprint);
        Long projectId = project.getId();

        // Get all the sprintList where project equals to projectId
        defaultSprintShouldBeFound("projectId.equals=" + projectId);

        // Get all the sprintList where project equals to projectId + 1
        defaultSprintShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSprintShouldBeFound(String filter) throws Exception {
        restSprintMockMvc.perform(get("/api/sprints?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].sprintName").value(hasItem(DEFAULT_SPRINT_NAME)))
            .andExpect(jsonPath("$.[*].sprintCount").value(hasItem(DEFAULT_SPRINT_COUNT)));

        // Check, that the count call also returns 1
        restSprintMockMvc.perform(get("/api/sprints/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSprintShouldNotBeFound(String filter) throws Exception {
        restSprintMockMvc.perform(get("/api/sprints?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSprintMockMvc.perform(get("/api/sprints/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSprint() throws Exception {
        // Get the sprint
        restSprintMockMvc.perform(get("/api/sprints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSprint() throws Exception {
        // Initialize the database
        sprintService.save(sprint);

        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint
        Sprint updatedSprint = sprintRepository.findById(sprint.getId()).get();
        // Disconnect from session so that the updates on updatedSprint are not directly saved in db
        em.detach(updatedSprint);
        updatedSprint
            .sprintName(UPDATED_SPRINT_NAME)
            .sprintCount(UPDATED_SPRINT_COUNT);

        restSprintMockMvc.perform(put("/api/sprints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSprint)))
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getSprintName()).isEqualTo(UPDATED_SPRINT_NAME);
        assertThat(testSprint.getSprintCount()).isEqualTo(UPDATED_SPRINT_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Create the Sprint

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSprintMockMvc.perform(put("/api/sprints")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSprint() throws Exception {
        // Initialize the database
        sprintService.save(sprint);

        int databaseSizeBeforeDelete = sprintRepository.findAll().size();

        // Delete the sprint
        restSprintMockMvc.perform(delete("/api/sprints/{id}", sprint.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
