package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;
import by.bsuir.domain.Videocard;
import by.bsuir.repository.VideocardRepository;
import by.bsuir.repository.search.VideocardSearchRepository;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VideocardResource} REST controller.
 */
@SpringBootTest(classes = CourseworkApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class VideocardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_POWER = 1D;
    private static final Double UPDATED_POWER = 2D;

    @Autowired
    private VideocardRepository videocardRepository;

    /**
     * This repository is mocked in the by.bsuir.repository.search test package.
     *
     * @see by.bsuir.repository.search.VideocardSearchRepositoryMockConfiguration
     */
    @Autowired
    private VideocardSearchRepository mockVideocardSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideocardMockMvc;

    private Videocard videocard;

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
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Videocard createUpdatedEntity(EntityManager em) {
        Videocard videocard = new Videocard()
            .name(UPDATED_NAME)
            .power(UPDATED_POWER);
        return videocard;
    }

    @BeforeEach
    public void initTest() {
        videocard = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideocard() throws Exception {
        int databaseSizeBeforeCreate = videocardRepository.findAll().size();

        // Create the Videocard
        restVideocardMockMvc.perform(post("/api/videocards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videocard)))
            .andExpect(status().isCreated());

        // Validate the Videocard in the database
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeCreate + 1);
        Videocard testVideocard = videocardList.get(videocardList.size() - 1);
        assertThat(testVideocard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVideocard.getPower()).isEqualTo(DEFAULT_POWER);

        // Validate the Videocard in Elasticsearch
        verify(mockVideocardSearchRepository, times(1)).save(testVideocard);
    }

    @Test
    @Transactional
    public void createVideocardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videocardRepository.findAll().size();

        // Create the Videocard with an existing ID
        videocard.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideocardMockMvc.perform(post("/api/videocards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videocard)))
            .andExpect(status().isBadRequest());

        // Validate the Videocard in the database
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeCreate);

        // Validate the Videocard in Elasticsearch
        verify(mockVideocardSearchRepository, times(0)).save(videocard);
    }


    @Test
    @Transactional
    public void getAllVideocards() throws Exception {
        // Initialize the database
        videocardRepository.saveAndFlush(videocard);

        // Get all the videocardList
        restVideocardMockMvc.perform(get("/api/videocards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videocard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(videocard.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
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

        int databaseSizeBeforeUpdate = videocardRepository.findAll().size();

        // Update the videocard
        Videocard updatedVideocard = videocardRepository.findById(videocard.getId()).get();
        // Disconnect from session so that the updates on updatedVideocard are not directly saved in db
        em.detach(updatedVideocard);
        updatedVideocard
            .name(UPDATED_NAME)
            .power(UPDATED_POWER);

        restVideocardMockMvc.perform(put("/api/videocards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideocard)))
            .andExpect(status().isOk());

        // Validate the Videocard in the database
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeUpdate);
        Videocard testVideocard = videocardList.get(videocardList.size() - 1);
        assertThat(testVideocard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVideocard.getPower()).isEqualTo(UPDATED_POWER);

        // Validate the Videocard in Elasticsearch
        verify(mockVideocardSearchRepository, times(1)).save(testVideocard);
    }

    @Test
    @Transactional
    public void updateNonExistingVideocard() throws Exception {
        int databaseSizeBeforeUpdate = videocardRepository.findAll().size();

        // Create the Videocard

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideocardMockMvc.perform(put("/api/videocards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videocard)))
            .andExpect(status().isBadRequest());

        // Validate the Videocard in the database
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Videocard in Elasticsearch
        verify(mockVideocardSearchRepository, times(0)).save(videocard);
    }

    @Test
    @Transactional
    public void deleteVideocard() throws Exception {
        // Initialize the database
        videocardRepository.saveAndFlush(videocard);

        int databaseSizeBeforeDelete = videocardRepository.findAll().size();

        // Delete the videocard
        restVideocardMockMvc.perform(delete("/api/videocards/{id}", videocard.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Videocard> videocardList = videocardRepository.findAll();
        assertThat(videocardList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Videocard in Elasticsearch
        verify(mockVideocardSearchRepository, times(1)).deleteById(videocard.getId());
    }

    @Test
    @Transactional
    public void searchVideocard() throws Exception {
        // Initialize the database
        videocardRepository.saveAndFlush(videocard);
        when(mockVideocardSearchRepository.search(queryStringQuery("id:" + videocard.getId())))
            .thenReturn(Collections.singletonList(videocard));
        // Search the videocard
        restVideocardMockMvc.perform(get("/api/_search/videocards?query=id:" + videocard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videocard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].power").value(hasItem(DEFAULT_POWER.doubleValue())));
    }
}
