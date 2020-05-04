package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;

import by.bsuir.domain.PowerCost;
import by.bsuir.repository.PowerCostRepository;
import by.bsuir.repository.search.PowerCostSearchRepository;
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
 * Test class for the PowerCostResource REST controller.
 *
 * @see PowerCostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseworkApp.class)
public class PowerCostResourceIntTest {

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE_PER_KILOWATT = 0D;
    private static final Double UPDATED_PRICE_PER_KILOWATT = 1D;

    @Autowired
    private PowerCostRepository powerCostRepository;

    @Autowired
    private PowerCostSearchRepository powerCostSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPowerCostMockMvc;

    private PowerCost powerCost;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PowerCostResource powerCostResource = new PowerCostResource(powerCostRepository, powerCostSearchRepository);
        this.restPowerCostMockMvc = MockMvcBuilders.standaloneSetup(powerCostResource)
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
    public static PowerCost createEntity(EntityManager em) {
        PowerCost powerCost = new PowerCost()
            .city(DEFAULT_CITY)
            .pricePerKilowatt(DEFAULT_PRICE_PER_KILOWATT);
        return powerCost;
    }

    @Before
    public void initTest() {
        powerCostSearchRepository.deleteAll();
        powerCost = createEntity(em);
    }

    @Test
    @Transactional
    public void createPowerCost() throws Exception {
        int databaseSizeBeforeCreate = powerCostRepository.findAll().size();

        // Create the PowerCost
        restPowerCostMockMvc.perform(post("/api/power-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(powerCost)))
            .andExpect(status().isCreated());

        // Validate the PowerCost in the database
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeCreate + 1);
        PowerCost testPowerCost = powerCostList.get(powerCostList.size() - 1);
        assertThat(testPowerCost.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPowerCost.getPricePerKilowatt()).isEqualTo(DEFAULT_PRICE_PER_KILOWATT);

        // Validate the PowerCost in Elasticsearch
        PowerCost powerCostEs = powerCostSearchRepository.findOne(testPowerCost.getId());
        assertThat(powerCostEs).isEqualToComparingFieldByField(testPowerCost);
    }

    @Test
    @Transactional
    public void createPowerCostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = powerCostRepository.findAll().size();

        // Create the PowerCost with an existing ID
        powerCost.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPowerCostMockMvc.perform(post("/api/power-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(powerCost)))
            .andExpect(status().isBadRequest());

        // Validate the PowerCost in the database
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = powerCostRepository.findAll().size();
        // set the field null
        powerCost.setCity(null);

        // Create the PowerCost, which fails.

        restPowerCostMockMvc.perform(post("/api/power-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(powerCost)))
            .andExpect(status().isBadRequest());

        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPowerCosts() throws Exception {
        // Initialize the database
        powerCostRepository.saveAndFlush(powerCost);

        // Get all the powerCostList
        restPowerCostMockMvc.perform(get("/api/power-costs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(powerCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].pricePerKilowatt").value(hasItem(DEFAULT_PRICE_PER_KILOWATT.doubleValue())));
    }

    @Test
    @Transactional
    public void getPowerCost() throws Exception {
        // Initialize the database
        powerCostRepository.saveAndFlush(powerCost);

        // Get the powerCost
        restPowerCostMockMvc.perform(get("/api/power-costs/{id}", powerCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(powerCost.getId().intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.pricePerKilowatt").value(DEFAULT_PRICE_PER_KILOWATT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPowerCost() throws Exception {
        // Get the powerCost
        restPowerCostMockMvc.perform(get("/api/power-costs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePowerCost() throws Exception {
        // Initialize the database
        powerCostRepository.saveAndFlush(powerCost);
        powerCostSearchRepository.save(powerCost);
        int databaseSizeBeforeUpdate = powerCostRepository.findAll().size();

        // Update the powerCost
        PowerCost updatedPowerCost = powerCostRepository.findOne(powerCost.getId());
        updatedPowerCost
            .city(UPDATED_CITY)
            .pricePerKilowatt(UPDATED_PRICE_PER_KILOWATT);

        restPowerCostMockMvc.perform(put("/api/power-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPowerCost)))
            .andExpect(status().isOk());

        // Validate the PowerCost in the database
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeUpdate);
        PowerCost testPowerCost = powerCostList.get(powerCostList.size() - 1);
        assertThat(testPowerCost.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPowerCost.getPricePerKilowatt()).isEqualTo(UPDATED_PRICE_PER_KILOWATT);

        // Validate the PowerCost in Elasticsearch
        PowerCost powerCostEs = powerCostSearchRepository.findOne(testPowerCost.getId());
        assertThat(powerCostEs).isEqualToComparingFieldByField(testPowerCost);
    }

    @Test
    @Transactional
    public void updateNonExistingPowerCost() throws Exception {
        int databaseSizeBeforeUpdate = powerCostRepository.findAll().size();

        // Create the PowerCost

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPowerCostMockMvc.perform(put("/api/power-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(powerCost)))
            .andExpect(status().isCreated());

        // Validate the PowerCost in the database
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePowerCost() throws Exception {
        // Initialize the database
        powerCostRepository.saveAndFlush(powerCost);
        powerCostSearchRepository.save(powerCost);
        int databaseSizeBeforeDelete = powerCostRepository.findAll().size();

        // Get the powerCost
        restPowerCostMockMvc.perform(delete("/api/power-costs/{id}", powerCost.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean powerCostExistsInEs = powerCostSearchRepository.exists(powerCost.getId());
        assertThat(powerCostExistsInEs).isFalse();

        // Validate the database is empty
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPowerCost() throws Exception {
        // Initialize the database
        powerCostRepository.saveAndFlush(powerCost);
        powerCostSearchRepository.save(powerCost);

        // Search the powerCost
        restPowerCostMockMvc.perform(get("/api/_search/power-costs?query=id:" + powerCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(powerCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].pricePerKilowatt").value(hasItem(DEFAULT_PRICE_PER_KILOWATT.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PowerCost.class);
        PowerCost powerCost1 = new PowerCost();
        powerCost1.setId(1L);
        PowerCost powerCost2 = new PowerCost();
        powerCost2.setId(powerCost1.getId());
        assertThat(powerCost1).isEqualTo(powerCost2);
        powerCost2.setId(2L);
        assertThat(powerCost1).isNotEqualTo(powerCost2);
        powerCost1.setId(null);
        assertThat(powerCost1).isNotEqualTo(powerCost2);
    }
}
