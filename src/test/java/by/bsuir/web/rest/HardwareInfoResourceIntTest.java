package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;

import by.bsuir.domain.HardwareInfo;
import by.bsuir.domain.Videocard;
import by.bsuir.repository.HardwareInfoRepository;
import by.bsuir.repository.search.HardwareInfoSearchRepository;
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
 * Test class for the HardwareInfoResource REST controller.
 *
 * @see HardwareInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseworkApp.class)
public class HardwareInfoResourceIntTest {

    private static final Double DEFAULT_HASH_POWER = 0D;
    private static final Double UPDATED_HASH_POWER = 1D;

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    @Autowired
    private HardwareInfoRepository hardwareInfoRepository;

    @Autowired
    private HardwareInfoSearchRepository hardwareInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHardwareInfoMockMvc;

    private HardwareInfo hardwareInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HardwareInfoResource hardwareInfoResource = new HardwareInfoResource(hardwareInfoRepository, hardwareInfoSearchRepository);
        this.restHardwareInfoMockMvc = MockMvcBuilders.standaloneSetup(hardwareInfoResource)
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
    public static HardwareInfo createEntity(EntityManager em) {
        HardwareInfo hardwareInfo = new HardwareInfo()
            .hashPower(DEFAULT_HASH_POWER)
            .price(DEFAULT_PRICE);
        // Add required entity
        Videocard videocard = VideocardResourceIntTest.createEntity(em);
        em.persist(videocard);
        em.flush();
        hardwareInfo.setVideocard(videocard);
        return hardwareInfo;
    }

    @Before
    public void initTest() {
        hardwareInfoSearchRepository.deleteAll();
        hardwareInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createHardwareInfo() throws Exception {
        int databaseSizeBeforeCreate = hardwareInfoRepository.findAll().size();

        // Create the HardwareInfo
        restHardwareInfoMockMvc.perform(post("/api/hardware-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareInfo)))
            .andExpect(status().isCreated());

        // Validate the HardwareInfo in the database
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeCreate + 1);
        HardwareInfo testHardwareInfo = hardwareInfoList.get(hardwareInfoList.size() - 1);
        assertThat(testHardwareInfo.getHashPower()).isEqualTo(DEFAULT_HASH_POWER);
        assertThat(testHardwareInfo.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the HardwareInfo in Elasticsearch
        HardwareInfo hardwareInfoEs = hardwareInfoSearchRepository.findOne(testHardwareInfo.getId());
        assertThat(hardwareInfoEs).isEqualToComparingFieldByField(testHardwareInfo);
    }

    @Test
    @Transactional
    public void createHardwareInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hardwareInfoRepository.findAll().size();

        // Create the HardwareInfo with an existing ID
        hardwareInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHardwareInfoMockMvc.perform(post("/api/hardware-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareInfo)))
            .andExpect(status().isBadRequest());

        // Validate the HardwareInfo in the database
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHashPowerIsRequired() throws Exception {
        int databaseSizeBeforeTest = hardwareInfoRepository.findAll().size();
        // set the field null
        hardwareInfo.setHashPower(null);

        // Create the HardwareInfo, which fails.

        restHardwareInfoMockMvc.perform(post("/api/hardware-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        hardwareInfoSearchRepository.save(hardwareInfo);
        int databaseSizeBeforeUpdate = hardwareInfoRepository.findAll().size();

        // Update the hardwareInfo
        HardwareInfo updatedHardwareInfo = hardwareInfoRepository.findOne(hardwareInfo.getId());
        updatedHardwareInfo
            .hashPower(UPDATED_HASH_POWER)
            .price(UPDATED_PRICE);

        restHardwareInfoMockMvc.perform(put("/api/hardware-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHardwareInfo)))
            .andExpect(status().isOk());

        // Validate the HardwareInfo in the database
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeUpdate);
        HardwareInfo testHardwareInfo = hardwareInfoList.get(hardwareInfoList.size() - 1);
        assertThat(testHardwareInfo.getHashPower()).isEqualTo(UPDATED_HASH_POWER);
        assertThat(testHardwareInfo.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the HardwareInfo in Elasticsearch
        HardwareInfo hardwareInfoEs = hardwareInfoSearchRepository.findOne(testHardwareInfo.getId());
        assertThat(hardwareInfoEs).isEqualToComparingFieldByField(testHardwareInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingHardwareInfo() throws Exception {
        int databaseSizeBeforeUpdate = hardwareInfoRepository.findAll().size();

        // Create the HardwareInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHardwareInfoMockMvc.perform(put("/api/hardware-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareInfo)))
            .andExpect(status().isCreated());

        // Validate the HardwareInfo in the database
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHardwareInfo() throws Exception {
        // Initialize the database
        hardwareInfoRepository.saveAndFlush(hardwareInfo);
        hardwareInfoSearchRepository.save(hardwareInfo);
        int databaseSizeBeforeDelete = hardwareInfoRepository.findAll().size();

        // Get the hardwareInfo
        restHardwareInfoMockMvc.perform(delete("/api/hardware-infos/{id}", hardwareInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean hardwareInfoExistsInEs = hardwareInfoSearchRepository.exists(hardwareInfo.getId());
        assertThat(hardwareInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<HardwareInfo> hardwareInfoList = hardwareInfoRepository.findAll();
        assertThat(hardwareInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHardwareInfo() throws Exception {
        // Initialize the database
        hardwareInfoRepository.saveAndFlush(hardwareInfo);
        hardwareInfoSearchRepository.save(hardwareInfo);

        // Search the hardwareInfo
        restHardwareInfoMockMvc.perform(get("/api/_search/hardware-infos?query=id:" + hardwareInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hardwareInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].hashPower").value(hasItem(DEFAULT_HASH_POWER.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HardwareInfo.class);
        HardwareInfo hardwareInfo1 = new HardwareInfo();
        hardwareInfo1.setId(1L);
        HardwareInfo hardwareInfo2 = new HardwareInfo();
        hardwareInfo2.setId(hardwareInfo1.getId());
        assertThat(hardwareInfo1).isEqualTo(hardwareInfo2);
        hardwareInfo2.setId(2L);
        assertThat(hardwareInfo1).isNotEqualTo(hardwareInfo2);
        hardwareInfo1.setId(null);
        assertThat(hardwareInfo1).isNotEqualTo(hardwareInfo2);
    }
}
