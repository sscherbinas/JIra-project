package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.JiraApp;
import com.shcherbyna.jira.domain.Project;
import com.shcherbyna.jira.domain.Sprint;
import com.shcherbyna.jira.domain.JiraUser;
import com.shcherbyna.jira.repository.ProjectRepository;
import com.shcherbyna.jira.service.ProjectService;
import com.shcherbyna.jira.service.dto.ProjectCriteria;
import com.shcherbyna.jira.service.ProjectQueryService;

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
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@SpringBootTest(classes = JiraApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ProjectResourceIT {

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_MANAGER = "BBBBBBBBBB";

    private static final Long DEFAULT_TEAM_SIZE = 1L;
    private static final Long UPDATED_TEAM_SIZE = 2L;
    private static final Long SMALLER_TEAM_SIZE = 1L - 1L;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectQueryService projectQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMockMvc;

    private Project project;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .projectName(DEFAULT_PROJECT_NAME)
            .projectManager(DEFAULT_PROJECT_MANAGER)
            .teamSize(DEFAULT_TEAM_SIZE);
        return project;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .projectName(UPDATED_PROJECT_NAME)
            .projectManager(UPDATED_PROJECT_MANAGER)
            .teamSize(UPDATED_TEAM_SIZE);
        return project;
    }

    @BeforeEach
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testProject.getProjectManager()).isEqualTo(DEFAULT_PROJECT_MANAGER);
        assertThat(testProject.getTeamSize()).isEqualTo(DEFAULT_TEAM_SIZE);
    }

    @Test
    @Transactional
    public void createProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project with an existing ID
        project.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setProjectName(null);

        // Create the Project, which fails.

        restProjectMockMvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].projectManager").value(hasItem(DEFAULT_PROJECT_MANAGER)))
            .andExpect(jsonPath("$.[*].teamSize").value(hasItem(DEFAULT_TEAM_SIZE.intValue())));
    }
    
    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.projectManager").value(DEFAULT_PROJECT_MANAGER))
            .andExpect(jsonPath("$.teamSize").value(DEFAULT_TEAM_SIZE.intValue()));
    }


    @Test
    @Transactional
    public void getProjectsByIdFiltering() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        Long id = project.getId();

        defaultProjectShouldBeFound("id.equals=" + id);
        defaultProjectShouldNotBeFound("id.notEquals=" + id);

        defaultProjectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.greaterThan=" + id);

        defaultProjectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProjectsByProjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName equals to DEFAULT_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.equals=" + DEFAULT_PROJECT_NAME);

        // Get all the projectList where projectName equals to UPDATED_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.equals=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName not equals to DEFAULT_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.notEquals=" + DEFAULT_PROJECT_NAME);

        // Get all the projectList where projectName not equals to UPDATED_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.notEquals=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName in DEFAULT_PROJECT_NAME or UPDATED_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.in=" + DEFAULT_PROJECT_NAME + "," + UPDATED_PROJECT_NAME);

        // Get all the projectList where projectName equals to UPDATED_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.in=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName is not null
        defaultProjectShouldBeFound("projectName.specified=true");

        // Get all the projectList where projectName is null
        defaultProjectShouldNotBeFound("projectName.specified=false");
    }
                @Test
    @Transactional
    public void getAllProjectsByProjectNameContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName contains DEFAULT_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.contains=" + DEFAULT_PROJECT_NAME);

        // Get all the projectList where projectName contains UPDATED_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.contains=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName does not contain DEFAULT_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.doesNotContain=" + DEFAULT_PROJECT_NAME);

        // Get all the projectList where projectName does not contain UPDATED_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.doesNotContain=" + UPDATED_PROJECT_NAME);
    }


    @Test
    @Transactional
    public void getAllProjectsByProjectManagerIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectManager equals to DEFAULT_PROJECT_MANAGER
        defaultProjectShouldBeFound("projectManager.equals=" + DEFAULT_PROJECT_MANAGER);

        // Get all the projectList where projectManager equals to UPDATED_PROJECT_MANAGER
        defaultProjectShouldNotBeFound("projectManager.equals=" + UPDATED_PROJECT_MANAGER);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectManagerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectManager not equals to DEFAULT_PROJECT_MANAGER
        defaultProjectShouldNotBeFound("projectManager.notEquals=" + DEFAULT_PROJECT_MANAGER);

        // Get all the projectList where projectManager not equals to UPDATED_PROJECT_MANAGER
        defaultProjectShouldBeFound("projectManager.notEquals=" + UPDATED_PROJECT_MANAGER);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectManagerIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectManager in DEFAULT_PROJECT_MANAGER or UPDATED_PROJECT_MANAGER
        defaultProjectShouldBeFound("projectManager.in=" + DEFAULT_PROJECT_MANAGER + "," + UPDATED_PROJECT_MANAGER);

        // Get all the projectList where projectManager equals to UPDATED_PROJECT_MANAGER
        defaultProjectShouldNotBeFound("projectManager.in=" + UPDATED_PROJECT_MANAGER);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectManagerIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectManager is not null
        defaultProjectShouldBeFound("projectManager.specified=true");

        // Get all the projectList where projectManager is null
        defaultProjectShouldNotBeFound("projectManager.specified=false");
    }
                @Test
    @Transactional
    public void getAllProjectsByProjectManagerContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectManager contains DEFAULT_PROJECT_MANAGER
        defaultProjectShouldBeFound("projectManager.contains=" + DEFAULT_PROJECT_MANAGER);

        // Get all the projectList where projectManager contains UPDATED_PROJECT_MANAGER
        defaultProjectShouldNotBeFound("projectManager.contains=" + UPDATED_PROJECT_MANAGER);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectManagerNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectManager does not contain DEFAULT_PROJECT_MANAGER
        defaultProjectShouldNotBeFound("projectManager.doesNotContain=" + DEFAULT_PROJECT_MANAGER);

        // Get all the projectList where projectManager does not contain UPDATED_PROJECT_MANAGER
        defaultProjectShouldBeFound("projectManager.doesNotContain=" + UPDATED_PROJECT_MANAGER);
    }


    @Test
    @Transactional
    public void getAllProjectsByTeamSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where teamSize equals to DEFAULT_TEAM_SIZE
        defaultProjectShouldBeFound("teamSize.equals=" + DEFAULT_TEAM_SIZE);

        // Get all the projectList where teamSize equals to UPDATED_TEAM_SIZE
        defaultProjectShouldNotBeFound("teamSize.equals=" + UPDATED_TEAM_SIZE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTeamSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where teamSize not equals to DEFAULT_TEAM_SIZE
        defaultProjectShouldNotBeFound("teamSize.notEquals=" + DEFAULT_TEAM_SIZE);

        // Get all the projectList where teamSize not equals to UPDATED_TEAM_SIZE
        defaultProjectShouldBeFound("teamSize.notEquals=" + UPDATED_TEAM_SIZE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTeamSizeIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where teamSize in DEFAULT_TEAM_SIZE or UPDATED_TEAM_SIZE
        defaultProjectShouldBeFound("teamSize.in=" + DEFAULT_TEAM_SIZE + "," + UPDATED_TEAM_SIZE);

        // Get all the projectList where teamSize equals to UPDATED_TEAM_SIZE
        defaultProjectShouldNotBeFound("teamSize.in=" + UPDATED_TEAM_SIZE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTeamSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where teamSize is not null
        defaultProjectShouldBeFound("teamSize.specified=true");

        // Get all the projectList where teamSize is null
        defaultProjectShouldNotBeFound("teamSize.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByTeamSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where teamSize is greater than or equal to DEFAULT_TEAM_SIZE
        defaultProjectShouldBeFound("teamSize.greaterThanOrEqual=" + DEFAULT_TEAM_SIZE);

        // Get all the projectList where teamSize is greater than or equal to UPDATED_TEAM_SIZE
        defaultProjectShouldNotBeFound("teamSize.greaterThanOrEqual=" + UPDATED_TEAM_SIZE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTeamSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where teamSize is less than or equal to DEFAULT_TEAM_SIZE
        defaultProjectShouldBeFound("teamSize.lessThanOrEqual=" + DEFAULT_TEAM_SIZE);

        // Get all the projectList where teamSize is less than or equal to SMALLER_TEAM_SIZE
        defaultProjectShouldNotBeFound("teamSize.lessThanOrEqual=" + SMALLER_TEAM_SIZE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTeamSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where teamSize is less than DEFAULT_TEAM_SIZE
        defaultProjectShouldNotBeFound("teamSize.lessThan=" + DEFAULT_TEAM_SIZE);

        // Get all the projectList where teamSize is less than UPDATED_TEAM_SIZE
        defaultProjectShouldBeFound("teamSize.lessThan=" + UPDATED_TEAM_SIZE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTeamSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where teamSize is greater than DEFAULT_TEAM_SIZE
        defaultProjectShouldNotBeFound("teamSize.greaterThan=" + DEFAULT_TEAM_SIZE);

        // Get all the projectList where teamSize is greater than SMALLER_TEAM_SIZE
        defaultProjectShouldBeFound("teamSize.greaterThan=" + SMALLER_TEAM_SIZE);
    }


    @Test
    @Transactional
    public void getAllProjectsBySprintsIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        Sprint sprints = SprintResourceIT.createEntity(em);
        em.persist(sprints);
        em.flush();
        project.addSprints(sprints);
        projectRepository.saveAndFlush(project);
        Long sprintsId = sprints.getId();

        // Get all the projectList where sprints equals to sprintsId
        defaultProjectShouldBeFound("sprintsId.equals=" + sprintsId);

        // Get all the projectList where sprints equals to sprintsId + 1
        defaultProjectShouldNotBeFound("sprintsId.equals=" + (sprintsId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        JiraUser users = JiraUserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        project.addUsers(users);
        projectRepository.saveAndFlush(project);
        Long usersId = users.getId();

        // Get all the projectList where users equals to usersId
        defaultProjectShouldBeFound("usersId.equals=" + usersId);

        // Get all the projectList where users equals to usersId + 1
        defaultProjectShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjectShouldBeFound(String filter) throws Exception {
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].projectManager").value(hasItem(DEFAULT_PROJECT_MANAGER)))
            .andExpect(jsonPath("$.[*].teamSize").value(hasItem(DEFAULT_TEAM_SIZE.intValue())));

        // Check, that the count call also returns 1
        restProjectMockMvc.perform(get("/api/projects/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjectShouldNotBeFound(String filter) throws Exception {
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectMockMvc.perform(get("/api/projects/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).get();
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .projectName(UPDATED_PROJECT_NAME)
            .projectManager(UPDATED_PROJECT_MANAGER)
            .teamSize(UPDATED_TEAM_SIZE);

        restProjectMockMvc.perform(put("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProject.getProjectManager()).isEqualTo(UPDATED_PROJECT_MANAGER);
        assertThat(testProject.getTeamSize()).isEqualTo(UPDATED_TEAM_SIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Create the Project

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc.perform(put("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Delete the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
