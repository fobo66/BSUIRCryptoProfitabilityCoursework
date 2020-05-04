package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;
import by.bsuir.domain.PowerCost;
import by.bsuir.repository.PowerCostRepository;
import by.bsuir.repository.search.PowerCostSearchRepository;

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
 * Integration tests for the {@link PowerCostResource} REST controller.
 */
@SpringBootTest(classes = CourseworkApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PowerCostResourceIT {

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE_PER_KILOWATT = 0D;
    private static final Double UPDATED_PRICE_PER_KILOWATT = 1D;

    @Autowired
    private PowerCostRepository powerCostRepository;

    /**
     * This repository is mocked in the by.bsuir.repository.search test package.
     *
     * @see by.bsuir.repository.search.PowerCostSearchRepositoryMockConfiguration
     */
    @Autowired
    private PowerCostSearchRepository mockPowerCostSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPowerCostMockMvc;

    private PowerCost powerCost;

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
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PowerCost createUpdatedEntity(EntityManager em) {
        PowerCost powerCost = new PowerCost()
            .city(UPDATED_CITY)
            .pricePerKilowatt(UPDATED_PRICE_PER_KILOWATT);
        return powerCost;
    }

    @BeforeEach
    public void initTest() {
        powerCost = createEntity(em);
    }

    @Test
    @Transactional
    public void createPowerCost() throws Exception {
        int databaseSizeBeforeCreate = powerCostRepository.findAll().size();

        // Create the PowerCost
        restPowerCostMockMvc.perform(post("/api/power-costs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(powerCost)))
            .andExpect(status().isCreated());

        // Validate the PowerCost in the database
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeCreate + 1);
        PowerCost testPowerCost = powerCostList.get(powerCostList.size() - 1);
        assertThat(testPowerCost.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPowerCost.getPricePerKilowatt()).isEqualTo(DEFAULT_PRICE_PER_KILOWATT);

        // Validate the PowerCost in Elasticsearch
        verify(mockPowerCostSearchRepository, times(1)).save(testPowerCost);
    }

    @Test
    @Transactional
    public void createPowerCostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = powerCostRepository.findAll().size();

        // Create the PowerCost with an existing ID
        powerCost.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPowerCostMockMvc.perform(post("/api/power-costs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(powerCost)))
            .andExpect(status().isBadRequest());

        // Validate the PowerCost in the database
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeCreate);

        // Validate the PowerCost in Elasticsearch
        verify(mockPowerCostSearchRepository, times(0)).save(powerCost);
    }


    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = powerCostRepository.findAll().size();
        // set the field null
        powerCost.setCity(null);

        // Create the PowerCost, which fails.

        restPowerCostMockMvc.perform(post("/api/power-costs")
            .contentType(MediaType.APPLICATION_JSON)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(powerCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(powerCost.getId().intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
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

        int databaseSizeBeforeUpdate = powerCostRepository.findAll().size();

        // Update the powerCost
        PowerCost updatedPowerCost = powerCostRepository.findById(powerCost.getId()).get();
        // Disconnect from session so that the updates on updatedPowerCost are not directly saved in db
        em.detach(updatedPowerCost);
        updatedPowerCost
            .city(UPDATED_CITY)
            .pricePerKilowatt(UPDATED_PRICE_PER_KILOWATT);

        restPowerCostMockMvc.perform(put("/api/power-costs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPowerCost)))
            .andExpect(status().isOk());

        // Validate the PowerCost in the database
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeUpdate);
        PowerCost testPowerCost = powerCostList.get(powerCostList.size() - 1);
        assertThat(testPowerCost.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPowerCost.getPricePerKilowatt()).isEqualTo(UPDATED_PRICE_PER_KILOWATT);

        // Validate the PowerCost in Elasticsearch
        verify(mockPowerCostSearchRepository, times(1)).save(testPowerCost);
    }

    @Test
    @Transactional
    public void updateNonExistingPowerCost() throws Exception {
        int databaseSizeBeforeUpdate = powerCostRepository.findAll().size();

        // Create the PowerCost

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPowerCostMockMvc.perform(put("/api/power-costs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(powerCost)))
            .andExpect(status().isBadRequest());

        // Validate the PowerCost in the database
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PowerCost in Elasticsearch
        verify(mockPowerCostSearchRepository, times(0)).save(powerCost);
    }

    @Test
    @Transactional
    public void deletePowerCost() throws Exception {
        // Initialize the database
        powerCostRepository.saveAndFlush(powerCost);

        int databaseSizeBeforeDelete = powerCostRepository.findAll().size();

        // Delete the powerCost
        restPowerCostMockMvc.perform(delete("/api/power-costs/{id}", powerCost.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PowerCost> powerCostList = powerCostRepository.findAll();
        assertThat(powerCostList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PowerCost in Elasticsearch
        verify(mockPowerCostSearchRepository, times(1)).deleteById(powerCost.getId());
    }

    @Test
    @Transactional
    public void searchPowerCost() throws Exception {
        // Initialize the database
        powerCostRepository.saveAndFlush(powerCost);
        when(mockPowerCostSearchRepository.search(queryStringQuery("id:" + powerCost.getId())))
            .thenReturn(Collections.singletonList(powerCost));
        // Search the powerCost
        restPowerCostMockMvc.perform(get("/api/_search/power-costs?query=id:" + powerCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(powerCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].pricePerKilowatt").value(hasItem(DEFAULT_PRICE_PER_KILOWATT.doubleValue())));
    }
}
