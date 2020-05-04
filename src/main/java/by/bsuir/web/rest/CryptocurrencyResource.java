package by.bsuir.web.rest;

import by.bsuir.service.CryptocurrencyService;
import by.bsuir.web.rest.errors.BadRequestAlertException;
import by.bsuir.service.dto.CryptocurrencyDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link by.bsuir.domain.Cryptocurrency}.
 */
@RestController
@RequestMapping("/api")
public class CryptocurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CryptocurrencyResource.class);

    private static final String ENTITY_NAME = "cryptocurrency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CryptocurrencyService cryptocurrencyService;

    public CryptocurrencyResource(CryptocurrencyService cryptocurrencyService) {
        this.cryptocurrencyService = cryptocurrencyService;
    }

    /**
     * {@code POST  /cryptocurrencies} : Create a new cryptocurrency.
     *
     * @param cryptocurrencyDTO the cryptocurrencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cryptocurrencyDTO, or with status {@code 400 (Bad Request)} if the cryptocurrency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cryptocurrencies")
    public ResponseEntity<CryptocurrencyDTO> createCryptocurrency(@Valid @RequestBody CryptocurrencyDTO cryptocurrencyDTO) throws URISyntaxException {
        log.debug("REST request to save Cryptocurrency : {}", cryptocurrencyDTO);
        if (cryptocurrencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new cryptocurrency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CryptocurrencyDTO result = cryptocurrencyService.save(cryptocurrencyDTO);
        return ResponseEntity.created(new URI("/api/cryptocurrencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cryptocurrencies} : Updates an existing cryptocurrency.
     *
     * @param cryptocurrencyDTO the cryptocurrencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cryptocurrencyDTO,
     * or with status {@code 400 (Bad Request)} if the cryptocurrencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cryptocurrencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cryptocurrencies")
    public ResponseEntity<CryptocurrencyDTO> updateCryptocurrency(@Valid @RequestBody CryptocurrencyDTO cryptocurrencyDTO) throws URISyntaxException {
        log.debug("REST request to update Cryptocurrency : {}", cryptocurrencyDTO);
        if (cryptocurrencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CryptocurrencyDTO result = cryptocurrencyService.save(cryptocurrencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cryptocurrencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cryptocurrencies} : get all the cryptocurrencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cryptocurrencies in body.
     */
    @GetMapping("/cryptocurrencies")
    public List<CryptocurrencyDTO> getAllCryptocurrencies() {
        log.debug("REST request to get all Cryptocurrencies");
        return cryptocurrencyService.findAll();
    }

    /**
     * {@code GET  /cryptocurrencies/:id} : get the "id" cryptocurrency.
     *
     * @param id the id of the cryptocurrencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cryptocurrencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cryptocurrencies/{id}")
    public ResponseEntity<CryptocurrencyDTO> getCryptocurrency(@PathVariable Long id) {
        log.debug("REST request to get Cryptocurrency : {}", id);
        Optional<CryptocurrencyDTO> cryptocurrencyDTO = cryptocurrencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cryptocurrencyDTO);
    }

    /**
     * {@code DELETE  /cryptocurrencies/:id} : delete the "id" cryptocurrency.
     *
     * @param id the id of the cryptocurrencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cryptocurrencies/{id}")
    public ResponseEntity<Void> deleteCryptocurrency(@PathVariable Long id) {
        log.debug("REST request to delete Cryptocurrency : {}", id);
        cryptocurrencyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/cryptocurrencies?query=:query} : search for the cryptocurrency corresponding
     * to the query.
     *
     * @param query the query of the cryptocurrency search.
     * @return the result of the search.
     */
    @GetMapping("/_search/cryptocurrencies")
    public List<CryptocurrencyDTO> searchCryptocurrencies(@RequestParam String query) {
        log.debug("REST request to search Cryptocurrencies for query {}", query);
        return cryptocurrencyService.search(query);
    }
}
