package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;
import by.bsuir.domain.ProfitabilityAnalysis;
import by.bsuir.domain.User;
import by.bsuir.repository.ProfitabilityAnalysisRepository;
import by.bsuir.repository.search.ProfitabilityAnalysisSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProfitabilityAnalysisResource} REST controller.
 */
@SpringBootTest(classes = CourseworkApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProfitabilityAnalysisResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_RESULT = false;
    private static final Boolean UPDATED_RESULT = true;

    @Autowired
    private ProfitabilityAnalysisRepository profitabilityAnalysisRepository;

    /**
     * This repository is mocked in the by.bsuir.repository.search test package.
     *
     * @see by.bsuir.repository.search.ProfitabilityAnalysisSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfitabilityAnalysisSearchRepository mockProfitabilityAnalysisSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfitabilityAnalysisMockMvc;

    private ProfitabilityAnalysis profitabilityAnalysis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfitabilityAnalysis createEntity(EntityManager em) {
        ProfitabilityAnalysis profitabilityAnalysis = new ProfitabilityAnalysis()
            .date(DEFAULT_DATE)
            .result(DEFAULT_RESULT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profitabilityAnalysis.setUser(user);
        return profitabilityAnalysis;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfitabilityAnalysis createUpdatedEntity(EntityManager em) {
        ProfitabilityAnalysis profitabilityAnalysis = new ProfitabilityAnalysis()
            .date(UPDATED_DATE)
            .result(UPDATED_RESULT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profitabilityAnalysis.setUser(user);
        return profitabilityAnalysis;
    }

    @BeforeEach
    public void initTest() {
        profitabilityAnalysis = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfitabilityAnalysis() throws Exception {
        int databaseSizeBeforeCreate = profitabilityAnalysisRepository.findAll().size();

        // Create the ProfitabilityAnalysis
        restProfitabilityAnalysisMockMvc.perform(post("/api/profitability-analyses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profitabilityAnalysis)))
            .andExpect(status().isCreated());

        // Validate the ProfitabilityAnalysis in the database
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeCreate + 1);
        ProfitabilityAnalysis testProfitabilityAnalysis = profitabilityAnalysisList.get(profitabilityAnalysisList.size() - 1);
        assertThat(testProfitabilityAnalysis.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProfitabilityAnalysis.isResult()).isEqualTo(DEFAULT_RESULT);

        // Validate the ProfitabilityAnalysis in Elasticsearch
        verify(mockProfitabilityAnalysisSearchRepository, times(1)).save(testProfitabilityAnalysis);
    }

    @Test
    @Transactional
    public void createProfitabilityAnalysisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profitabilityAnalysisRepository.findAll().size();

        // Create the ProfitabilityAnalysis with an existing ID
        profitabilityAnalysis.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfitabilityAnalysisMockMvc.perform(post("/api/profitability-analyses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profitabilityAnalysis)))
            .andExpect(status().isBadRequest());

        // Validate the ProfitabilityAnalysis in the database
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProfitabilityAnalysis in Elasticsearch
        verify(mockProfitabilityAnalysisSearchRepository, times(0)).save(profitabilityAnalysis);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = profitabilityAnalysisRepository.findAll().size();
        // set the field null
        profitabilityAnalysis.setDate(null);

        // Create the ProfitabilityAnalysis, which fails.

        restProfitabilityAnalysisMockMvc.perform(post("/api/profitability-analyses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profitabilityAnalysis)))
            .andExpect(status().isBadRequest());

        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResultIsRequired() throws Exception {
        int databaseSizeBeforeTest = profitabilityAnalysisRepository.findAll().size();
        // set the field null
        profitabilityAnalysis.setResult(null);

        // Create the ProfitabilityAnalysis, which fails.

        restProfitabilityAnalysisMockMvc.perform(post("/api/profitability-analyses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profitabilityAnalysis)))
            .andExpect(status().isBadRequest());

        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfitabilityAnalyses() throws Exception {
        // Initialize the database
        profitabilityAnalysisRepository.saveAndFlush(profitabilityAnalysis);

        // Get all the profitabilityAnalysisList
        restProfitabilityAnalysisMockMvc.perform(get("/api/profitability-analyses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profitabilityAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getProfitabilityAnalysis() throws Exception {
        // Initialize the database
        profitabilityAnalysisRepository.saveAndFlush(profitabilityAnalysis);

        // Get the profitabilityAnalysis
        restProfitabilityAnalysisMockMvc.perform(get("/api/profitability-analyses/{id}", profitabilityAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profitabilityAnalysis.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProfitabilityAnalysis() throws Exception {
        // Get the profitabilityAnalysis
        restProfitabilityAnalysisMockMvc.perform(get("/api/profitability-analyses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfitabilityAnalysis() throws Exception {
        // Initialize the database
        profitabilityAnalysisRepository.saveAndFlush(profitabilityAnalysis);

        int databaseSizeBeforeUpdate = profitabilityAnalysisRepository.findAll().size();

        // Update the profitabilityAnalysis
        ProfitabilityAnalysis updatedProfitabilityAnalysis = profitabilityAnalysisRepository.findById(profitabilityAnalysis.getId()).get();
        // Disconnect from session so that the updates on updatedProfitabilityAnalysis are not directly saved in db
        em.detach(updatedProfitabilityAnalysis);
        updatedProfitabilityAnalysis
            .date(UPDATED_DATE)
            .result(UPDATED_RESULT);

        restProfitabilityAnalysisMockMvc.perform(put("/api/profitability-analyses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfitabilityAnalysis)))
            .andExpect(status().isOk());

        // Validate the ProfitabilityAnalysis in the database
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeUpdate);
        ProfitabilityAnalysis testProfitabilityAnalysis = profitabilityAnalysisList.get(profitabilityAnalysisList.size() - 1);
        assertThat(testProfitabilityAnalysis.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProfitabilityAnalysis.isResult()).isEqualTo(UPDATED_RESULT);

        // Validate the ProfitabilityAnalysis in Elasticsearch
        verify(mockProfitabilityAnalysisSearchRepository, times(1)).save(testProfitabilityAnalysis);
    }

    @Test
    @Transactional
    public void updateNonExistingProfitabilityAnalysis() throws Exception {
        int databaseSizeBeforeUpdate = profitabilityAnalysisRepository.findAll().size();

        // Create the ProfitabilityAnalysis

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfitabilityAnalysisMockMvc.perform(put("/api/profitability-analyses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profitabilityAnalysis)))
            .andExpect(status().isBadRequest());

        // Validate the ProfitabilityAnalysis in the database
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfitabilityAnalysis in Elasticsearch
        verify(mockProfitabilityAnalysisSearchRepository, times(0)).save(profitabilityAnalysis);
    }

    @Test
    @Transactional
    public void deleteProfitabilityAnalysis() throws Exception {
        // Initialize the database
        profitabilityAnalysisRepository.saveAndFlush(profitabilityAnalysis);

        int databaseSizeBeforeDelete = profitabilityAnalysisRepository.findAll().size();

        // Delete the profitabilityAnalysis
        restProfitabilityAnalysisMockMvc.perform(delete("/api/profitability-analyses/{id}", profitabilityAnalysis.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProfitabilityAnalysis in Elasticsearch
        verify(mockProfitabilityAnalysisSearchRepository, times(1)).deleteById(profitabilityAnalysis.getId());
    }

    @Test
    @Transactional
    public void searchProfitabilityAnalysis() throws Exception {
        // Initialize the database
        profitabilityAnalysisRepository.saveAndFlush(profitabilityAnalysis);
        when(mockProfitabilityAnalysisSearchRepository.search(queryStringQuery("id:" + profitabilityAnalysis.getId())))
            .thenReturn(Collections.singletonList(profitabilityAnalysis));
        // Search the profitabilityAnalysis
        restProfitabilityAnalysisMockMvc.perform(get("/api/_search/profitability-analyses?query=id:" + profitabilityAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profitabilityAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.booleanValue())));
    }
}
