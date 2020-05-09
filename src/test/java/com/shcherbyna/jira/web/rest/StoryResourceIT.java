package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.JiraApp;
import com.shcherbyna.jira.domain.Story;
import com.shcherbyna.jira.repository.StoryRepository;
import com.shcherbyna.jira.service.StoryService;

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
 * Integration tests for the {@link StoryResource} REST controller.
 */
@SpringBootTest(classes = JiraApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class StoryResourceIT {

    private static final String DEFAULT_STORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private StoryService storyService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoryMockMvc;

    private Story story;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Story createEntity(EntityManager em) {
        Story story = new Story()
            .storyName(DEFAULT_STORY_NAME)
            .createdBy(DEFAULT_CREATED_BY)
            .description(DEFAULT_DESCRIPTION);
        return story;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Story createUpdatedEntity(EntityManager em) {
        Story story = new Story()
            .storyName(UPDATED_STORY_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .description(UPDATED_DESCRIPTION);
        return story;
    }

    @BeforeEach
    public void initTest() {
        story = createEntity(em);
    }

    @Test
    @Transactional
    public void createStory() throws Exception {
        int databaseSizeBeforeCreate = storyRepository.findAll().size();

        // Create the Story
        restStoryMockMvc.perform(post("/api/stories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isCreated());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate + 1);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getStoryName()).isEqualTo(DEFAULT_STORY_NAME);
        assertThat(testStory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testStory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createStoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storyRepository.findAll().size();

        // Create the Story with an existing ID
        story.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoryMockMvc.perform(post("/api/stories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storyRepository.findAll().size();
        // set the field null
        story.setStoryName(null);

        // Create the Story, which fails.

        restStoryMockMvc.perform(post("/api/stories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isBadRequest());

        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStories() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get all the storyList
        restStoryMockMvc.perform(get("/api/stories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(story.getId().intValue())))
            .andExpect(jsonPath("$.[*].storyName").value(hasItem(DEFAULT_STORY_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get the story
        restStoryMockMvc.perform(get("/api/stories/{id}", story.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(story.getId().intValue()))
            .andExpect(jsonPath("$.storyName").value(DEFAULT_STORY_NAME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingStory() throws Exception {
        // Get the story
        restStoryMockMvc.perform(get("/api/stories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStory() throws Exception {
        // Initialize the database
        storyService.save(story);

        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Update the story
        Story updatedStory = storyRepository.findById(story.getId()).get();
        // Disconnect from session so that the updates on updatedStory are not directly saved in db
        em.detach(updatedStory);
        updatedStory
            .storyName(UPDATED_STORY_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .description(UPDATED_DESCRIPTION);

        restStoryMockMvc.perform(put("/api/stories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStory)))
            .andExpect(status().isOk());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getStoryName()).isEqualTo(UPDATED_STORY_NAME);
        assertThat(testStory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Create the Story

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoryMockMvc.perform(put("/api/stories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStory() throws Exception {
        // Initialize the database
        storyService.save(story);

        int databaseSizeBeforeDelete = storyRepository.findAll().size();

        // Delete the story
        restStoryMockMvc.perform(delete("/api/stories/{id}", story.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
