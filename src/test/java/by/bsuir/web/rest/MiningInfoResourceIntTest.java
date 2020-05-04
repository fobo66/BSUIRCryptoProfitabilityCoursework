package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;

import by.bsuir.domain.MiningInfo;
import by.bsuir.domain.Cryptocurrency;
import by.bsuir.repository.MiningInfoRepository;
import by.bsuir.repository.search.MiningInfoSearchRepository;
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
 * Test class for the MiningInfoResource REST controller.
 *
 * @see MiningInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseworkApp.class)
public class MiningInfoResourceIntTest {

    private static final Float DEFAULT_DIFFICULTY = 0F;
    private static final Float UPDATED_DIFFICULTY = 1F;

    private static final Integer DEFAULT_BLOCK_REWARD = 0;
    private static final Integer UPDATED_BLOCK_REWARD = 1;

    @Autowired
    private MiningInfoRepository miningInfoRepository;

    @Autowired
    private MiningInfoSearchRepository miningInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMiningInfoMockMvc;

    private MiningInfo miningInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MiningInfoResource miningInfoResource = new MiningInfoResource(miningInfoRepository, miningInfoSearchRepository);
        this.restMiningInfoMockMvc = MockMvcBuilders.standaloneSetup(miningInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

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
        Cryptocurrency cryptocurrency = CryptocurrencyResourceIntTest.createEntity(em);
        em.persist(cryptocurrency);
        em.flush();
        miningInfo.setCryptocurrency(cryptocurrency);
        return miningInfo;
    }

    @Before
    public void initTest() {
        miningInfoSearchRepository.deleteAll();
        miningInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createMiningInfo() throws Exception {
        int databaseSizeBeforeCreate = miningInfoRepository.findAll().size();

        // Create the MiningInfo
        restMiningInfoMockMvc.perform(post("/api/mining-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(miningInfo)))
            .andExpect(status().isCreated());

        // Validate the MiningInfo in the database
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeCreate + 1);
        MiningInfo testMiningInfo = miningInfoList.get(miningInfoList.size() - 1);
        assertThat(testMiningInfo.getDifficulty()).isEqualTo(DEFAULT_DIFFICULTY);
        assertThat(testMiningInfo.getBlockReward()).isEqualTo(DEFAULT_BLOCK_REWARD);

        // Validate the MiningInfo in Elasticsearch
        MiningInfo miningInfoEs = miningInfoSearchRepository.findOne(testMiningInfo.getId());
        assertThat(miningInfoEs).isEqualToComparingFieldByField(testMiningInfo);
    }

    @Test
    @Transactional
    public void createMiningInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = miningInfoRepository.findAll().size();

        // Create the MiningInfo with an existing ID
        miningInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMiningInfoMockMvc.perform(post("/api/mining-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(miningInfo)))
            .andExpect(status().isBadRequest());

        // Validate the MiningInfo in the database
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDifficultyIsRequired() throws Exception {
        int databaseSizeBeforeTest = miningInfoRepository.findAll().size();
        // set the field null
        miningInfo.setDifficulty(null);

        // Create the MiningInfo, which fails.

        restMiningInfoMockMvc.perform(post("/api/mining-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        miningInfoSearchRepository.save(miningInfo);
        int databaseSizeBeforeUpdate = miningInfoRepository.findAll().size();

        // Update the miningInfo
        MiningInfo updatedMiningInfo = miningInfoRepository.findOne(miningInfo.getId());
        updatedMiningInfo
            .difficulty(UPDATED_DIFFICULTY)
            .blockReward(UPDATED_BLOCK_REWARD);

        restMiningInfoMockMvc.perform(put("/api/mining-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMiningInfo)))
            .andExpect(status().isOk());

        // Validate the MiningInfo in the database
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeUpdate);
        MiningInfo testMiningInfo = miningInfoList.get(miningInfoList.size() - 1);
        assertThat(testMiningInfo.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
        assertThat(testMiningInfo.getBlockReward()).isEqualTo(UPDATED_BLOCK_REWARD);

        // Validate the MiningInfo in Elasticsearch
        MiningInfo miningInfoEs = miningInfoSearchRepository.findOne(testMiningInfo.getId());
        assertThat(miningInfoEs).isEqualToComparingFieldByField(testMiningInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingMiningInfo() throws Exception {
        int databaseSizeBeforeUpdate = miningInfoRepository.findAll().size();

        // Create the MiningInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMiningInfoMockMvc.perform(put("/api/mining-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(miningInfo)))
            .andExpect(status().isCreated());

        // Validate the MiningInfo in the database
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMiningInfo() throws Exception {
        // Initialize the database
        miningInfoRepository.saveAndFlush(miningInfo);
        miningInfoSearchRepository.save(miningInfo);
        int databaseSizeBeforeDelete = miningInfoRepository.findAll().size();

        // Get the miningInfo
        restMiningInfoMockMvc.perform(delete("/api/mining-infos/{id}", miningInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean miningInfoExistsInEs = miningInfoSearchRepository.exists(miningInfo.getId());
        assertThat(miningInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<MiningInfo> miningInfoList = miningInfoRepository.findAll();
        assertThat(miningInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMiningInfo() throws Exception {
        // Initialize the database
        miningInfoRepository.saveAndFlush(miningInfo);
        miningInfoSearchRepository.save(miningInfo);

        // Search the miningInfo
        restMiningInfoMockMvc.perform(get("/api/_search/mining-infos?query=id:" + miningInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(miningInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.doubleValue())))
            .andExpect(jsonPath("$.[*].blockReward").value(hasItem(DEFAULT_BLOCK_REWARD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MiningInfo.class);
        MiningInfo miningInfo1 = new MiningInfo();
        miningInfo1.setId(1L);
        MiningInfo miningInfo2 = new MiningInfo();
        miningInfo2.setId(miningInfo1.getId());
        assertThat(miningInfo1).isEqualTo(miningInfo2);
        miningInfo2.setId(2L);
        assertThat(miningInfo1).isNotEqualTo(miningInfo2);
        miningInfo1.setId(null);
        assertThat(miningInfo1).isNotEqualTo(miningInfo2);
    }
}
