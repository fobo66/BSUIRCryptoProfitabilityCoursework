package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;
import by.bsuir.domain.MiningInfo;
import by.bsuir.domain.Cryptocurrency;
import by.bsuir.repository.MiningInfoRepository;
import by.bsuir.repository.search.MiningInfoSearchRepository;

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
 * Integration tests for the {@link MiningInfoResource} REST controller.
 */
@SpringBootTest(classes = CourseworkApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class MiningInfoResourceIT {

    private static final Float DEFAULT_DIFFICULTY = 0F;
    private static final Float UPDATED_DIFFICULTY = 1F;

    private static final Integer DEFAULT_BLOCK_REWARD = 0;
    private static final Integer UPDATED_BLOCK_REWARD = 1;

    @Autowired
    private MiningInfoRepository miningInfoRepository;

    /**
     * This repository is mocked in the by.bsuir.repository.search test package.
     *
     * @see by.bsuir.repository.search.MiningInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private MiningInfoSearchRepository mockMiningInfoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMiningInfoMockMvc;

    private MiningInfo miningInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MiningInfo createEntity(EntityManager em) {
        MiningInfo miningInfo = new MiningInfo()
            .difficulty(DEFAULT_DIFFICULTY)
            .blockReward(DEFAULT_BLOCK_REWARD);
        // Add required entity
        Cryptocurrency cryptocurrency;
        if (TestUtil.findAll(em, Cryptocurrency.class).isEmpty()) {
            cryptocurrency = CryptocurrencyResourceIT.createEntity(em);
            em.persist(cryptocurrency);
            em.flush();
        } else {
            cryptocurrency = TestUtil.findAll(em, Cryptocurrency.class).get(0);
        }
        miningInfo.setCryptocurrency(cryptocurrency);
        return miningInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MiningInfo createUpdatedEntity(EntityManager em) {
        MiningInfo miningInfo = new MiningInfo()
            .difficulty(UPDATED_DIFFICULTY)
            .blockReward(UPDATED_BLOCK_REWARD);
        // Add required entity
        Cryptocurrency cryptocurrency;
        if (TestUtil.findAll(em, Cryptocurrency.class).isEmpty()) {
            cryptocurrency = CryptocurrencyResourceIT.createUpdatedEntity(em);
            em.persist(cryptocurrency);
            em.flush();
        } else {
            cryptocurrency = TestUtil.findAll(em, Cryptocurrency.class).get(0);
        }
        miningInfo.setCryptocurrency(cryptocurrency);
        return miningInfo;
    }

    @BeforeEach
    public void initTest() {
        miningInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createMiningInfo() throws Exception {
        int databaseSizeBeforeCreate = miningInfoRepository.findAll().size();

        // Create the MiningInfo
        restMiningInfoMockMvc.perform(post("/api/mining-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(miningInfo)))
            .andExpect(status().isCreated());

        // Validate the MiningInfo in the database
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeCreate + 1);
        MiningInfo testMiningInfo = miningInfoList.get(miningInfoList.size() - 1);
        assertThat(testMiningInfo.getDifficulty()).isEqualTo(DEFAULT_DIFFICULTY);
        assertThat(testMiningInfo.getBlockReward()).isEqualTo(DEFAULT_BLOCK_REWARD);

        // Validate the MiningInfo in Elasticsearch
        verify(mockMiningInfoSearchRepository, times(1)).save(testMiningInfo);
    }

    @Test
    @Transactional
    public void createMiningInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = miningInfoRepository.findAll().size();

        // Create the MiningInfo with an existing ID
        miningInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMiningInfoMockMvc.perform(post("/api/mining-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(miningInfo)))
            .andExpect(status().isBadRequest());

        // Validate the MiningInfo in the database
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the MiningInfo in Elasticsearch
        verify(mockMiningInfoSearchRepository, times(0)).save(miningInfo);
    }


    @Test
    @Transactional
    public void checkDifficultyIsRequired() throws Exception {
        int databaseSizeBeforeTest = miningInfoRepository.findAll().size();
        // set the field null
        miningInfo.setDifficulty(null);

        // Create the MiningInfo, which fails.

        restMiningInfoMockMvc.perform(post("/api/mining-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(miningInfo)))
            .andExpect(status().isBadRequest());

        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBlockRewardIsRequired() throws Exception {
        int databaseSizeBeforeTest = miningInfoRepository.findAll().size();
        // set the field null
        miningInfo.setBlockReward(null);

        // Create the MiningInfo, which fails.

        restMiningInfoMockMvc.perform(post("/api/mining-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(miningInfo)))
            .andExpect(status().isBadRequest());

        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMiningInfos() throws Exception {
        // Initialize the database
        miningInfoRepository.saveAndFlush(miningInfo);

        // Get all the miningInfoList
        restMiningInfoMockMvc.perform(get("/api/mining-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(miningInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.doubleValue())))
            .andExpect(jsonPath("$.[*].blockReward").value(hasItem(DEFAULT_BLOCK_REWARD)));
    }
    
    @Test
    @Transactional
    public void getMiningInfo() throws Exception {
        // Initialize the database
        miningInfoRepository.saveAndFlush(miningInfo);

        // Get the miningInfo
        restMiningInfoMockMvc.perform(get("/api/mining-infos/{id}", miningInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(miningInfo.getId().intValue()))
            .andExpect(jsonPath("$.difficulty").value(DEFAULT_DIFFICULTY.doubleValue()))
            .andExpect(jsonPath("$.blockReward").value(DEFAULT_BLOCK_REWARD));
    }

    @Test
    @Transactional
    public void getNonExistingMiningInfo() throws Exception {
        // Get the miningInfo
        restMiningInfoMockMvc.perform(get("/api/mining-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMiningInfo() throws Exception {
        // Initialize the database
        miningInfoRepository.saveAndFlush(miningInfo);

        int databaseSizeBeforeUpdate = miningInfoRepository.findAll().size();

        // Update the miningInfo
        MiningInfo updatedMiningInfo = miningInfoRepository.findById(miningInfo.getId()).get();
        // Disconnect from session so that the updates on updatedMiningInfo are not directly saved in db
        em.detach(updatedMiningInfo);
        updatedMiningInfo
            .difficulty(UPDATED_DIFFICULTY)
            .blockReward(UPDATED_BLOCK_REWARD);

        restMiningInfoMockMvc.perform(put("/api/mining-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMiningInfo)))
            .andExpect(status().isOk());

        // Validate the MiningInfo in the database
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeUpdate);
        MiningInfo testMiningInfo = miningInfoList.get(miningInfoList.size() - 1);
        assertThat(testMiningInfo.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
        assertThat(testMiningInfo.getBlockReward()).isEqualTo(UPDATED_BLOCK_REWARD);

        // Validate the MiningInfo in Elasticsearch
        verify(mockMiningInfoSearchRepository, times(1)).save(testMiningInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingMiningInfo() throws Exception {
        int databaseSizeBeforeUpdate = miningInfoRepository.findAll().size();

        // Create the MiningInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMiningInfoMockMvc.perform(put("/api/mining-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(miningInfo)))
            .andExpect(status().isBadRequest());

        // Validate the MiningInfo in the database
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MiningInfo in Elasticsearch
        verify(mockMiningInfoSearchRepository, times(0)).save(miningInfo);
    }

    @Test
    @Transactional
    public void deleteMiningInfo() throws Exception {
        // Initialize the database
        miningInfoRepository.saveAndFlush(miningInfo);

        int databaseSizeBeforeDelete = miningInfoRepository.findAll().size();

        // Delete the miningInfo
        restMiningInfoMockMvc.perform(delete("/api/mining-infos/{id}", miningInfo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MiningInfo in Elasticsearch
        verify(mockMiningInfoSearchRepository, times(1)).deleteById(miningInfo.getId());
    }

    @Test
    @Transactional
    public void searchMiningInfo() throws Exception {
        // Initialize the database
        miningInfoRepository.saveAndFlush(miningInfo);
        when(mockMiningInfoSearchRepository.search(queryStringQuery("id:" + miningInfo.getId())))
            .thenReturn(Collections.singletonList(miningInfo));
        // Search the miningInfo
        restMiningInfoMockMvc.perform(get("/api/_search/mining-infos?query=id:" + miningInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(miningInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.doubleValue())))
            .andExpect(jsonPath("$.[*].blockReward").value(hasItem(DEFAULT_BLOCK_REWARD)));
    }
}
