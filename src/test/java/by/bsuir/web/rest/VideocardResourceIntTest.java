package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;

import by.bsuir.domain.Videocard;
import by.bsuir.repository.VideocardRepository;
import by.bsuir.repository.search.VideocardSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VideocardResource REST controller.
 *
 * @see VideocardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseworkApp.class)
public class VideocardResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_POWER = 1D;
    private static final Double UPDATED_POWER = 2D;

    @Autowired
    private VideocardRepository videocardRepository;

    @Autowired
    private VideocardSearchRepository videocardSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVideocardMockMvc;

    private Videocard videocard;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VideocardResource videocardResource = new VideocardResource(videocardRepository, videocardSearchRepository);
        this.restVideocardMockMvc = MockMvcBuilders.standaloneSetup(videocardResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Videocard createEntity(EntityManager em) {
        Videocard videocard = new Videocard()
            .name(DEFAULT_NAME)
            .power(DEFAULT_POWER);
        return videocard;
    }

    @Before
    public void initTest() {
        videocardSearchRepository.deleteAll();
        videocard = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideocard() throws Exception {
        int databaseSizeBeforeCreate = videocardRepository.findAll().size();

        // Create the Videocard
        restVideocardMockMvc.perform(post("/api/videocards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videocard)))
            .andExpect(status().isCreated());

        // Validate the Videocard in the database
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeCreate + 1);
        Videocard testVideocard = videocardList.get(videocardList.size() - 1);
        assertThat(testVideocard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVideocard.getPower()).isEqualTo(DEFAULT_POWER);

        // Validate the Videocard in Elasticsearch
        Videocard videocardEs = videocardSearchRepository.findOne(testVideocard.getId());
        assertThat(videocardEs).isEqualToComparingFieldByField(testVideocard);
    }

    @Test
    @Transactional
    public void createVideocardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videocardRepository.findAll().size();

        // Create the Videocard with an existing ID
        videocard.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideocardMockMvc.perform(post("/api/videocards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videocard)))
            .andExpect(status().isBadRequest());

        // Validate the Videocard in the database
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVideocards() throws Exception {
        // Initialize the database
        videocardRepository.saveAndFlush(videocard);

        // Get all the videocardList
        restVideocardMockMvc.perform(get("/api/videocards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videocard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].power").value(hasItem(DEFAULT_POWER.doubleValue())));
    }

    @Test
    @Transactional
    public void getVideocard() throws Exception {
        // Initialize the database
        videocardRepository.saveAndFlush(videocard);

        // Get the videocard
        restVideocardMockMvc.perform(get("/api/videocards/{id}", videocard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(videocard.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.power").value(DEFAULT_POWER.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVideocard() throws Exception {
        // Get the videocard
        restVideocardMockMvc.perform(get("/api/videocards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideocard() throws Exception {
        // Initialize the database
        videocardRepository.saveAndFlush(videocard);
        videocardSearchRepository.save(videocard);
        int databaseSizeBeforeUpdate = videocardRepository.findAll().size();

        // Update the videocard
        Videocard updatedVideocard = videocardRepository.findOne(videocard.getId());
        updatedVideocard
            .name(UPDATED_NAME)
            .power(UPDATED_POWER);

        restVideocardMockMvc.perform(put("/api/videocards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideocard)))
            .andExpect(status().isOk());

        // Validate the Videocard in the database
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeUpdate);
        Videocard testVideocard = videocardList.get(videocardList.size() - 1);
        assertThat(testVideocard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVideocard.getPower()).isEqualTo(UPDATED_POWER);

        // Validate the Videocard in Elasticsearch
        Videocard videocardEs = videocardSearchRepository.findOne(testVideocard.getId());
        assertThat(videocardEs).isEqualToComparingFieldByField(testVideocard);
    }

    @Test
    @Transactional
    public void updateNonExistingVideocard() throws Exception {
        int databaseSizeBeforeUpdate = videocardRepository.findAll().size();

        // Create the Videocard

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVideocardMockMvc.perform(put("/api/videocards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videocard)))
            .andExpect(status().isCreated());

        // Validate the Videocard in the database
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVideocard() throws Exception {
        // Initialize the database
        videocardRepository.saveAndFlush(videocard);
        videocardSearchRepository.save(videocard);
        int databaseSizeBeforeDelete = videocardRepository.findAll().size();

        // Get the videocard
        restVideocardMockMvc.perform(delete("/api/videocards/{id}", videocard.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean videocardExistsInEs = videocardSearchRepository.exists(videocard.getId());
        assertThat(videocardExistsInEs).isFalse();

        // Validate the database is empty
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVideocard() throws Exception {
        // Initialize the database
        videocardRepository.saveAndFlush(videocard);
        videocardSearchRepository.save(videocard);

        // Search the videocard
        restVideocardMockMvc.perform(get("/api/_search/videocards?query=id:" + videocard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videocard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].power").value(hasItem(DEFAULT_POWER.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Videocard.class);
        Videocard videocard1 = new Videocard();
        videocard1.setId(1L);
        Videocard videocard2 = new Videocard();
        videocard2.setId(videocard1.getId());
        assertThat(videocard1).isEqualTo(videocard2);
        videocard2.setId(2L);
        assertThat(videocard1).isNotEqualTo(videocard2);
        videocard1.setId(null);
        assertThat(videocard1).isNotEqualTo(videocard2);
    }
}
