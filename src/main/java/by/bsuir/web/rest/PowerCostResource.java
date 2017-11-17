package by.bsuir.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.bsuir.domain.PowerCost;

import by.bsuir.repository.PowerCostRepository;
import by.bsuir.repository.search.PowerCostSearchRepository;
import by.bsuir.web.rest.util.HeaderUtil;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PowerCost.
 */
@RestController
@RequestMapping("/api")
public class PowerCostResource {

    private static final String ENTITY_NAME = "powerCost";
    private final Logger log = LoggerFactory.getLogger(PowerCostResource.class);
    private final PowerCostRepository powerCostRepository;

    private final PowerCostSearchRepository powerCostSearchRepository;

    public PowerCostResource(PowerCostRepository powerCostRepository, PowerCostSearchRepository powerCostSearchRepository) {
        this.powerCostRepository = powerCostRepository;
        this.powerCostSearchRepository = powerCostSearchRepository;
    }

    /**
     * POST  /power-costs : Create a new powerCost.
     *
     * @param powerCost the powerCost to create
     * @return the ResponseEntity with status 201 (Created) and with body the new powerCost, or with status 400 (Bad Request) if the powerCost has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/power-costs")
    @Timed
    public ResponseEntity<PowerCost> createPowerCost(@Valid @RequestBody PowerCost powerCost) throws URISyntaxException {
        log.debug("REST request to save PowerCost : {}", powerCost);
        if (powerCost.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new powerCost cannot already have an ID")).body(null);
        }
        PowerCost result = powerCostRepository.save(powerCost);
        powerCostSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/power-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /power-costs : Updates an existing powerCost.
     *
     * @param powerCost the powerCost to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated powerCost,
     * or with status 400 (Bad Request) if the powerCost is not valid,
     * or with status 500 (Internal Server Error) if the powerCost couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/power-costs")
    @Timed
    public ResponseEntity<PowerCost> updatePowerCost(@Valid @RequestBody PowerCost powerCost) throws URISyntaxException {
        log.debug("REST request to update PowerCost : {}", powerCost);
        if (powerCost.getId() == null) {
            return createPowerCost(powerCost);
        }
        PowerCost result = powerCostRepository.save(powerCost);
        powerCostSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, powerCost.getId().toString()))
            .body(result);
    }

    /**
     * GET  /power-costs : get all the powerCosts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of powerCosts in body
     */
    @GetMapping("/power-costs")
    @Timed
    public List<PowerCost> getAllPowerCosts() {
        log.debug("REST request to get all PowerCosts");
        return powerCostRepository.findAll();
    }

    /**
     * GET  /power-costs/:id : get the "id" powerCost.
     *
     * @param id the id of the powerCost to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the powerCost, or with status 404 (Not Found)
     */
    @GetMapping("/power-costs/{id}")
    @Timed
    public ResponseEntity<PowerCost> getPowerCost(@PathVariable Long id) {
        log.debug("REST request to get PowerCost : {}", id);
        PowerCost powerCost = powerCostRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(powerCost));
    }

    /**
     * DELETE  /power-costs/:id : delete the "id" powerCost.
     *
     * @param id the id of the powerCost to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/power-costs/{id}")
    @Timed
    public ResponseEntity<Void> deletePowerCost(@PathVariable Long id) {
        log.debug("REST request to delete PowerCost : {}", id);
        powerCostRepository.delete(id);
        powerCostSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/power-costs?query=:query : search for the powerCost corresponding
     * to the query.
     *
     * @param query the query of the powerCost search
     * @return the result of the search
     */
    @GetMapping("/_search/power-costs")
    @Timed
    public List<PowerCost> searchPowerCosts(@RequestParam String query) {
        log.debug("REST request to search PowerCosts for query {}", query);
        return StreamSupport
            .stream(powerCostSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
