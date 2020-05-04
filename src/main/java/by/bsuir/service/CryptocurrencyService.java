package by.bsuir.service;

import by.bsuir.domain.Cryptocurrency;
import by.bsuir.repository.CryptocurrencyRepository;
import by.bsuir.repository.search.CryptocurrencySearchRepository;
import by.bsuir.service.dto.CryptocurrencyDTO;
import by.bsuir.service.mapper.CryptocurrencyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Cryptocurrency}.
 */
@Service
@Transactional
public class CryptocurrencyService {

    private final Logger log = LoggerFactory.getLogger(CryptocurrencyService.class);

    private final CryptocurrencyRepository cryptocurrencyRepository;

    private final CryptocurrencyMapper cryptocurrencyMapper;

    private final CryptocurrencySearchRepository cryptocurrencySearchRepository;

    public CryptocurrencyService(CryptocurrencyRepository cryptocurrencyRepository, CryptocurrencyMapper cryptocurrencyMapper, CryptocurrencySearchRepository cryptocurrencySearchRepository) {
        this.cryptocurrencyRepository = cryptocurrencyRepository;
        this.cryptocurrencyMapper = cryptocurrencyMapper;
        this.cryptocurrencySearchRepository = cryptocurrencySearchRepository;
    }

    /**
     * Save a cryptocurrency.
     *
     * @param cryptocurrencyDTO the entity to save.
     * @return the persisted entity.
     */
    public CryptocurrencyDTO save(CryptocurrencyDTO cryptocurrencyDTO) {
        log.debug("Request to save Cryptocurrency : {}", cryptocurrencyDTO);
        Cryptocurrency cryptocurrency = cryptocurrencyMapper.toEntity(cryptocurrencyDTO);
        cryptocurrency = cryptocurrencyRepository.save(cryptocurrency);
        CryptocurrencyDTO result = cryptocurrencyMapper.toDto(cryptocurrency);
        cryptocurrencySearchRepository.save(cryptocurrency);
        return result;
    }

    /**
     * Get all the cryptocurrencies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CryptocurrencyDTO> findAll() {
        log.debug("Request to get all Cryptocurrencies");
        return cryptocurrencyRepository.findAll().stream()
            .map(cryptocurrencyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one cryptocurrency by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CryptocurrencyDTO> findOne(Long id) {
        log.debug("Request to get Cryptocurrency : {}", id);
        return cryptocurrencyRepository.findById(id)
            .map(cryptocurrencyMapper::toDto);
    }

    /**
     * Delete the cryptocurrency by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cryptocurrency : {}", id);
        cryptocurrencyRepository.deleteById(id);
        cryptocurrencySearchRepository.deleteById(id);
    }

    /**
     * Search for the cryptocurrency corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CryptocurrencyDTO> search(String query) {
        log.debug("Request to search Cryptocurrencies for query {}", query);
        return StreamSupport
            .stream(cryptocurrencySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(cryptocurrencyMapper::toDto)
            .collect(Collectors.toList());
    }
}
