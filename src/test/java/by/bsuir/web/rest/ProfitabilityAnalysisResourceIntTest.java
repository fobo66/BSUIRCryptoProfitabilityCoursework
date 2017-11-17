package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;

import by.bsuir.domain.ProfitabilityAnalysis;
import by.bsuir.domain.User;
import by.bsuir.repository.ProfitabilityAnalysisRepository;
import by.bsuir.repository.search.ProfitabilityAnalysisSearchRepository;
import by.bsuir.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProfitabilityAnalysisResource REST controller.
 *
 * @see ProfitabilityAnalysisResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseworkApp.class)
public class ProfitabilityAnalysisResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_RESULT = false;
    private static final Boolean UPDATED_RESULT = true;

    @Autowired
    private ProfitabilityAnalysisRepository profitabilityAnalysisRepository;

    @Autowired
    private ProfitabilityAnalysisSearchRepository profitabilityAnalysisSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfitabilityAnalysisMockMvc;

    private ProfitabilityAnalysis profitabilityAnalysis;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfitabilityAnalysis createEntity(EntityManager em) {
        ProfitabilityAnalysis profitabilityAnalysis = new ProfitabilityAnalysis()
            .date(DEFAULT_DATE)
            .result(DEFAULT_RESULT);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        profitabilityAnalysis.setUser(user);
        return profitabilityAnalysis;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfitabilityAnalysisResource profitabilityAnalysisResource = new ProfitabilityAnalysisResource(profitabilityAnalysisRepository, profitabilityAnalysisSearchRepository);
        this.restProfitabilityAnalysisMockMvc = MockMvcBuilders.standaloneSetup(profitabilityAnalysisResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        profitabilityAnalysisSearchRepository.deleteAll();
        profitabilityAnalysis = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfitabilityAnalysis() throws Exception {
        int databaseSizeBeforeCreate = profitabilityAnalysisRepository.findAll().size();

        // Create the ProfitabilityAnalysis
        restProfitabilityAnalysisMockMvc.perform(post("/api/profitability-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profitabilityAnalysis)))
            .andExpect(status().isCreated());

        // Validate the ProfitabilityAnalysis in the database
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeCreate + 1);
        ProfitabilityAnalysis testProfitabilityAnalysis = profitabilityAnalysisList.get(profitabilityAnalysisList.size() - 1);
        assertThat(testProfitabilityAnalysis.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProfitabilityAnalysis.isResult()).isEqualTo(DEFAULT_RESULT);

        // Validate the ProfitabilityAnalysis in Elasticsearch
        ProfitabilityAnalysis profitabilityAnalysisEs = profitabilityAnalysisSearchRepository.findOne(testProfitabilityAnalysis.getId());
        assertThat(profitabilityAnalysisEs).isEqualToComparingFieldByField(testProfitabilityAnalysis);
    }

    @Test
    @Transactional
    public void createProfitabilityAnalysisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profitabilityAnalysisRepository.findAll().size();

        // Create the ProfitabilityAnalysis with an existing ID
        profitabilityAnalysis.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfitabilityAnalysisMockMvc.perform(post("/api/profitability-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profitabilityAnalysis)))
            .andExpect(status().isBadRequest());

        // Validate the ProfitabilityAnalysis in the database
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = profitabilityAnalysisRepository.findAll().size();
        // set the field null
        profitabilityAnalysis.setDate(null);

        // Create the ProfitabilityAnalysis, which fails.

        restProfitabilityAnalysisMockMvc.perform(post("/api/profitability-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        profitabilityAnalysisSearchRepository.save(profitabilityAnalysis);
        int databaseSizeBeforeUpdate = profitabilityAnalysisRepository.findAll().size();

        // Update the profitabilityAnalysis
        ProfitabilityAnalysis updatedProfitabilityAnalysis = profitabilityAnalysisRepository.findOne(profitabilityAnalysis.getId());
        updatedProfitabilityAnalysis
            .date(UPDATED_DATE)
            .result(UPDATED_RESULT);

        restProfitabilityAnalysisMockMvc.perform(put("/api/profitability-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfitabilityAnalysis)))
            .andExpect(status().isOk());

        // Validate the ProfitabilityAnalysis in the database
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeUpdate);
        ProfitabilityAnalysis testProfitabilityAnalysis = profitabilityAnalysisList.get(profitabilityAnalysisList.size() - 1);
        assertThat(testProfitabilityAnalysis.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProfitabilityAnalysis.isResult()).isEqualTo(UPDATED_RESULT);

        // Validate the ProfitabilityAnalysis in Elasticsearch
        ProfitabilityAnalysis profitabilityAnalysisEs = profitabilityAnalysisSearchRepository.findOne(testProfitabilityAnalysis.getId());
        assertThat(profitabilityAnalysisEs).isEqualToComparingFieldByField(testProfitabilityAnalysis);
    }

    @Test
    @Transactional
    public void updateNonExistingProfitabilityAnalysis() throws Exception {
        int databaseSizeBeforeUpdate = profitabilityAnalysisRepository.findAll().size();

        // Create the ProfitabilityAnalysis

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProfitabilityAnalysisMockMvc.perform(put("/api/profitability-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profitabilityAnalysis)))
            .andExpect(status().isCreated());

        // Validate the ProfitabilityAnalysis in the database
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProfitabilityAnalysis() throws Exception {
        // Initialize the database
        profitabilityAnalysisRepository.saveAndFlush(profitabilityAnalysis);
        profitabilityAnalysisSearchRepository.save(profitabilityAnalysis);
        int databaseSizeBeforeDelete = profitabilityAnalysisRepository.findAll().size();

        // Get the profitabilityAnalysis
        restProfitabilityAnalysisMockMvc.perform(delete("/api/profitability-analyses/{id}", profitabilityAnalysis.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean profitabilityAnalysisExistsInEs = profitabilityAnalysisSearchRepository.exists(profitabilityAnalysis.getId());
        assertThat(profitabilityAnalysisExistsInEs).isFalse();

        // Validate the database is empty
        List<ProfitabilityAnalysis> profitabilityAnalysisList = profitabilityAnalysisRepository.findAll();
        assertThat(profitabilityAnalysisList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProfitabilityAnalysis() throws Exception {
        // Initialize the database
        profitabilityAnalysisRepository.saveAndFlush(profitabilityAnalysis);
        profitabilityAnalysisSearchRepository.save(profitabilityAnalysis);

        // Search the profitabilityAnalysis
        restProfitabilityAnalysisMockMvc.perform(get("/api/_search/profitability-analyses?query=id:" + profitabilityAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profitabilityAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfitabilityAnalysis.class);
        ProfitabilityAnalysis profitabilityAnalysis1 = new ProfitabilityAnalysis();
        profitabilityAnalysis1.setId(1L);
        ProfitabilityAnalysis profitabilityAnalysis2 = new ProfitabilityAnalysis();
        profitabilityAnalysis2.setId(profitabilityAnalysis1.getId());
        assertThat(profitabilityAnalysis1).isEqualTo(profitabilityAnalysis2);
        profitabilityAnalysis2.setId(2L);
        assertThat(profitabilityAnalysis1).isNotEqualTo(profitabilityAnalysis2);
        profitabilityAnalysis1.setId(null);
        assertThat(profitabilityAnalysis1).isNotEqualTo(profitabilityAnalysis2);
    }
}
