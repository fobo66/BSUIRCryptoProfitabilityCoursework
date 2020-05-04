package by.bsuir.web.rest;

import by.bsuir.domain.ProfitabilityAnalysis;
import by.bsuir.repository.ProfitabilityAnalysisRepository;
import by.bsuir.repository.search.ProfitabilityAnalysisSearchRepository;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing {@link by.bsuir.domain.ProfitabilityAnalysis}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProfitabilityAnalysisResource {

    private static final String ENTITY_NAME = "profitabilityAnalysis";

    private final Logger log = LoggerFactory.getLogger(ProfitabilityAnalysisResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfitabilityAnalysisRepository profitabilityAnalysisRepository;

    private final ProfitabilityAnalysisSearchRepository profitabilityAnalysisSearchRepository;

    public ProfitabilityAnalysisResource(ProfitabilityAnalysisRepository profitabilityAnalysisRepository, ProfitabilityAnalysisSearchRepository profitabilityAnalysisSearchRepository) {
        this.profitabilityAnalysisRepository = profitabilityAnalysisRepository;
        this.profitabilityAnalysisSearchRepository = profitabilityAnalysisSearchRepository;
    }

    /**
     * {@code POST  /profitability-analyses} : Create a new profitabilityAnalysis.
     *
     * @param profitabilityAnalysis the profitabilityAnalysis to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profitabilityAnalysis, or with status {@code 400 (Bad Request)} if the profitabilityAnalysis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profitability-analyses")
    public ResponseEntity<ProfitabilityAnalysis> createProfitabilityAnalysis(@Valid @RequestBody ProfitabilityAnalysis profitabilityAnalysis) throws URISyntaxException {
        log.debug("REST request to save ProfitabilityAnalysis : {}", profitabilityAnalysis);
        if (profitabilityAnalysis.getId() != null) {
            throw new BadRequestAlertException("A new profitabilityAnalysis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfitabilityAnalysis result = profitabilityAnalysisRepository.save(profitabilityAnalysis);
        profitabilityAnalysisSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/profitability-analyses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /profitability-analyses} : Updates an existing profitabilityAnalysis.
     *
     * @param profitabilityAnalysis the profitabilityAnalysis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profitabilityAnalysis,
     * or with status {@code 400 (Bad Request)} if the profitabilityAnalysis is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profitabilityAnalysis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profitability-analyses")
    public ResponseEntity<ProfitabilityAnalysis> updateProfitabilityAnalysis(@Valid @RequestBody ProfitabilityAnalysis profitabilityAnalysis) throws URISyntaxException {
        log.debug("REST request to update ProfitabilityAnalysis : {}", profitabilityAnalysis);
        if (profitabilityAnalysis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfitabilityAnalysis result = profitabilityAnalysisRepository.save(profitabilityAnalysis);
        profitabilityAnalysisSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profitabilityAnalysis.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /profitability-analyses} : get all the profitabilityAnalyses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profitabilityAnalyses in body.
     */
    @GetMapping("/profitability-analyses")
    public List<ProfitabilityAnalysis> getAllProfitabilityAnalyses() {
        log.debug("REST request to get all ProfitabilityAnalyses");
        return profitabilityAnalysisRepository.findAll();
    }

    /**
     * {@code GET  /profitability-analyses/:id} : get the "id" profitabilityAnalysis.
     *
     * @param id the id of the profitabilityAnalysis to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profitabilityAnalysis, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profitability-analyses/{id}")
    public ResponseEntity<ProfitabilityAnalysis> getProfitabilityAnalysis(@PathVariable Long id) {
        log.debug("REST request to get ProfitabilityAnalysis : {}", id);
        Optional<ProfitabilityAnalysis> profitabilityAnalysis = profitabilityAnalysisRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(profitabilityAnalysis);
    }

    /**
     * {@code DELETE  /profitability-analyses/:id} : delete the "id" profitabilityAnalysis.
     *
     * @param id the id of the profitabilityAnalysis to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profitability-analyses/{id}")
    public ResponseEntity<Void> deleteProfitabilityAnalysis(@PathVariable Long id) {
        log.debug("REST request to delete ProfitabilityAnalysis : {}", id);
        profitabilityAnalysisRepository.deleteById(id);
        profitabilityAnalysisSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/profitability-analyses?query=:query} : search for the profitabilityAnalysis corresponding
     * to the query.
     *
     * @param query the query of the profitabilityAnalysis search.
     * @return the result of the search.
     */
    @GetMapping("/_search/profitability-analyses")
    public List<ProfitabilityAnalysis> searchProfitabilityAnalyses(@RequestParam String query) {
        log.debug("REST request to search ProfitabilityAnalyses for query {}", query);
        return StreamSupport
            .stream(profitabilityAnalysisSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
