package by.bsuir.web.rest;

import by.bsuir.CourseworkApp;

import by.bsuir.domain.Cryptocurrency;
import by.bsuir.repository.CryptocurrencyRepository;
import by.bsuir.service.CryptocurrencyService;
import by.bsuir.repository.search.CryptocurrencySearchRepository;
import by.bsuir.service.dto.CryptocurrencyDTO;
import by.bsuir.service.mapper.CryptocurrencyMapper;
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
 * Test class for the CryptocurrencyResource REST controller.
 *
 * @see CryptocurrencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseworkApp.class)
public class CryptocurrencyResourceIntTest {

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

    @Autowired
    private CryptocurrencySearchRepository cryptocurrencySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCryptocurrencyMockMvc;

    private Cryptocurrency cryptocurrency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CryptocurrencyResource cryptocurrencyResource = new CryptocurrencyResource(cryptocurrencyService);
        this.restCryptocurrencyMockMvc = MockMvcBuilders.standaloneSetup(cryptocurrencyResource)
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
    public static Cryptocurrency createEntity(EntityManager em) {
        Cryptocurrency cryptocurrency = new Cryptocurrency()
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .price(DEFAULT_PRICE);
        return cryptocurrency;
    }

    @Before
    public void initTest() {
        cryptocurrencySearchRepository.deleteAll();
        cryptocurrency = createEntity(em);
    }

    @Test
    @Transactional
    public void createCryptocurrency() throws Exception {
        int databaseSizeBeforeCreate = cryptocurrencyRepository.findAll().size();

        // Create the Cryptocurrency
        CryptocurrencyDTO cryptocurrencyDTO = cryptocurrencyMapper.toDto(cryptocurrency);
        restCryptocurrencyMockMvc.perform(post("/api/cryptocurrencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
        Cryptocurrency cryptocurrencyEs = cryptocurrencySearchRepository.findOne(testCryptocurrency.getId());
        assertThat(cryptocurrencyEs).isEqualToComparingFieldByField(testCryptocurrency);
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cryptocurrencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeCreate);
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
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cryptocurrency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME.toString())))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cryptocurrency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME.toString()))
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
        cryptocurrencySearchRepository.save(cryptocurrency);
        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();

        // Update the cryptocurrency
        Cryptocurrency updatedCryptocurrency = cryptocurrencyRepository.findOne(cryptocurrency.getId());
        updatedCryptocurrency
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .price(UPDATED_PRICE);
        CryptocurrencyDTO cryptocurrencyDTO = cryptocurrencyMapper.toDto(updatedCryptocurrency);

        restCryptocurrencyMockMvc.perform(put("/api/cryptocurrencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
        Cryptocurrency cryptocurrencyEs = cryptocurrencySearchRepository.findOne(testCryptocurrency.getId());
        assertThat(cryptocurrencyEs).isEqualToComparingFieldByField(testCryptocurrency);
    }

    @Test
    @Transactional
    public void updateNonExistingCryptocurrency() throws Exception {
        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();

        // Create the Cryptocurrency
        CryptocurrencyDTO cryptocurrencyDTO = cryptocurrencyMapper.toDto(cryptocurrency);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCryptocurrencyMockMvc.perform(put("/api/cryptocurrencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cryptocurrencyDTO)))
            .andExpect(status().isCreated());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCryptocurrency() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);
        cryptocurrencySearchRepository.save(cryptocurrency);
        int databaseSizeBeforeDelete = cryptocurrencyRepository.findAll().size();

        // Get the cryptocurrency
        restCryptocurrencyMockMvc.perform(delete("/api/cryptocurrencies/{id}", cryptocurrency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cryptocurrencyExistsInEs = cryptocurrencySearchRepository.exists(cryptocurrency.getId());
        assertThat(cryptocurrencyExistsInEs).isFalse();

        // Validate the database is empty
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCryptocurrency() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);
        cryptocurrencySearchRepository.save(cryptocurrency);

        // Search the cryptocurrency
        restCryptocurrencyMockMvc.perform(get("/api/_search/cryptocurrencies?query=id:" + cryptocurrency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cryptocurrency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cryptocurrency.class);
        Cryptocurrency cryptocurrency1 = new Cryptocurrency();
        cryptocurrency1.setId(1L);
        Cryptocurrency cryptocurrency2 = new Cryptocurrency();
        cryptocurrency2.setId(cryptocurrency1.getId());
        assertThat(cryptocurrency1).isEqualTo(cryptocurrency2);
        cryptocurrency2.setId(2L);
        assertThat(cryptocurrency1).isNotEqualTo(cryptocurrency2);
        cryptocurrency1.setId(null);
        assertThat(cryptocurrency1).isNotEqualTo(cryptocurrency2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CryptocurrencyDTO.class);
        CryptocurrencyDTO cryptocurrencyDTO1 = new CryptocurrencyDTO();
        cryptocurrencyDTO1.setId(1L);
        CryptocurrencyDTO cryptocurrencyDTO2 = new CryptocurrencyDTO();
        assertThat(cryptocurrencyDTO1).isNotEqualTo(cryptocurrencyDTO2);
        cryptocurrencyDTO2.setId(cryptocurrencyDTO1.getId());
        assertThat(cryptocurrencyDTO1).isEqualTo(cryptocurrencyDTO2);
        cryptocurrencyDTO2.setId(2L);
        assertThat(cryptocurrencyDTO1).isNotEqualTo(cryptocurrencyDTO2);
        cryptocurrencyDTO1.setId(null);
        assertThat(cryptocurrencyDTO1).isNotEqualTo(cryptocurrencyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cryptocurrencyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cryptocurrencyMapper.fromId(null)).isNull();
    }
}
