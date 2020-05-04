package by.bsuir.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.bsuir.service.CryptocurrencyService;
import by.bsuir.web.rest.util.HeaderUtil;
import by.bsuir.service.dto.CryptocurrencyDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Cryptocurrency.
 */
@RestController
@RequestMapping("/api")
public class CryptocurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CryptocurrencyResource.class);

    private static final String ENTITY_NAME = "cryptocurrency";

    private final CryptocurrencyService cryptocurrencyService;

    public CryptocurrencyResource(CryptocurrencyService cryptocurrencyService) {
        this.cryptocurrencyService = cryptocurrencyService;
    }

    /**
     * POST  /cryptocurrencies : Create a new cryptocurrency.
     *
     * @param cryptocurrencyDTO the cryptocurrencyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cryptocurrencyDTO, or with status 400 (Bad Request) if the cryptocurrency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cryptocurrencies")
    @Timed
    public ResponseEntity<CryptocurrencyDTO> createCryptocurrency(@Valid @RequestBody CryptocurrencyDTO cryptocurrencyDTO) throws URISyntaxException {
        log.debug("REST request to save Cryptocurrency : {}", cryptocurrencyDTO);
        if (cryptocurrencyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cryptocurrency cannot already have an ID")).body(null);
        }
        CryptocurrencyDTO result = cryptocurrencyService.save(cryptocurrencyDTO);
        return ResponseEntity.created(new URI("/api/cryptocurrencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cryptocurrencies : Updates an existing cryptocurrency.
     *
     * @param cryptocurrencyDTO the cryptocurrencyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cryptocurrencyDTO,
     * or with status 400 (Bad Request) if the cryptocurrencyDTO is not valid,
     * or with status 500 (Internal Server Error) if the cryptocurrencyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cryptocurrencies")
    @Timed
    public ResponseEntity<CryptocurrencyDTO> updateCryptocurrency(@Valid @RequestBody CryptocurrencyDTO cryptocurrencyDTO) throws URISyntaxException {
        log.debug("REST request to update Cryptocurrency : {}", cryptocurrencyDTO);
        if (cryptocurrencyDTO.getId() == null) {
            return createCryptocurrency(cryptocurrencyDTO);
        }
        CryptocurrencyDTO result = cryptocurrencyService.save(cryptocurrencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cryptocurrencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cryptocurrencies : get all the cryptocurrencies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cryptocurrencies in body
     */
    @GetMapping("/cryptocurrencies")
    @Timed
    public List<CryptocurrencyDTO> getAllCryptocurrencies() {
        log.debug("REST request to get all Cryptocurrencies");
        return cryptocurrencyService.findAll();
        }

    /**
     * GET  /cryptocurrencies/:id : get the "id" cryptocurrency.
     *
     * @param id the id of the cryptocurrencyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cryptocurrencyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cryptocurrencies/{id}")
    @Timed
    public ResponseEntity<CryptocurrencyDTO> getCryptocurrency(@PathVariable Long id) {
        log.debug("REST request to get Cryptocurrency : {}", id);
        CryptocurrencyDTO cryptocurrencyDTO = cryptocurrencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cryptocurrencyDTO));
    }

    /**
     * DELETE  /cryptocurrencies/:id : delete the "id" cryptocurrency.
     *
     * @param id the id of the cryptocurrencyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cryptocurrencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCryptocurrency(@PathVariable Long id) {
        log.debug("REST request to delete Cryptocurrency : {}", id);
        cryptocurrencyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cryptocurrencies?query=:query : search for the cryptocurrency corresponding
     * to the query.
     *
     * @param query the query of the cryptocurrency search
     * @return the result of the search
     */
    @GetMapping("/_search/cryptocurrencies")
    @Timed
    public List<CryptocurrencyDTO> searchCryptocurrencies(@RequestParam String query) {
        log.debug("REST request to search Cryptocurrencies for query {}", query);
        return cryptocurrencyService.search(query);
    }

}
