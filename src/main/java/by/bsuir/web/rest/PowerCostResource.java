package by.bsuir.web.rest;

import by.bsuir.domain.PowerCost;
import by.bsuir.repository.PowerCostRepository;
import by.bsuir.repository.search.PowerCostSearchRepository;
import by.bsuir.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
 * REST controller for managing {@link by.bsuir.domain.PowerCost}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PowerCostResource {

    private final Logger log = LoggerFactory.getLogger(PowerCostResource.class);

    private static final String ENTITY_NAME = "powerCost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PowerCostRepository powerCostRepository;

    private final PowerCostSearchRepository powerCostSearchRepository;

    public PowerCostResource(PowerCostRepository powerCostRepository, PowerCostSearchRepository powerCostSearchRepository) {
        this.powerCostRepository = powerCostRepository;
        this.powerCostSearchRepository = powerCostSearchRepository;
    }

    /**
     * {@code POST  /power-costs} : Create a new powerCost.
     *
     * @param powerCost the powerCost to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new powerCost, or with status {@code 400 (Bad Request)} if the powerCost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/power-costs")
    public ResponseEntity<PowerCost> createPowerCost(@Valid @RequestBody PowerCost powerCost) throws URISyntaxException {
        log.debug("REST request to save PowerCost : {}", powerCost);
        if (powerCost.getId() != null) {
            throw new BadRequestAlertException("A new powerCost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PowerCost result = powerCostRepository.save(powerCost);
        powerCostSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/power-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /power-costs} : Updates an existing powerCost.
     *
     * @param powerCost the powerCost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated powerCost,
     * or with status {@code 400 (Bad Request)} if the powerCost is not valid,
     * or with status {@code 500 (Internal Server Error)} if the powerCost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/power-costs")
    public ResponseEntity<PowerCost> updatePowerCost(@Valid @RequestBody PowerCost powerCost) throws URISyntaxException {
        log.debug("REST request to update PowerCost : {}", powerCost);
        if (powerCost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PowerCost result = powerCostRepository.save(powerCost);
        powerCostSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, powerCost.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /power-costs} : get all the powerCosts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of powerCosts in body.
     */
    @GetMapping("/power-costs")
    public List<PowerCost> getAllPowerCosts() {
        log.debug("REST request to get all PowerCosts");
        return powerCostRepository.findAll();
    }

    /**
     * {@code GET  /power-costs/:id} : get the "id" powerCost.
     *
     * @param id the id of the powerCost to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the powerCost, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/power-costs/{id}")
    public ResponseEntity<PowerCost> getPowerCost(@PathVariable Long id) {
        log.debug("REST request to get PowerCost : {}", id);
        Optional<PowerCost> powerCost = powerCostRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(powerCost);
    }

    /**
     * {@code DELETE  /power-costs/:id} : delete the "id" powerCost.
     *
     * @param id the id of the powerCost to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/power-costs/{id}")
    public ResponseEntity<Void> deletePowerCost(@PathVariable Long id) {
        log.debug("REST request to delete PowerCost : {}", id);
        powerCostRepository.deleteById(id);
        powerCostSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/power-costs?query=:query} : search for the powerCost corresponding
     * to the query.
     *
     * @param query the query of the powerCost search.
     * @return the result of the search.
     */
    @GetMapping("/_search/power-costs")
    public List<PowerCost> searchPowerCosts(@RequestParam String query) {
        log.debug("REST request to search PowerCosts for query {}", query);
        return StreamSupport
            .stream(powerCostSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
