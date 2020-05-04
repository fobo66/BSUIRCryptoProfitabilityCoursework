package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;
import by.bsuir.domain.HardwareInfo;
import by.bsuir.domain.Videocard;
import by.bsuir.repository.HardwareInfoRepository;
import by.bsuir.repository.search.HardwareInfoSearchRepository;

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
 * Integration tests for the {@link HardwareInfoResource} REST controller.
 */
@SpringBootTest(classes = CourseworkApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class HardwareInfoResourceIT {

    private static final Double DEFAULT_HASH_POWER = 0D;
    private static final Double UPDATED_HASH_POWER = 1D;

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    @Autowired
    private HardwareInfoRepository hardwareInfoRepository;

    /**
     * This repository is mocked in the by.bsuir.repository.search test package.
     *
     * @see by.bsuir.repository.search.HardwareInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private HardwareInfoSearchRepository mockHardwareInfoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHardwareInfoMockMvc;

    private HardwareInfo hardwareInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HardwareInfo createEntity(EntityManager em) {
        HardwareInfo hardwareInfo = new HardwareInfo()
            .hashPower(DEFAULT_HASH_POWER)
            .price(DEFAULT_PRICE);
        // Add required entity
        Videocard videocard;
        if (TestUtil.findAll(em, Videocard.class).isEmpty()) {
            videocard = VideocardResourceIT.createEntity(em);
            em.persist(videocard);
            em.flush();
        } else {
            videocard = TestUtil.findAll(em, Videocard.class).get(0);
        }
        hardwareInfo.setVideocard(videocard);
        return hardwareInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HardwareInfo createUpdatedEntity(EntityManager em) {
        HardwareInfo hardwareInfo = new HardwareInfo()
            .hashPower(UPDATED_HASH_POWER)
            .price(UPDATED_PRICE);
        // Add required entity
        Videocard videocard;
        if (TestUtil.findAll(em, Videocard.class).isEmpty()) {
            videocard = VideocardResourceIT.createUpdatedEntity(em);
            em.persist(videocard);
            em.flush();
        } else {
            videocard = TestUtil.findAll(em, Videocard.class).get(0);
        }
        hardwareInfo.setVideocard(videocard);
        return hardwareInfo;
    }

    @BeforeEach
    public void initTest() {
        hardwareInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createHardwareInfo() throws Exception {
        int databaseSizeBeforeCreate = hardwareInfoRepository.findAll().size();

        // Create the HardwareInfo
        restHardwareInfoMockMvc.perform(post("/api/hardware-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hardwareInfo)))
            .andExpect(status().isCreated());

        // Validate the HardwareInfo in the database
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeCreate + 1);
        HardwareInfo testHardwareInfo = hardwareInfoList.get(hardwareInfoList.size() - 1);
        assertThat(testHardwareInfo.getHashPower()).isEqualTo(DEFAULT_HASH_POWER);
        assertThat(testHardwareInfo.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the HardwareInfo in Elasticsearch
        verify(mockHardwareInfoSearchRepository, times(1)).save(testHardwareInfo);
    }

    @Test
    @Transactional
    public void createHardwareInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hardwareInfoRepository.findAll().size();

        // Create the HardwareInfo with an existing ID
        hardwareInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHardwareInfoMockMvc.perform(post("/api/hardware-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hardwareInfo)))
            .andExpect(status().isBadRequest());

        // Validate the HardwareInfo in the database
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the HardwareInfo in Elasticsearch
        verify(mockHardwareInfoSearchRepository, times(0)).save(hardwareInfo);
    }


    @Test
    @Transactional
    public void checkHashPowerIsRequired() throws Exception {
        int databaseSizeBeforeTest = hardwareInfoRepository.findAll().size();
        // set the field null
        hardwareInfo.setHashPower(null);

        // Create the HardwareInfo, which fails.

        restHardwareInfoMockMvc.perform(post("/api/hardware-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hardwareInfo)))
            .andExpect(status().isBadRequest());

        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = hardwareInfoRepository.findAll().size();
        // set the field null
        hardwareInfo.setPrice(null);

        // Create the HardwareInfo, which fails.

        restHardwareInfoMockMvc.perform(post("/api/hardware-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hardwareInfo)))
            .andExpect(status().isBadRequest());

        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHardwareInfos() throws Exception {
        // Initialize the database
        hardwareInfoRepository.saveAndFlush(hardwareInfo);

        // Get all the hardwareInfoList
        restHardwareInfoMockMvc.perform(get("/api/hardware-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hardwareInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].hashPower").value(hasItem(DEFAULT_HASH_POWER.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getHardwareInfo() throws Exception {
        // Initialize the database
        hardwareInfoRepository.saveAndFlush(hardwareInfo);

        // Get the hardwareInfo
        restHardwareInfoMockMvc.perform(get("/api/hardware-infos/{id}", hardwareInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hardwareInfo.getId().intValue()))
            .andExpect(jsonPath("$.hashPower").value(DEFAULT_HASH_POWER.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHardwareInfo() throws Exception {
        // Get the hardwareInfo
        restHardwareInfoMockMvc.perform(get("/api/hardware-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHardwareInfo() throws Exception {
        // Initialize the database
        hardwareInfoRepository.saveAndFlush(hardwareInfo);

        int databaseSizeBeforeUpdate = hardwareInfoRepository.findAll().size();

        // Update the hardwareInfo
        HardwareInfo updatedHardwareInfo = hardwareInfoRepository.findById(hardwareInfo.getId()).get();
        // Disconnect from session so that the updates on updatedHardwareInfo are not directly saved in db
        em.detach(updatedHardwareInfo);
        updatedHardwareInfo
            .hashPower(UPDATED_HASH_POWER)
            .price(UPDATED_PRICE);

        restHardwareInfoMockMvc.perform(put("/api/hardware-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHardwareInfo)))
            .andExpect(status().isOk());

        // Validate the HardwareInfo in the database
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeUpdate);
        HardwareInfo testHardwareInfo = hardwareInfoList.get(hardwareInfoList.size() - 1);
        assertThat(testHardwareInfo.getHashPower()).isEqualTo(UPDATED_HASH_POWER);
        assertThat(testHardwareInfo.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the HardwareInfo in Elasticsearch
        verify(mockHardwareInfoSearchRepository, times(1)).save(testHardwareInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingHardwareInfo() throws Exception {
        int databaseSizeBeforeUpdate = hardwareInfoRepository.findAll().size();

        // Create the HardwareInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHardwareInfoMockMvc.perform(put("/api/hardware-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hardwareInfo)))
            .andExpect(status().isBadRequest());

        // Validate the HardwareInfo in the database
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HardwareInfo in Elasticsearch
        verify(mockHardwareInfoSearchRepository, times(0)).save(hardwareInfo);
    }

    @Test
    @Transactional
    public void deleteHardwareInfo() throws Exception {
        // Initialize the database
        hardwareInfoRepository.saveAndFlush(hardwareInfo);

        int databaseSizeBeforeDelete = hardwareInfoRepository.findAll().size();

        // Delete the hardwareInfo
        restHardwareInfoMockMvc.perform(delete("/api/hardware-infos/{id}", hardwareInfo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HardwareInfo in Elasticsearch
        verify(mockHardwareInfoSearchRepository, times(1)).deleteById(hardwareInfo.getId());
    }

    @Test
    @Transactional
    public void searchHardwareInfo() throws Exception {
        // Initialize the database
        hardwareInfoRepository.saveAndFlush(hardwareInfo);
        when(mockHardwareInfoSearchRepository.search(queryStringQuery("id:" + hardwareInfo.getId())))
            .thenReturn(Collections.singletonList(hardwareInfo));
        // Search the hardwareInfo
        restHardwareInfoMockMvc.perform(get("/api/_search/hardware-infos?query=id:" + hardwareInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hardwareInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].hashPower").value(hasItem(DEFAULT_HASH_POWER.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
}
