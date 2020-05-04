package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;
import by.bsuir.domain.Cryptocurrency;
import by.bsuir.repository.CryptocurrencyRepository;
import by.bsuir.repository.search.CryptocurrencySearchRepository;
import by.bsuir.service.CryptocurrencyService;
import by.bsuir.service.dto.CryptocurrencyDTO;
import by.bsuir.service.mapper.CryptocurrencyMapper;

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
 * Integration tests for the {@link CryptocurrencyResource} REST controller.
 */
@SpringBootTest(classes = CourseworkApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CryptocurrencyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBB";

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;

    @Autowired
    private CryptocurrencyMapper cryptocurrencyMapper;

    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    /**
     * This repository is mocked in the by.bsuir.repository.search test package.
     *
     * @see by.bsuir.repository.search.CryptocurrencySearchRepositoryMockConfiguration
     */
    @Autowired
    private CryptocurrencySearchRepository mockCryptocurrencySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCryptocurrencyMockMvc;

    private Cryptocurrency cryptocurrency;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cryptocurrency createEntity(EntityManager em) {
        Cryptocurrency cryptocurrency = new Cryptocurrency()
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .price(DEFAULT_PRICE);
        return cryptocurrency;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cryptocurrency createUpdatedEntity(EntityManager em) {
        Cryptocurrency cryptocurrency = new Cryptocurrency()
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .price(UPDATED_PRICE);
        return cryptocurrency;
    }

    @BeforeEach
    public void initTest() {
        cryptocurrency = createEntity(em);
    }

    @Test
    @Transactional
    public void createCryptocurrency() throws Exception {
        int databaseSizeBeforeCreate = cryptocurrencyRepository.findAll().size();

        // Create the Cryptocurrency
        CryptocurrencyDTO cryptocurrencyDTO = cryptocurrencyMapper.toDto(cryptocurrency);
        restCryptocurrencyMockMvc.perform(post("/api/cryptocurrencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cryptocurrencyDTO)))
            .andExpect(status().isCreated());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeCreate + 1);
        Cryptocurrency testCryptocurrency = cryptocurrencyList.get(cryptocurrencyList.size() - 1);
        assertThat(testCryptocurrency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCryptocurrency.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testCryptocurrency.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the Cryptocurrency in Elasticsearch
        verify(mockCryptocurrencySearchRepository, times(1)).save(testCryptocurrency);
    }

    @Test
    @Transactional
    public void createCryptocurrencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cryptocurrencyRepository.findAll().size();

        // Create the Cryptocurrency with an existing ID
        cryptocurrency.setId(1L);
        CryptocurrencyDTO cryptocurrencyDTO = cryptocurrencyMapper.toDto(cryptocurrency);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCryptocurrencyMockMvc.perform(post("/api/cryptocurrencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cryptocurrencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cryptocurrency in Elasticsearch
        verify(mockCryptocurrencySearchRepository, times(0)).save(cryptocurrency);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cryptocurrencyRepository.findAll().size();
        // set the field null
        cryptocurrency.setName(null);

        // Create the Cryptocurrency, which fails.
        CryptocurrencyDTO cryptocurrencyDTO = cryptocurrencyMapper.toDto(cryptocurrency);

        restCryptocurrencyMockMvc.perform(post("/api/cryptocurrencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cryptocurrencyDTO)))
            .andExpect(status().isBadRequest());

        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCryptocurrencies() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        // Get all the cryptocurrencyList
        restCryptocurrencyMockMvc.perform(get("/api/cryptocurrencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cryptocurrency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCryptocurrency() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        // Get the cryptocurrency
        restCryptocurrencyMockMvc.perform(get("/api/cryptocurrencies/{id}", cryptocurrency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cryptocurrency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCryptocurrency() throws Exception {
        // Get the cryptocurrency
        restCryptocurrencyMockMvc.perform(get("/api/cryptocurrencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCryptocurrency() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();

        // Update the cryptocurrency
        Cryptocurrency updatedCryptocurrency = cryptocurrencyRepository.findById(cryptocurrency.getId()).get();
        // Disconnect from session so that the updates on updatedCryptocurrency are not directly saved in db
        em.detach(updatedCryptocurrency);
        updatedCryptocurrency
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .price(UPDATED_PRICE);
        CryptocurrencyDTO cryptocurrencyDTO = cryptocurrencyMapper.toDto(updatedCryptocurrency);

        restCryptocurrencyMockMvc.perform(put("/api/cryptocurrencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cryptocurrencyDTO)))
            .andExpect(status().isOk());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
        Cryptocurrency testCryptocurrency = cryptocurrencyList.get(cryptocurrencyList.size() - 1);
        assertThat(testCryptocurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCryptocurrency.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testCryptocurrency.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the Cryptocurrency in Elasticsearch
        verify(mockCryptocurrencySearchRepository, times(1)).save(testCryptocurrency);
    }

    @Test
    @Transactional
    public void updateNonExistingCryptocurrency() throws Exception {
        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();

        // Create the Cryptocurrency
        CryptocurrencyDTO cryptocurrencyDTO = cryptocurrencyMapper.toDto(cryptocurrency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCryptocurrencyMockMvc.perform(put("/api/cryptocurrencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cryptocurrencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cryptocurrency in Elasticsearch
        verify(mockCryptocurrencySearchRepository, times(0)).save(cryptocurrency);
    }

    @Test
    @Transactional
    public void deleteCryptocurrency() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        int databaseSizeBeforeDelete = cryptocurrencyRepository.findAll().size();

        // Delete the cryptocurrency
        restCryptocurrencyMockMvc.perform(delete("/api/cryptocurrencies/{id}", cryptocurrency.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cryptocurrency in Elasticsearch
        verify(mockCryptocurrencySearchRepository, times(1)).deleteById(cryptocurrency.getId());
    }

    @Test
    @Transactional
    public void searchCryptocurrency() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);
        when(mockCryptocurrencySearchRepository.search(queryStringQuery("id:" + cryptocurrency.getId())))
            .thenReturn(Collections.singletonList(cryptocurrency));
        // Search the cryptocurrency
        restCryptocurrencyMockMvc.perform(get("/api/_search/cryptocurrencies?query=id:" + cryptocurrency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cryptocurrency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
}
