package com.shcherbyna.jira.web.rest;

import com.shcherbyna.jira.JiraApp;
import com.shcherbyna.jira.domain.Defect;
import com.shcherbyna.jira.repository.DefectRepository;
import com.shcherbyna.jira.service.DefectService;

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
 * Integration tests for the {@link DefectResource} REST controller.
 */
@SpringBootTest(classes = JiraApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class DefectResourceIT {

    private static final String DEFAULT_DEFECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEFECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DefectRepository defectRepository;

    @Autowired
    private DefectService defectService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDefectMockMvc;

    private Defect defect;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Defect createEntity(EntityManager em) {
        Defect defect = new Defect()
            .defectName(DEFAULT_DEFECT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return defect;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Defect createUpdatedEntity(EntityManager em) {
        Defect defect = new Defect()
            .defectName(UPDATED_DEFECT_NAME)
            .description(UPDATED_DESCRIPTION);
        return defect;
    }

    @BeforeEach
    public void initTest() {
        defect = createEntity(em);
    }

    @Test
    @Transactional
    public void createDefect() throws Exception {
        int databaseSizeBeforeCreate = defectRepository.findAll().size();

        // Create the Defect
        restDefectMockMvc.perform(post("/api/defects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(defect)))
            .andExpect(status().isCreated());

        // Validate the Defect in the database
        List<Defect> defectList = defectRepository.findAll();
        assertThat(defectList).hasSize(databaseSizeBeforeCreate + 1);
        Defect testDefect = defectList.get(defectList.size() - 1);
        assertThat(testDefect.getDefectName()).isEqualTo(DEFAULT_DEFECT_NAME);
        assertThat(testDefect.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDefectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = defectRepository.findAll().size();

        // Create the Defect with an existing ID
        defect.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDefectMockMvc.perform(post("/api/defects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(defect)))
            .andExpect(status().isBadRequest());

        // Validate the Defect in the database
        List<Defect> defectList = defectRepository.findAll();
        assertThat(defectList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDefectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = defectRepository.findAll().size();
        // set the field null
        defect.setDefectName(null);

        // Create the Defect, which fails.

        restDefectMockMvc.perform(post("/api/defects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(defect)))
            .andExpect(status().isBadRequest());

        List<Defect> defectList = defectRepository.findAll();
        assertThat(defectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDefects() throws Exception {
        // Initialize the database
        defectRepository.saveAndFlush(defect);

        // Get all the defectList
        restDefectMockMvc.perform(get("/api/defects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(defect.getId().intValue())))
            .andExpect(jsonPath("$.[*].defectName").value(hasItem(DEFAULT_DEFECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getDefect() throws Exception {
        // Initialize the database
        defectRepository.saveAndFlush(defect);

        // Get the defect
        restDefectMockMvc.perform(get("/api/defects/{id}", defect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(defect.getId().intValue()))
            .andExpect(jsonPath("$.defectName").value(DEFAULT_DEFECT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingDefect() throws Exception {
        // Get the defect
        restDefectMockMvc.perform(get("/api/defects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDefect() throws Exception {
        // Initialize the database
        defectService.save(defect);

        int databaseSizeBeforeUpdate = defectRepository.findAll().size();

        // Update the defect
        Defect updatedDefect = defectRepository.findById(defect.getId()).get();
        // Disconnect from session so that the updates on updatedDefect are not directly saved in db
        em.detach(updatedDefect);
        updatedDefect
            .defectName(UPDATED_DEFECT_NAME)
            .description(UPDATED_DESCRIPTION);

        restDefectMockMvc.perform(put("/api/defects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDefect)))
            .andExpect(status().isOk());

        // Validate the Defect in the database
        List<Defect> defectList = defectRepository.findAll();
        assertThat(defectList).hasSize(databaseSizeBeforeUpdate);
        Defect testDefect = defectList.get(defectList.size() - 1);
        assertThat(testDefect.getDefectName()).isEqualTo(UPDATED_DEFECT_NAME);
        assertThat(testDefect.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDefect() throws Exception {
        int databaseSizeBeforeUpdate = defectRepository.findAll().size();

        // Create the Defect

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefectMockMvc.perform(put("/api/defects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(defect)))
            .andExpect(status().isBadRequest());

        // Validate the Defect in the database
        List<Defect> defectList = defectRepository.findAll();
        assertThat(defectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDefect() throws Exception {
        // Initialize the database
        defectService.save(defect);

        int databaseSizeBeforeDelete = defectRepository.findAll().size();

        // Delete the defect
        restDefectMockMvc.perform(delete("/api/defects/{id}", defect.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Defect> defectList = defectRepository.findAll();
        assertThat(defectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
