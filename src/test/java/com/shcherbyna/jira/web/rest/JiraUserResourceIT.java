package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.JiraApp;
import com.shcherbyna.jira.domain.JiraUser;
import com.shcherbyna.jira.repository.JiraUserRepository;
import com.shcherbyna.jira.service.JiraUserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JiraUserResource} REST controller.
 */
@SpringBootTest(classes = JiraApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class JiraUserResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    @Autowired
    private JiraUserRepository jiraUserRepository;

    @Mock
    private JiraUserRepository jiraUserRepositoryMock;

    @Mock
    private JiraUserService jiraUserServiceMock;

    @Autowired
    private JiraUserService jiraUserService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJiraUserMockMvc;

    private JiraUser jiraUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JiraUser createEntity(EntityManager em) {
        JiraUser jiraUser = new JiraUser()
            .userName(DEFAULT_USER_NAME)
            .jobTitle(DEFAULT_JOB_TITLE);
        return jiraUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JiraUser createUpdatedEntity(EntityManager em) {
        JiraUser jiraUser = new JiraUser()
            .userName(UPDATED_USER_NAME)
            .jobTitle(UPDATED_JOB_TITLE);
        return jiraUser;
    }

    @BeforeEach
    public void initTest() {
        jiraUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createJiraUser() throws Exception {
        int databaseSizeBeforeCreate = jiraUserRepository.findAll().size();

        // Create the JiraUser
        restJiraUserMockMvc.perform(post("/api/jira-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jiraUser)))
            .andExpect(status().isCreated());

        // Validate the JiraUser in the database
        List<JiraUser> jiraUserList = jiraUserRepository.findAll();
        assertThat(jiraUserList).hasSize(databaseSizeBeforeCreate + 1);
        JiraUser testJiraUser = jiraUserList.get(jiraUserList.size() - 1);
        assertThat(testJiraUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testJiraUser.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
    }

    @Test
    @Transactional
    public void createJiraUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jiraUserRepository.findAll().size();

        // Create the JiraUser with an existing ID
        jiraUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJiraUserMockMvc.perform(post("/api/jira-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jiraUser)))
            .andExpect(status().isBadRequest());

        // Validate the JiraUser in the database
        List<JiraUser> jiraUserList = jiraUserRepository.findAll();
        assertThat(jiraUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jiraUserRepository.findAll().size();
        // set the field null
        jiraUser.setUserName(null);

        // Create the JiraUser, which fails.

        restJiraUserMockMvc.perform(post("/api/jira-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jiraUser)))
            .andExpect(status().isBadRequest());

        List<JiraUser> jiraUserList = jiraUserRepository.findAll();
        assertThat(jiraUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJiraUsers() throws Exception {
        // Initialize the database
        jiraUserRepository.saveAndFlush(jiraUser);

        // Get all the jiraUserList
        restJiraUserMockMvc.perform(get("/api/jira-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jiraUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllJiraUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(jiraUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJiraUserMockMvc.perform(get("/api/jira-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(jiraUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllJiraUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jiraUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJiraUserMockMvc.perform(get("/api/jira-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(jiraUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getJiraUser() throws Exception {
        // Initialize the database
        jiraUserRepository.saveAndFlush(jiraUser);

        // Get the jiraUser
        restJiraUserMockMvc.perform(get("/api/jira-users/{id}", jiraUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jiraUser.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE));
    }

    @Test
    @Transactional
    public void getNonExistingJiraUser() throws Exception {
        // Get the jiraUser
        restJiraUserMockMvc.perform(get("/api/jira-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJiraUser() throws Exception {
        // Initialize the database
        jiraUserService.save(jiraUser);

        int databaseSizeBeforeUpdate = jiraUserRepository.findAll().size();

        // Update the jiraUser
        JiraUser updatedJiraUser = jiraUserRepository.findById(jiraUser.getId()).get();
        // Disconnect from session so that the updates on updatedJiraUser are not directly saved in db
        em.detach(updatedJiraUser);
        updatedJiraUser
            .userName(UPDATED_USER_NAME)
            .jobTitle(UPDATED_JOB_TITLE);

        restJiraUserMockMvc.perform(put("/api/jira-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJiraUser)))
            .andExpect(status().isOk());

        // Validate the JiraUser in the database
        List<JiraUser> jiraUserList = jiraUserRepository.findAll();
        assertThat(jiraUserList).hasSize(databaseSizeBeforeUpdate);
        JiraUser testJiraUser = jiraUserList.get(jiraUserList.size() - 1);
        assertThat(testJiraUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testJiraUser.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingJiraUser() throws Exception {
        int databaseSizeBeforeUpdate = jiraUserRepository.findAll().size();

        // Create the JiraUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJiraUserMockMvc.perform(put("/api/jira-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jiraUser)))
            .andExpect(status().isBadRequest());

        // Validate the JiraUser in the database
        List<JiraUser> jiraUserList = jiraUserRepository.findAll();
        assertThat(jiraUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJiraUser() throws Exception {
        // Initialize the database
        jiraUserService.save(jiraUser);

        int databaseSizeBeforeDelete = jiraUserRepository.findAll().size();

        // Delete the jiraUser
        restJiraUserMockMvc.perform(delete("/api/jira-users/{id}", jiraUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JiraUser> jiraUserList = jiraUserRepository.findAll();
        assertThat(jiraUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
